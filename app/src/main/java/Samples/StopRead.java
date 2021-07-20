package Samples;

/**
 * Ben Lepsch
 * barebones reading program
 * trying to condense everything from MainController into this file
 */

import com.fazecast.jSerialComm.SerialPort;
import org.json.JSONObject;
import com.thingmagic.ECTConstants;
import com.thingmagic.ElaraJSONParser;
import com.thingmagic.ElaraTransportListener;
import java.io.InputStream;
import java.io.OutputStream;

public class StopRead {
    private boolean hasElaraListener = false;

    public static SerialPort reader;

    // send JSON message to the reader
    public String sendMessage(SerialPort sp, String message, ElaraTransportListener etl) {
        String response = null;
        try {
            InputStream in = sp.getInputStream();
            OutputStream out = sp.getOutputStream();
            StringBuilder data = new StringBuilder();
            data.append(message);

            if (!message.contains("Passthrough")) {
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
        catch (Exception e) {
            System.out.println("exeltpon in sendMessage: " + e);
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
        catch (Exception e) {
            System.out.println("exeltpon in sendMessage: " + e);
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
        catch (Exception e) {
            System.out.println("exeltpon in sendMessage: " + e);
        }

        return result;
    }

    public static void main(String[] args) {
        try {
            SerialPort[] ports = SerialPort.getCommPorts();
            String[] results = new String[ports.length];

            // print data about all available serial ports
            for (int i = 0; i < ports.length; i++) {
                results[i] = ports[i].getSystemPortName();
                System.out.println("Com port: " + results[i]);
                System.out.println("Descriptive Port Name: " + ports[i].getDescriptivePortName());
                System.out.println("Device name: " + ports[i].getPortDescription());
            }

            // connect the reader to the first port
            reader = SerialPort.getCommPorts()[0];
            reader.setComPortParameters(115200, 8, 1, 0);
            if (reader.openPort()) {
                System.out.println("Port is open");
            } else {
                System.out.println("Failed to open port");
                return;
            }

            StopRead msg = new StopRead();
            ElaraTransportListener elaraTransportListener = null;
            ElaraJSONParser ejsonp = new ElaraJSONParser();

            String stop = ejsonp.formJSONCommand(ECTConstants.STOP_RZ);
            System.out.println("Command: " + stop);
            String response = (String) msg.sendMessage(reader, stop, elaraTransportListener);
            System.out.println("Response: " + response); 
        }
        catch (Exception e) {
            System.out.println("exeltpon in sendMessage: " + e);
        }
    }
}