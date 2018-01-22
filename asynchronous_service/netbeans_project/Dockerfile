FROM oracle/glassfish:4.1.2

COPY target/cloudflow_training-1.0-SNAPSHOT.war /cfg-training.war
COPY docker-entrypoint.sh /entrypoint.sh
COPY scripts/* /usr/local/bin/

EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]

CMD ["asadmin", "start-domain", "-v"]
