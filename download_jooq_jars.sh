#!/bin/bash

# These .jar 's are to support generating classes with a simple bash script.
# The benefit is a shorter build. Otherwise JOOQ would have to connect to the db
# with every build and generate the sources every time.
# I have not yet found a better way

wget https://repo1.maven.org/maven2/javax/xml/bind/jaxb-api/2.3.1/jaxb-api-2.3.1.jar
wget https://repo1.maven.org/maven2/org/glassfish/jaxb/jaxb-core/2.3.0/jaxb-core-2.3.0.jar
wget https://repo1.maven.org/maven2/com/sun/xml/bind/jaxb-impl/2.3.2/jaxb-impl-2.3.2.jar

wget https://repo1.maven.org/maven2/org/jooq/jooq/3.11.11/jooq-3.11.11.jar
wget https://repo1.maven.org/maven2/org/jooq/jooq-codegen/3.11.11/jooq-codegen-3.11.11.jar
wget https://repo1.maven.org/maven2/org/jooq/jooq-meta/3.11.11/jooq-meta-3.11.11.jar

wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.16/mysql-connector-java-5.1.18.jar

wget https://repo1.maven.org/maven2/javax/activation/activation/1.1.1/activation-1.1.1.jar

wget https://search.maven.org/remotecontent?filepath=org/reactivestreams/reactive-streams/1.0.3/reactive-streams-1.0.3.jar
mv remotecontent\?filepath\=org%2Freactivestreams%2Freactive-streams%2F1.0.3%2Freactive-streams-1.0.3.jar reactive-streams-1.0.3.jar

wget https://repo1.maven.org/maven2/jakarta/xml/bind/jakarta.xml.bind-api/2.3.2/jakarta.xml.bind-api-2.3.2.jar

wget https://repo1.maven.org/maven2/org/glassfish/jaxb/jaxb-runtime/2.3.2/jaxb-runtime-2.3.2.jar

