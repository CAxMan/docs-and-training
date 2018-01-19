from suds.client import Client

c=Client("http://localhost:8000/Calculator?wsdl")
resp=c.service.AddNumbers(2, 5)
print ("2+5=")
print (resp)
