# Sample Maven / Java project following along with the LinkedInLearning course [Learning JDBC](https://www.linkedin.com/learning/learning-jdbc-24697410)

The idea behind this repository is to have a good base project to look back on whenever I need to recall JDBC knowledge.

## Setup
### Project structure
The project's base structure was generated by the following Maven command:
```
mvn archetype:generate "-DgroupId=com.pedroghiotti.learning" "-DartifactId=learning-jdbc" "-DarchetypeArtifactId=maven-archetype-quickstart" "-DarchetypeVersion=1.5" "-DinteractiveMode=false"
```

### DB Setup
The project's database, for development purposes, runs on a docker container in my local machine.
The commands utilized in setting up said container were the following:
```
docker pull postgres

docker run --name "learning-jdbc-pg" -e POSTGRES_PASSWORD=pgpw -d -p 5432:5432 -v ./pgdata:/var/lib/postgresql/data postgres
```