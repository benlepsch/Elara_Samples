package Samples;

/**
 * Ben Lepsch
 * barebones reading program
 * trying to condense everything from MainController into this file
 */

import com.fazecast.jSerialComm.SerialPort;
import com.thingmagic.ECTConstants;
import com.thingmagic.ElaraJSONParser;
import com.thingmagic.ElaraTransportListener;
import java.io.InputStream;
import java.io.OutputStream;

public class Read {
    // send JSON message to the reader
    public String sendMessage(SerialPort sp, String message, ElaraTransportListener etl) {
        String response = null;
        try {
            InputStream in = sp.getInputStream();
            OutputStream out = sp.getOutputStream();
            StringBuilder data = new StringBuilder();
            data.append(message);

            if (!message.conatins("Passthrough")) {
                data.append("\n");
            }

            if (hasElaraListener) {
                etl.message(true, message);
            }

            if (message.contains("GetInfo")) {
                while (in.available() > 0) {
                    in.read();
                }
            }

            out.write(data.toString().getBytes());

            if (!(message.contains("ActivateUpdate") || message.contains("EndUpdate") || message.contains("Passthrough"))) {
                response = receiveCMDMessage(message, in, etl);
            }
        }
        return response;
    }

    public String receiveMessage() {

    }

    public String receiveCMDMessage() {

    }

    public static void main(String[] args) {

    }
}