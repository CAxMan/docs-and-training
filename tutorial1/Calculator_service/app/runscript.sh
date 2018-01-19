#!/bin/bash

if [[ -v REMOTE_HOST ]]
then
    cat spyne_flask.nginx_template | sed "s|REMOTE_HOST|$REMOTE_HOST|" > /etc/nginx/sites-enabled/spyne_flask.nginx
    echo yo
fi
    
/etc/init.d/nginx start
python spyne_flask_ex.py

