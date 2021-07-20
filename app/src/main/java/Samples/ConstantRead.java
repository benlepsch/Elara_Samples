package Samples;

/**
 * Ben Lepsch
 * barebones reading program
 * trying to condense everything from MainController into this file
 */

import com.fazecast.jSerialComm.SerialPort;
import org.json.JSONObject;
import org.json.JSONException;
import com.thingmagic.ECTConstants;
import com.thingmagic.ElaraJSONParser;
import com.thingmagic.ElaraTransportListener;
import java.io.InputStream;
import java.io.OutputStream;

public class ConstantRead {
    public static SerialPort reader;

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

            // Get reader info
            String info = ejsonp.formJSONCommand(ECTConstants.GET_INFO_FIELDS_ALL);
            System.out.println("Command: " +info);
            String rec = (String) msg.sendMessage(reader, info, elaraTransportListener);
            System.out.println("Response: " +rec);
           
            // Set WorkFlow
            String Workflow = ejsonp.formJSONCommand(ECTConstants.SET_CFG_MODE_MONITOR);
            System.out.println("Command: " + Workflow);
            String response1 = (String) msg.sendMessage(reader, Workflow, elaraTransportListener);
            System.out.println("Response: " + response1);
            
            // Start BulkTagRead
            String Startread = ejsonp.formJSONCommand(ECTConstants.START_RZ);
            System.out.println("Command: " + Startread);
            String response2 = (String) msg.sendMessage(reader, Startread, elaraTransportListener);
            System.out.println("Response: " + response2);
            // Thread.sleep(5000);
            // String data=(String) msg.receiveMessage(reader.getInputStream(), elaraTransportListener);
            // System.out.println("TagReports : " +data);

            System.out.println("Tag Reports: ");
            int count = 0;
            do {
                Thread.sleep(1000);
                String data = (String) msg.receiveMessage(reader.getInputStream(), elaraTransportListener);
                if (!data.equals(""))
                    System.out.println(data);
                count ++;
            } while (count < 400);
            
            // Stop reading
            String Stopread = ejsonp.formJSONCommand(ECTConstants.STOP_RZ);
            System.out.println("Command: " + Stopread);
            String response3 = (String) msg.sendMessage(reader, Stopread, elaraTransportListener);
            System.out.println("Response: " + response3);

            reader.closePort();
        } catch (Exception e) {
            System.out.println("exeltpon in sendMessage: " + e);
        }
    }
}