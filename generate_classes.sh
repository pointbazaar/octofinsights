#https://stackoverflow.com/questions/43574426/how-to-resolve-java-lang-noclassdeffounderror-javax-xml-bind-jaxbexception-in-j

java  -classpath jooq-3.11.11.jar:\
jooq-meta-3.11.11.jar:\
jooq-codegen-3.11.11.jar:\
mysql-connector-java-8.0.16.jar:\
jakarta.xml.bind-api-2.3.2:\
jaxb-runtime-2.3.2.jar:\
jaxb-api-2.2.12.jar:\
jaxb-impl-2.3.0.jar:\
activation-1.1.1.jar:\
jaxb-core-2.2.11.jar:.  org.jooq.codegen.GenerationTool library.xml
