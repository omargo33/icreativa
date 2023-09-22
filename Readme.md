#ICreativa

##ramv

##comprobantesSri


docker exec -t docker-db-1 pg_dumpall -c -U postgres > dump_ramv.sql



mvn sonar:sonar \
  -Dsonar.projectKey=ramv \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=8a3adf6d2c1c8322cd80b1c66f23e432512a4c1c