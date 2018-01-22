FROM oracle/glassfish:4.1.2

COPY target/cloudflow_training-1.0-SNAPSHOT.war /cfgtraining-syncservice.war
COPY docker-entrypoint.sh /entrypoint.sh
RUN chmod 755 /entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]

CMD ["asadmin", "start-domain", "-v"]
