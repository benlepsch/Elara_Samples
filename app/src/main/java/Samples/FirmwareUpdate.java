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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author pchinnapapannagari
 */
public class FirmwareUpdate {
    
    public static SerialPort reader;
    public static void main(String[] args) throws JSONException, InterruptedException {
        //Choose right application binary file path here
        String firmwareFile="elara_app-01.0D.00.4D.bin";
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
        
        //Stop read
        String Stopread = ejsonp.formJSONCommand(ECTConstants.STOP_RZ);
        System.out.println("Command : " +Stopread);
        String rec=(String) msg.sendMessage(reader, Stopread, elaraTransportListener);
        System.out.println("Response : " +rec);
        
        //Disable Kbwedge
        String Kbdisable = ejsonp.formJSONCommand(ECTConstants.SET_CFG_USBKBDISABLE);
        System.out.println("Command : " +Kbdisable);
        String res=(String) msg.sendMessage(reader, Kbdisable, elaraTransportListener);
        System.out.println("Response : " +res); 
        Thread.sleep(2000);
        String info = ejsonp.formJSONCommand(ECTConstants.GET_INFO_FIELDS_ALL);
        System.out.println("command : " +info);
        String response=(String) msg.sendMessage(reader, info, elaraTransportListener);
        System.out.println("response : " +response);
        
        JSONObject    getInfoInterfaceJSON = new JSONObject(response);
           if(getInfoInterfaceJSON.get("_Interfaces").toString().contains("RS232"))
            {
               msg.elaraRS232 = true;
            }
           
        //Firmware update
        boolean status=(boolean) msg.elaraFirmwareUpgrade(reader,firmwareFile);
        if(status=true){
            System.out.println("Firmware update is successful");
        }else{
            System.out.println("Firmware update is failed");
        }
                    
         reader.closePort();
        } 
        }

