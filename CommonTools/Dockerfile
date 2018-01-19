FROM ubuntu:latest
MAINTAINER SINTEF
RUN apt-get update -y
RUN apt-get install -y python-pip python-dev build-essential
RUN apt-get install -y curl vim
RUN pip install --upgrade pip
COPY requirements.txt /tmp
RUN pip install -r /tmp/requirements.txt
RUN rm /tmp/requirements.txt

WORKDIR /app
ENTRYPOINT ["python"]
