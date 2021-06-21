package Samples;

/*
    Stops the reader
*/

import com.fazecast.jSerialComm.SerialPort;
import com.thingmagic.ECTConstants;
import com.thingmagic.ElaraJSONParser;
import com.thingmagic.ElaraTransportListener;
import com.thingmagic.MainController;

/**
 * @author benlepsch
 */

public class StopRead {
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

            String stop = ejsonp.formJSONCommand(ECTConstants.STOP_RZ);
            System.out.println("Command: " + stop);
            String response = (String) msg.sendMessage(reader, stop, elaraTransportListener);
            System.out.println("Response: " + response);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
}