from suds.client import Client

#c=Client("http://127.0.0.1:8000/sintefcalc/addition/?wsdl")
c=Client("http://localhost:5000/sintefcalc/addition/?wsdl")
#print c.service.echo("y0",3)

resp=c.service.addNumbers(2, 5)
print ("2+5=")
print (resp)
