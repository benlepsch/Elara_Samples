package Samples;

/*
    continuously reads tags and prints data to CLI
*/

import com.fazecast.jSerialComm.SerialPort;
import com.thingmagic.ECTConstants;
import com.thingmagic.ElaraJSONParser;
import com.thingmagic.ElaraTransportListener;
import com.thingmagic.MainController;

/**
 * @author benlepsch
 */
public class ConstantRead {
    public static SerialPort reader;

    public static void main(String[] args) {
        try {
            // generate list of serial ports and select the first one
            SerialPort[] ports = SerialPort.getCommPorts();
            String[] results = new String[ports.length];
            for (int i = 0; i < ports.length; i++) {
                results[i] = ports[i].getSystemPortName();
                System.out.println("Com port: " + results[i]);
                System.out.println("Descriptive Port Name: " + ports[i].getDescriptivePortName());
                System.out.println("Device name: " + ports[i].getPortDescription());
            }

            // connect the reader to the port
            reader = SerialPort.getCommPorts()[0];
            reader.setComPortParameters(115200, 8, 1, 0);
            if (reader.openPort()) {
                System.out.println("Port is open");
            } else {
                System.out.println("Failed to open port");
                return;
            }

            MainController msg = new MainController();
            ElaraTransportListener elaraTransportListener = null;
            ElaraJSONParser ejsonp = new ElaraJSONParser();

            // Stop the read to ensure that module is free from reading the tags
            String stop = ejsonp.formJSONCommand(ECTConstants.STOP_RZ);
            System.out.println("Command: " + stop);
            String response = (String) msg.sendMessage(reader, stop, elaraTransportListener);
            System.out.println("Response: " + response); 

            // Disable Kbwedge
            String Kbdisable = ejsonp.formJSONCommand(ECTConstants.SET_CFG_USBKBDISABLE);
            System.out.println("Command: " + Kbdisable);
            String res = (String) msg.sendMessage(reader, Kbdisable, elaraTransportListener);
            System.out.println("Response: " + res); 
            
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
            } while (count < 10);
            
            // Stop reading
            String Stopread = ejsonp.formJSONCommand(ECTConstants.STOP_RZ);
            System.out.println("Command: " + Stopread);
            String response3 = (String) msg.sendMessage(reader, Stopread, elaraTransportListener);
            System.out.println("Response: " + response3);
            
            reader.closePort();

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
