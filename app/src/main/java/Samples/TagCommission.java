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
import org.json.JSONException;

/**
 *
 * @author pchinnapapannagari
 */
public class TagCommission {
    public static SerialPort reader;
    public static void main(String[] args)   {
        try{
        SerialPort[] ports = SerialPort.getCommPorts();
        String[] results = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            results[i] = ports[i].getSystemPortName();
            System.out.println("Com port: " + results[i]);
            System.out.println("DescriptivePortName: " +ports[i].getDescriptivePortName());
            System.out.println("PortDescription : " +ports[i].getPortDescription());
        }
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
        
        //Stop the read
        String Stopread = ejsonp.formJSONCommand(ECTConstants.STOP_RZ);
        System.out.println("Command : " +Stopread);
        String rec=(String) msg.sendMessage(reader, Stopread, elaraTransportListener);
        System.out.println("Response : " +rec);
        
        //Disable Kbwedge
        String Kbdisable = ejsonp.formJSONCommand(ECTConstants.SET_CFG_USBKBDISABLE);
        System.out.println("Command : " +Kbdisable);
        String res=(String) msg.sendMessage(reader, Kbdisable, elaraTransportListener);
        System.out.println("Response : " +res); 
        
        //Set TagCommission Workflow
        String Tagcommission = ejsonp.formJSONCommand(ECTConstants.SET_CFG_MODE_TAGCOMMISSION);
        System.out.println("Command : " +Tagcommission);
        String tagcomres=(String) msg.sendMessage(reader, Tagcommission, elaraTransportListener);
        System.out.println("Response : " +tagcomres);
        
        //Writedata to single tag
        String writeProfileSendCmd = ejsonp.setProfileWriteData("EPC", 2, 1, 0, "1122", false, false);
        System.out.println("Command:"+writeProfileSendCmd);
        String response =(String) msg.sendMessage(reader, writeProfileSendCmd, elaraTransportListener);
        System.out.println("Response:"+response);
        
        //This tag
        String execwrite = ejsonp.formJSONCommand(ECTConstants.THIS_TAG_PROF);
        System.out.println("Command : " +execwrite);
        String writeres=(String) msg.sendMessage(reader, execwrite, elaraTransportListener);
        System.out.println("Response : " +writeres);
         if(ejsonp.isCommandSuccess(writeres))
                        {
                            System.out.println("Write data is Successful");
           
                        }
                        else
                        {
                            String errorMessage = ejsonp.errorInfo(writeres);
                            System.out.println("Error:"+errorMessage);
                        }

        //Start read 
        String Startreads = ejsonp.formJSONCommand(ECTConstants.START_RZ);
        System.out.println("Command : " +Startreads);
        String Startresponse=(String) msg.sendMessage(reader, Startreads, elaraTransportListener);
        System.out.println("Response : " +Startresponse);
        Thread.sleep(5000);
        String data=(String) msg.receiveMessage(reader.getInputStream(), elaraTransportListener);
        System.out.println("TagReports : " +data);
        
        //Stop read
        String Stopreads = ejsonp.formJSONCommand(ECTConstants.STOP_RZ);
        System.out.println("Command : " +Stopreads);
        String response3=(String) msg.sendMessage(reader, Stopreads, elaraTransportListener);
        System.out.println("Response : " +response3);
        reader.closePort();
        }catch(InterruptedException | JSONException ex){
        
        System.out.println("Exception:"+ ex);
    }   
}
}
