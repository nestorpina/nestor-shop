Nestor-shop Demo Application

## Example application for use with App Engine Java, objetify and Jersey JAX-RS, using igz-code-gae libraries 

Requires [Apache Maven](http://maven.apache.org) 3.0 or greater, and JDK 6+ in order to run.

To build, run

    mvn package

Building will run the tests, but to explicitly run tests you can use the test target

    mvn test

To start the app, use the [Maven GAE Plugin](https://github.com/maven-gae-plugin/maven-gae-plugin) that is already included in this demo.  Just run the command.

    mvn gae:run
    
By default the local server will listen in port 8080, to access aplication enter the following url in your browser : 

    http://localhost:8080

To access de gae administration console enter

    http://localhost:8080/_ah/admin 

To stop the running local instance, execute

    mvn gae:stop
    
 
