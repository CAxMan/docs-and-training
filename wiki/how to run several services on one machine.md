# How to run several services on one machine

<!---
Moved from wiki by havahol.
Prior to that: Robert Schittn edited this page on Feb 23, 2018 · 1 revision
-->

With every service being wrapped inside a Docker container and only a single port open on a VM, the question might arise how to run more than one service behind that single port.

One option is to pack all services into a single container. This works well if the individual services are small and in the same language, but gets difficult for anything beyond that.

The recommended way here is to set up a simple nginx server on the VM which acts as a reverse proxy and routes different URL paths to different ports on the VM.

Consider two containers running on the VM:
1. Container `waiter`, running on the VM port 8881, listening to the web route `<host>/sintef/docker_services/waiter/...`
2. Container `calculator`, running on the VM port 8882, listening to the web route `<host>/sintef/docker_services/calculator/...`

Further assume that port 8080 on the VM is open to the outside world.

In this case, create the following nginx configuration (usually under `/etc/nginx/sites-available/someconfig`, and linked into `/etc/nginx/sites-enabled/someconfig`):
```
server {
        listen 8080;
        listen [::]:8080;


        location /sintef/docker_services/waiter {
                proxy_set_header Host $host;
                proxy_pass http://127.0.0.1:8881;
        }
        location /sintef/docker_services/calculator {
                proxy_set_header Host $host;
                proxy_pass http://127.0.0.1:8882;
        }
}
```
This will redirect traffic to the two containers based on the request URL. Add further `location` directives to include more services.