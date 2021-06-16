/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Samples;

import com.fazecast.jSerialComm.SerialPort;
import com.thingmagic.ECTConstants;
import com.thingmagic.ElaraJSONParser;
import com.thingmagic.ElaraTransportListener;
import com.thingmagic.MainController;

/**
 *
 * @author ben lepsch
 */
public class MaxPowerRead {
    public static SerialPort reader;
    
    public static void main(String[] args) throws InterruptedException {
        try {
            // get list of all devices connected thru serial
            SerialPort[] ports = SerialPort.getCommPorts();
            String[] results = new String[ports.length];
            
            // log info for each
            for (int i = 0; i < ports.length; i++) {
                results[i] = ports[i].getSystemPortName();
                System.out.println("Com port: " + results[i]);
                System.out.println("Descriptive Port Name: " + ports[i].getDescriptivePortName());
                System.out.println("Device Name: " + ports[i].getPortDescription());
            }
            
            // try connecting to the first one on the list @ baud rate 115200
            reader = SerialPort.getCommPorts()[0];
            reader.setComPortParameters(115200, 8, 1, 0);
            if (reader.openPort()) {
                System.out.println("Port is open :)");
            } else {
                System.out.println("Couldn't open port :(");
                return;
            }
            
            MainController msg = new MainController();
            ElaraTransportListener elaraTransportListener = null;
            ElaraJSONParser ejsonp = new ElaraJSONParser();
            
            // stop read to make sure reader isn't reading 
            String stop = ejsonp.formJSONCommand(ECTConstants.STOP_RZ);
            System.out.println("Command: " + stop);
            String response = (String) msg.sendMessage(reader, stop, elaraTransportListener);
            System.out.println("Response: " + response);
            
            // disable kbwedge, whatever that is
            String Kbdisable = ejsonp.formJSONCommand(ECTConstants.SET_CFG_USBKBDISABLE);
            System.out.println("Command: " + Kbdisable);
            String res = (String) msg.sendMessage(reader, Kbdisable, elaraTransportListener);
            System.out.println("Response: " + res);

            // debug reader info
            String info = ejsonp.formJSONCommand(ECTConstants.GET_INFO_FIELDS_ALL);
            System.out.println("Command: " + info);
            String rec = (String) msg.sendMessage(reader, info, elaraTransportListener);
            System.out.println("Response: " + rec);
            
            String Workflow = ejsonp.formJSONCommand(ECTConstants.SET_CFG_MODE_MONITOR);
            System.out.println("Command: " + Workflow);
            String response1 = (String) msg.sendMessage(reader, Workflow, elaraTransportListener);
            System.out.println("Response: " + response1);
            
            // set power to max, 27dBm
            String setPower = ejsonp.customJSONCommand(ECTConstants.SET_RZ_READ_POWER, new StringBuffer("27"));
            System.out.println("Command: " + setPower);
            String response2 = (String) msg.sendMessage(reader, setPower, elaraTransportListener);
            System.out.println("Response: " + response2);
            
            // start tag reading
            String startRead = ejsonp.formJSONCommand(ECTConstants.START_RZ);
            System.out.println("Command: " + startRead);
            String response3 = (String) msg.sendMessage(reader, startRead, elaraTransportListener);
            System.out.println("Response: " + response3);
            Thread.sleep(5000);
            String data = (String) msg.receiveMessage(reader.getInputStream(), elaraTransportListener);
            System.out.println("TagReports: " + data);
            
            // stop reading
            String stopRead = ejsonp.formJSONCommand(ECTConstants.STOP_RZ);
            System.out.println("Command: " + stopRead);
            String response4 = (String) msg.sendMessage(reader, stopRead, elaraTransportListener);
            System.out.println("Response: " + response4);
            
            reader.closePort();
        } 
        catch (InterruptedException ex) {
            System.out.println("Exception: " + ex);
        }
    }
}