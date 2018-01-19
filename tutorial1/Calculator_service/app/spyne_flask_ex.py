from flask import Flask
from flask_spyne import Spyne
from spyne.protocol.soap import Soap11
from spyne.model.primitive import Unicode, Integer
from spyne.model.complex import Iterable

app = Flask(__name__)
spyne = Spyne(app)

@app.route("/sintefcalc")
def hello():
    return "Hello World"

class SomeSoapService(spyne.Service):
    __service_url_path__ = '/sintefcalc/addition'
    __in_protocol__ = Soap11(validator='lxml')
    __out_protocol__ = Soap11()

    @spyne.srpc(Unicode, Integer, _returns=Iterable(Unicode))
    def echo(str, cnt):
        for i in range(cnt):
            yield str

if __name__ == '__main__':
    app.run(host = '127.0.0.1')
