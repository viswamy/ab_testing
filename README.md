# ab_testing 

Language independent ab_testing framework which allows users to create, modify and get experiments and states.

# Technologies Used
1. Java (with Maven as a build tool)
2. Redis as a primary data store
3. Apache Thrift for language independent RPC invocation

# Setup
1. Install Java Runtime, Java SDK, Eclipse, Eclipse-Maven plugin, Redis
2. Clone the repository
3. mvn package
4. Run the jar file that got created by => java -jar ab_testing-0.0.1-SNAPSHOT.jar ab_test.properties
