# ab_testing 

Language independent ab_testing framework which allows users to create, modify and get experiments and states.

# Motivation 
While deploying various features, and assessing the value of a feature (such as in recommendation systems), it is important that people run many experiments and choose what is best according to their own defined metrics. But since it is an experimentation, it is unwise to experiment it on all users and therefore, with this framework you can tune the % of users affected by your experiment. It also supports multiple languages like Java, Python, Perl, C++ etc,.

# Technologies Used
1. Java (with Maven as a build tool)
2. Redis as a primary data store
3. Apache Thrift for language independent RPC invocation

# Setup
1. Install Java Runtime, Java SDK, Eclipse, Eclipse-Maven plugin, Redis
2. Clone the repository
3. mvn package
4. Run the jar file that got created by => java -jar ab_testing-0.0.1-SNAPSHOT.jar ab_test.properties
