#!/usr/bin/env python
# encoding: utf8
#


from spyne.application import Application
from spyne.decorator import srpc
from spyne.service import ServiceBase

from spyne.model.complex import Iterable
from spyne.model.primitive import UnsignedInteger
from spyne.model.primitive import String
from spyne.model.complex import ComplexModel


from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication
from spyne.util.wsgi_wrapper import WsgiMounter

class CalculatorService(ServiceBase):
    @srpc(UnsignedInteger, UnsignedInteger, _returns=UnsignedInteger)
    def AddNumbers(a, b):
        """Adds two integers
        <b>Note, unsigned Ints!</b>

        @param First number
        @param Second number
        @return Sum
        """
        print(a+b)
        return (a+b)


application = Application([CalculatorService], 'spyne.examples.hello.soap',
                          in_protocol=Soap11(validator='lxml'),
                          out_protocol=Soap11())

#wsgi_application = WsgiApplication(application)

wsgi_application = WsgiMounter({
    'Calculator': application,
})

if __name__ == '__main__':
    import logging

    from wsgiref.simple_server import make_server

    logging.basicConfig(level=logging.DEBUG)
    logging.getLogger('spyne.protocol.xml').setLevel(logging.DEBUG)

    logging.info("listening to http://127.0.0.1:8000")
    logging.info("wsdl is at: http://localhost:8000/Calculator/?wsdl")

    server = make_server('127.0.0.1', 8000, wsgi_application)
    server.serve_forever()
