package no.sintef.simpleAsync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.Holder;

@WebService(serviceName = "simpleAsync")
public class simpleAsync {

    private final String namespace = "http://simpleAsync.sintef.no/";

    // Name your service here, and add your required input and output parameters.
    // Note that output parameters should be of Holder objects
    @WebMethod(operationName = "startSimplestAsync")
    public void startSimplestAsync(
            @WebParam(name = "serviceID", 
                    targetNamespace = namespace, 
                    mode = WebParam.Mode.IN) String serviceID,
            @WebParam(name = "sessionToken", 
                    targetNamespace = namespace, 
                    mode = WebParam.Mode.IN) String sessionToken,
            @WebParam(name = "outputMessage", 
                    targetNamespace = namespace, 
                    mode = WebParam.Mode.OUT) Holder<String> outputMessage,
            @WebParam(name = "outputCode", 
                    targetNamespace = namespace, 
                    mode = WebParam.Mode.OUT) Holder<String> outputCode,
            @WebParam(name =  "status_base64", 
                    targetNamespace = namespace, 
                    mode = WebParam.Mode.OUT) Holder<String> status_base64) 
    {
        log("SimplestAsyncEver.startSimplestAsync - started with input:" + 
                "\n\tserviceID =" + serviceID + 
                "\n\tsessionToken =" + sessionToken);
        
        BufferedReader reader = null;
        try {
            
            // Get the status file from the remote job
            //String command = "python startDummy.py";           
            //String command = "pwd";
            ProcessBuilder procBuilder =new ProcessBuilder("python","/usr/local/bin/startDummy.py");
            procBuilder.redirectErrorStream(true);
            
            Process proc = procBuilder.start();  
            
            reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String logMsg = "Output:\n";
            String line;
            while ((line = reader.readLine()) != null){
                logMsg += line;
            }
            proc.waitFor();
            log("SimplestAsyncEver.startSimplestAsync - Finished up without trouble - logMsg:" + logMsg);
            
            String unset = "unset";
            status_base64.value = "UNCHANGED";
            outputCode.value = unset;
            outputMessage.value = unset;
            //return output + "\nSuccess";
        } catch (Throwable t) {
            error(t.getMessage());
            //return t.getMessage();
        } finally{
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {              
                error(ex);
            }
        }
    }
    

    
    // This method is called repetively by WFM
    @WebMethod(operationName = "getServiceStatus")
    public void getServiceStatus(
            @WebParam(name = "serviceID",
                    targetNamespace = namespace, 
                    mode = WebParam.Mode.IN) String serviceID,
            @WebParam(name = "sessionToken",
                    targetNamespace = namespace,
                    mode = WebParam.Mode.IN) String sessionToken,
            @WebParam(name = "outputMessage", 
                    targetNamespace = namespace, 
                    mode = WebParam.Mode.OUT) Holder<String> outputMessage,
            @WebParam(name = "outputCode", 
                    targetNamespace = namespace, 
                    mode = WebParam.Mode.OUT) Holder<String> outputCode,
            @WebParam(name =  "status_base64", 
                    targetNamespace = namespace, 
                    mode = WebParam.Mode.OUT) Holder<String> status_base64) 
    {
        
        
        log("SimplestAsyncEver.getServiceStatus - started with input:" + 
                "\n\tserviceID =" + serviceID + 
                "\n\tsessionToken =" + sessionToken);
        
        BufferedReader reader = null;
        try {
            
            // Get the status file from the remote job
            //String command = "python startDummy.py";           
            //String command = "pwd";
            ProcessBuilder procBuilder =new ProcessBuilder("python","/usr/local/bin/getStatusDummy.py");
            procBuilder.redirectErrorStream(true);
            
            Process proc = procBuilder.start();  
            
            reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String keyValuePairs = "";
            String line;
            while ((line = reader.readLine()) != null){
                keyValuePairs += line;
            }
            proc.waitFor();
            log("SimplestAsyncEver.getServiceStatus - read from file:" + keyValuePairs);
            String code = getValueFromKey(keyValuePairs, "code");
            String message = getValueFromKey(keyValuePairs, "message");
            
            if( code.equals("20") ) {
                // This should be the only value here, as this async service
                // is designed to return at first getServiceStatus.
                log("\nCOMPLETED\n");
                status_base64.value = "COMPLETED";
                outputCode.value = code;
                outputMessage.value = message;
            } else {
                // But if something wrong has happened, display status bar at 17%
                String html = htmlStatusBar("17");
                status_base64.value = DatatypeConverter.printBase64Binary(html.getBytes());
            }
            
            //return output + "\nSuccess";
        } catch (Throwable t) {
            error(t.getMessage());
            //return t.getMessage();
        } finally{
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {              
                error(ex);
            }
        }
    }
    
    
    
    
    /*
    *  Key-value parser
    */
    private String getValueFromKey(String keyValueString, String key) {
        String[] map = keyValueString.split(",");
        for (String pair : map) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2 ) {
                if (keyValue[0].equals(key)) {
                    log("::getValueFromKey - found correct key: {" + keyValue[0] + ", " + keyValue[1] + "}");
                    return keyValue[1];
                }
            }
            else {
                String errorLogMsg = "Something is wrong with the extraParameters! Found key-value pair of length: " + keyValue.length + "\n{";
                for (String str : keyValue) {
                    errorLogMsg += keyValue + str + ", ";
                }
                log("::getValueFromKey\n" + errorLogMsg + "}");
            }
        }
        return "notFound";
    }
    
    /*
    *  Utility function for HTML progress bar
    */
    private String htmlStatusBar(String progressAsString) {
        int progress = new Integer(progressAsString);
        int maxWidth = 800;

        int relativeProgress = (int)((progress/100.0 ) * maxWidth);

        String html = "<html>\n" +
            "<head>\n" +
            "<title>blah</title>\n" +
            "<link href=\"https://api.eu-cloudflow.eu/portal/twopointo/styles/style.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
            "</head>\n" +
            "<body style=\"margin: 20px; padding: 20px;\">\n" +
            "<h1>Doing automatic registration</h1>\n" +
            "<div style=\"border-radius: 5px; border-color: lightblueblue; border-style:dashed; width: " + maxWidth + "px; height: 80px;padding:0; margin: 0; border-width: 3px;\">\n" +
            "<div style=\"position: relative; top: -3px; left: -3px; border-radius: 5px; border-color: lightblue; border-style:solid; width: " + relativeProgress + "px; height: 80px;padding:0; margin: 0; border-width: 3px; background-color: lightblue;\">\n" +
            "<h1 style=\"margin-left: 20px;\" >" + progress + "%</h1>\n" +
            "</div>\n" +
            "</div>\n" +
            "</head>\n" +
            "</body>";
        
        return html;
    }
    
    
    
    /*
    *  Utility function for less verbose logging
    */
    private void log(String message) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, message);
    }
    
    /*
    *  Utility function for less verbose error message in log
    */
    private void error(String message) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, message);
    }
     
    /*
    *  Utility function for less verbose error message in log
    */
    private void error(IOException ex) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
    }
}
