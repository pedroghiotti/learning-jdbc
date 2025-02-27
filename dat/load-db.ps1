docker cp ./load-db.sh learning-jdbc-pg:/
docker cp ./data.sql learning-jdbc-pg:/
docker exec learning-jdbc-pg bash load-db.sh