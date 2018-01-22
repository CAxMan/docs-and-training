package no.sintef.cloudflow_training;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.ws.Holder;

/**
 *
 * @author havahol
 */
@WebService(serviceName = "Calculator")
public class Calculator {

    
    // The namespace should match the package name in line 1. 
    // If package name is a.b.c, the namespace should be "http://c.b.a/" (casae sensitive)
    // WFM will have an easier time recognizing your web service if this is fulfilled
    private final String namespace = "http://cloudflow_training.sintef.no/";
    
    // Very simple example method: take two numbers and immediately return
    // their sum.
    // This method is _synchronous_, meaning that it immediately returns its
    // result to the caller.
    @WebMethod(operationName = "add")
    public void add(
            @WebParam(name = "numberOne", 
                    targetNamespace = namespace, 
                    mode = WebParam.Mode.IN) Float numberOne,
            @WebParam(name = "numberTwo", 
                    targetNamespace = namespace, 
                    mode = WebParam.Mode.IN) Float numberTwo,
            @WebParam(name = "sum", 
                    targetNamespace = namespace, 
                    mode = WebParam.Mode.OUT) Holder<Float> sum
    ) {
        log("sync_example.add - started with input:" + 
                "\n\tnumberOne =" + numberOne + 
                "\n\tnumberTwo =" + numberTwo);
        
        sum.value = numberOne + numberTwo;
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
