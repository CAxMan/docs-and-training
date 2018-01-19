FROM ubuntu:latest
MAINTAINER me
RUN apt-get update -y
RUN apt-get install -y python-pip python-dev build-essential
RUN apt-get install -y curl vim
RUN pip install --upgrade pip
COPY requirements.txt /tmp
RUN pip install -r /tmp/requirements.txt
RUN rm /tmp/requirements.txt

RUN apt-get install -y nginx
#COPY spyne_flask.nginx /etc/nginx/sites-available
#RUN ln -s  /etc/nginx/sites-available/spyne_flask.nginx /etc/nginx/sites-enabled

RUN mkdir /app
COPY app/ /app


EXPOSE 8000

WORKDIR /app
#ENTRYPOINT ["python"]
#CMD ["CalculatorService.py"]
ENTRYPOINT ["bash"]