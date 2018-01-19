from werkzeug.contrib.fixers import ProxyFix

from flask import Flask
from flask import request
from flask_spyne import Spyne
from spyne.protocol.soap import Soap11
from spyne.model.primitive import Unicode, Integer
from spyne.model.complex import Iterable

app = Flask(__name__)
spyne = Spyne(app)
app.wsgi_app = ProxyFix(app.wsgi_app)


@app.route("/sintefcalc")
def hello():
    print (request.headers)
    return "Hello World http://yea"

class SomeSoapService(spyne.Service):
    __service_url_path__ = '/sintefcalc/addition'
    __in_protocol__ = Soap11(validator='lxml')
    __out_protocol__ = Soap11()

    @spyne.srpc(Integer, Integer, _returns=Integer)
    def addNumbers(a, b):
        return (a+b)


if __name__ == '__main__':
    app.run(host = '127.0.0.1')
