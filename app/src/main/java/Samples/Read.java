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
    private boolean hasElaraListener = false;

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

    // receive tag reports from reader
    public String receiveMessage(InputStream in, ElaraTransportListener etl) {
        String result = null;

        try {
            int numBytes = 0;
            numBytes = in.available();
            byte[] readBuffer = new byte[numBytes];
            long startTime = System.currentTimeMillis();
            while (in.available() > 0) {
                long currentTime = System.currentTimeMillis();
                in.read(readBuffer);
                if ((currentTime - startTime) > 5000) {
                    break;
                }
            }

            result = new String(readBuffer);
            if (hasElaraListener) {
                etl.message(false, result);
            }
        }

        return result;
    }

    // receive cmd feedback from reader
    public String receiveCMDMessage(String message, InputStream in, ElaraTransportListener etl) {
        String result = null;
        
        try {
            int numBytes = 0;
            boolean streamAvailable = false;
            JSONObject messageJSON = null;
            JSONObject resultJSON = null;

            if (message != "" && message != null && message.length() != 0) {
                messageJSON = new JSONObject(message);
            }

            long startTime = System.currentTimeMillis();
            StringBuffer sbin = new StringBuffer();

            while (!streamAvailable) {
                numBytes = in.available();
                byte[] readBuffer = new byte[numBytes];
                in.read(readBuffer);
                String inputResult = new String(readBuffer);
                sbin.append(inputResult);

                if (isValidJSON(sbin.toString())) {
                    result = sbin.toString();
                    resultJSON = new JSONObject(result);
                    if (resultJSON.get("Report").equals("HB")) {
                        streamAvailable = true;
                    } else {
                        if (resultJSON.toString().contains("ErrID"))
                        {
                            if (resultJSON.get("ErrID").equals(0)) {
                               streamAvailable = true;
                               break;
                            }
                            else if (!resultJSON.get("ErrID").equals(0)) {
                                break;
                            }
                        }
                    }
                }

                long currentTime = System.currentTimeMillis();
                if ((currentTime - startTime) > 5000) {
                    break;
                }

                sbin.delete(0, sbin.length());

                if (hasElaraListener) {
                    etl.message(false, result);
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {

    }
}