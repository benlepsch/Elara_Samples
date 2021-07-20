# Elara Sample Code for Gradle - Smaller Version

Hi

This is the same sample code that came with the Elara RFID reader, restructured to work with gradle instead of netbeans so that it compiles and runs from a linux terminal.

Smaller version -- most of the source files for this to work were huge and unnecessary because it was intended to be run as a GUI, but on a raspberry pi I only needed a terminal interface. I deleted most of the background files and copied out the important bits to try and reduce size of the whole repo (8.6k line file > 200 line file).

## Installation

To download the code, use the command `git clone https://github.com/benlepsch/elara_samples`. This downloads the windows/netbeans version, so then type `cd elara_samples` followed by `git checkout bones` to switch to this branch. Also ensure that you have java installed: This runs on OpenJDK version 11.0.11 (check with the `java -version` command), and isn't tested with any newer versions. (To install java, use the command `apt-get install openjdk-11-jdk`)

### Required Packages

All of the .jar files needed are already in the git repo's `libs/` folder, ~~minus the JavaFX files.~~ Including the JFX files! No extra installation necessary, every dependency is included.

## Running the Program

### Editing/Creating java files

The java files that run the reader are stored in the folder `app/src/main/java/Samples/`. To create a new file, just place it in the folder and edit it with a text editor or IDE.

### Running code on the raspberry pi with custom reader command

If you're running the code through the raspberry pi I set up, all you have to do is make sure you're in the `~/Elara_Samples/` folder and then type `reader <NameOfProgram>`. For example, running the `ConstantRead.java` file would use the command `reader ConstantRead`. Running `reader help` explains how to use the command in slightly more detail.

### Setting up reader command on different linux machine

If you're not running the code on the raspberry pi but you're still on linux and you want to set up the reader command, the first step to do is rename the `reader_src` file to `reader` (on linux, open a terminal and type `mv reader_src reader`).

Next, there are two lines in the `reader` file that need to be updated. On line 4, edit the `CODE_DIR` variable to point to the directory this git repo is cloned to. Then, on line 5, edit the `JAVAFX_DIR` variable to point to the folder with your javafx files.

Once this is done, the next step is to move the `reader` file to the `/usr/bin/` directory to allow running it directly from the command line. (`mv reader /usr/bin/`) 

Finally, restart your terminal window to make sure the changes have taken effect.


### Running the code on another linux machine with normal gradle compilation

If you're not running the code on the raspberry pi and you haven't set up the reader command, here is how you compile java code normally through gradle.

To change the file that gradle runs, edit the `application` block in the file `app/build.gradle` to point `mainClass` to your program.

To compile and run the code: type `./gradlew run`

**It is important to note that running the code the normal gradle way won't stop the reader if you interrupt the code mid read with Control C, so if you want to be sure the reader is stopped and you aren't using my raspberry pi, you have to run the StopRead.java file after stopping the code.** If you let the code finish normally without interruption, however, it will stop the reader.
