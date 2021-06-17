# Elara Sample Code for Gradle

Hi

This is the same sample code that came with the Elara RFID reader, restructured to work with gradle instead of netbeans so that I can compile and run it from a linux terminal.

To change the file you're running: edit the `application` block in the `app/build.gradle` file to contain `mainClass = 'Samples.yourProgramHere'`.

To compile and run the code: type `./gradlew run`

This is being run with OpenJDK version 11.0.11, and also requires javafx to be imported. My JFX files are in `/usr/share/java/`, if yours are somewhere different just change the `dirs` line in `app/build.gradle` to point to the correct folder.
