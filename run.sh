export MAVEN_OPTS=" -XX:+TieredCompilation -XX:TieredStopAtLevel=1"
mvn install -T 1C && mvn exec:java


