# Elara Sample Code for Gradle

Hi

This is the same sample code that came with the Elara RFID reader, restructured to work with gradle instead of netbeans so that it compiles and runs from a linux terminal.

## Installation

To download the code, use the command `git clone https://github.com/benlepsch/elara_samples`. This downloads the windows/netbeans version, so then type `cd elara_samples` followed by `git checkout gradle` to switch to this branch. Also ensure that you have java installed: This runs on OpenJDK version 11.0.11 (check with the `java -version` command), and isn't tested with any newer versions. (To install java, use the command `apt-get install openjdk-11-jdk`)

### Required Packages

All of the .jar files needed are already in the git repo's `libs/` folder, minus the JavaFX files. JavaFX can be installed on linux with the command `apt-get install javafx`. On the raspberry pi i'm using, this installed the javafx files to `/usr/share/java/`. If your JavaFX installs somewhere different, change the `dirs` line in `app/build.gradle` to point to the correct JavaFX folder.

## Running the Program

### Editing/Creating java files

The java files that run the reader are stored in the folder `app/src/main/java/Samples/`. To create a new file, just place it in the folder and edit it with a text editor or IDE.

### Running code on the raspberry pi with custom reader command

If you're running the code through the raspberry pi I set up, all you have to do is make sure you're in the `~/Elara_Samples/` folder and then type `reader <NameOfProgram>`. For example, running the `ConstantRead.java` file would use the command `reader ConstantRead`.

### Running the code on another linux machine with normal gradle compilation

If you're not running the code on the raspberry pi that I put the above command on, here is how you compile java code normally through gradle.

To change the file that gradle runs, edit the `application` block in the file `app/build.gradle` to point `mainClass` to your program.

To compile and run the code: type `./gradlew run`

**It is important to note that running the code the normal gradle way won't stop the reader if you interrupt the code mid read with Control C, so if you want to be sure the reader is stopped and you aren't using my raspberry pi, you have to run the StopRead.java file after stopping the code.** If you let the code finish normally without interruption, however, it will stop the reader.
