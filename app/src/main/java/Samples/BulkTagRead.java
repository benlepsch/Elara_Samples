/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Samples;
/*
 * Sample program that performs BulkTagRead in CDC mode
 */


import com.fazecast.jSerialComm.SerialPort;
import com.thingmagic.ECTConstants;
import com.thingmagic.ElaraJSONParser;
import com.thingmagic.ElaraTransportListener;
import com.thingmagic.MainController;



/**
 *
 * @author pchinnapapannagari
 */
public class BulkTagRead {
     public static SerialPort reader;
    public static void main(String[] args) throws InterruptedException {
      try
      {
        SerialPort[] ports = SerialPort.getCommPorts();
            String[] results = new String[ports.length];
            for (int i = 0; i < ports.length; i++) {
                results[i] = ports[i].getSystemPortName();
                System.out.println("Com port: " + results[i]);
                System.out.println("DescriptivePortName: " +ports[i].getDescriptivePortName());
                System.out.println("Device Name : " +ports[i].getPortDescription());
            }
            //Connect to the reader
            reader = SerialPort.getCommPorts()[0];
            reader.setComPortParameters(115200, 8, 1, 0);
            if (reader.openPort()) {
                System.out.println("Port is open :)");
            } else {
                System.out.println("Failed to open port :(");
                return;                                
            }
            
            MainController msg=new MainController();
            ElaraTransportListener elaraTransportListener=null;
            ElaraJSONParser ejsonp = new ElaraJSONParser();
           
            //Stop the read to ensure that module is free from reading the tags
            String stop = ejsonp.formJSONCommand(ECTConstants.STOP_RZ);
            System.out.println("Command : " +stop);
            String response=(String) msg.sendMessage(reader, stop, elaraTransportListener);
            System.out.println("Response : " +response); 
            
            //Disable Kbwedge
            String Kbdisable = ejsonp.formJSONCommand(ECTConstants.SET_CFG_USBKBDISABLE);
            System.out.println("Command : " +Kbdisable);
            String res=(String) msg.sendMessage(reader, Kbdisable, elaraTransportListener);
            System.out.println("Response : " +res); 
            
            //Get Info
            String info = ejsonp.formJSONCommand(ECTConstants.GET_INFO_FIELDS_ALL);
            System.out.println("Command : " +info);
            String rec=(String) msg.sendMessage(reader, info, elaraTransportListener);
            System.out.println("Response : " +rec);
           
            //Set WorkFlow
            String Workflow = ejsonp.formJSONCommand(ECTConstants.SET_CFG_MODE_MONITOR);
            System.out.println("Command : " +Workflow);
            String response1=(String) msg.sendMessage(reader, Workflow, elaraTransportListener);
            System.out.println("Response : " +response1);
            
            //Start BulkTagRead
            String Startread = ejsonp.formJSONCommand(ECTConstants.START_RZ);
            System.out.println("Command : " +Startread);
            String response2=(String) msg.sendMessage(reader, Startread, elaraTransportListener);
            System.out.println("Response : " +response2);
            Thread.sleep(5000);
            String data=(String) msg.receiveMessage(reader.getInputStream(), elaraTransportListener);
            System.out.println("TagReports : " +data);
            
            //Start StopRead
            String Stopread = ejsonp.formJSONCommand(ECTConstants.STOP_RZ);
            System.out.println("Command : " +Stopread);
            String response3=(String) msg.sendMessage(reader, Stopread, elaraTransportListener);
            System.out.println("Response : " +response3);
            
            reader.closePort();
    
    }
      catch(InterruptedException ex){
        
        System.out.println("Exception:"+ ex);
    }
    }
}


