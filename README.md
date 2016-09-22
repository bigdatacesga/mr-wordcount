MapReduce WordCount Example for HDP 2.4.2
-----------------------------------------
This repo contains a sample wordcount application compiled against Hortonworks HPD 2.4.2.

Verify everything works using:

    mvn compile

Run the tests:

    mvn test

Generate a jar package:

    mvn package

The generated jar file will be located under the target directory

You can also generate a jar package skipping the tests phase:

    mvn package -Dmaven.test.skip

If using an IDE like Eclipse or Intellij it can be useful to have the accompanying JavaDoc and source code while developing:

    # Download sources and javadoc
    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

    # Attach them to the project
    mvn eclipse:eclipse
    # Or if you are using Intellij
    mvn idea:idea

