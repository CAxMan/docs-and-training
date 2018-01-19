#!/usr/bin/env python
# encoding: utf8


from spyne.application import Application
from spyne.decorator import srpc
from spyne.service import ServiceBase

from spyne.model.complex import Iterable
from spyne.model.primitive import UnsignedInteger
from spyne.model.primitive import String
from spyne.model.complex import ComplexModel
from spyne.util.wsgi_wrapper import WsgiMounter


from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication

class HPCCommand(ComplexModel):
    CommandLine = String
    MaxRuntime  = UnsignedInteger

class HelloWorldService(ServiceBase):
    @srpc(String, UnsignedInteger, UnsignedInteger, _returns=HPCCommand)
    def say_hello(name, times, myNum):
        """Docstrings for service methods appear as documentation in the wsdl.
        <b>What fun!</b>

        @param name the name to say hello to
        @param times the number of times to say hello
        @return the completed array
        """
        message = name + " " + str(myNum)
        p = HPCCommand() 
        p.CommandLine = "yo"
        p.MaxRuntime = 2
        print(p)
        return p


application = Application([HelloWorldService], 'spyne.examples.hello.soap',
                          in_protocol=Soap11(validator='lxml'),
                          out_protocol=Soap11())

wsgi_application = WsgiApplication(application)


if __name__ == '__main__':
    import logging

    from wsgiref.simple_server import make_server

    logging.basicConfig(level=logging.DEBUG)
    logging.getLogger('spyne.protocol.xml').setLevel(logging.DEBUG)

    logging.info("listening to http://127.0.0.1:8000")
    logging.info("wsdl is at: http://localhost:8000/?wsdl")

    server = make_server('127.0.0.1', 8000, wsgi_application)
    server.serve_forever()
