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
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.thingmagic.SerialReader;
import com.thingmagic.TMConstants;
import java.io.FileInputStream;

/**
 *
 * @author pchinnapapannagari
 */
public class ModuleFWUpdate {
    public static SerialPort reader;
    public static void main(String argv[])
  {
        try
        {
            // Program setup
            Reader r ;
            FileInputStream fileStream;
            String fwFilename = "C:\\Users\\M6enApp-daily-YEATS-1.9.0.1B-2018-07-18-0107.sim";
            String readerUri ;
            String deviceName = "";
            SerialPort[] ports = SerialPort.getCommPorts();
            String[] results = new String[ports.length];
            for (int i = 0; i < ports.length; i++) 
            {
                results[i] = ports[i].getSystemPortName();
                deviceName = results[i];
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
            String PassThroughMode = "+++PassthroughUART1+++";
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
            
    
            //Disable Autonomous read
            String Stopauto = ejsonp.formJSONCommand(ECTConstants.SET_CFG_FIELDS_RDRSTART_NOTACTIVE);
            System.out.println("Command : " +Stopauto);
            String stopres=(String) msg.sendMessage(reader, Stopauto, elaraTransportListener);
            System.out.println("Response : " +stopres);
            Thread.sleep(2000);

            //PassThroughMode
            String respons=(String) msg.sendMessage(reader, PassThroughMode, elaraTransportListener);
            Thread.sleep(2000);
            readerUri = "tmr:///" + deviceName;
            // Create Reader object, connecting to physical device       
            r = Reader.create(readerUri);
            boolean val = reader.closePort();
            Thread.sleep(2000);
            try
            {   
                System.out.println("Trying to connect to the module....");
                r.connect();
                System.out.println("Connected Successfully to the module");
                if (Reader.Region.UNSPEC == (Reader.Region) r.paramGet("/reader/region/id"))
                {
                    Reader.Region[] supportedRegions = (Reader.Region[]) r.paramGet(TMConstants.TMR_PARAM_REGION_SUPPORTEDREGIONS);
                    if (supportedRegions.length < 1)
                    {
                        throw new Exception("Reader doesn't support any regions");
                    } else 
                    {
                        r.paramSet("/reader/region/id", supportedRegions[0]);
                    }
                }
            } 
            catch (Exception ex) 
            {
                if (ex.getMessage().equalsIgnoreCase("Application image failed CRC check")) 
                {
                    System.out.println("Current image failed CRC check. Replacing with new firmware");                    
                }
                else
                {
                    throw new Exception(ex);                    
                }
            }
            
            if (r instanceof SerialReader) 
            {
                
                fileStream = new FileInputStream(fwFilename);
                System.out.println("Loading firmware...");
                r.firmwareLoad(fileStream);
                fileStream.close();
            }
            String version = (String) r.paramGet("/reader/version/software");
            System.out.println("Firmware load successful with version " + version);
            // Shut down reader
            r.destroy();
        } 
        catch (ReaderException re) 
        {
            if (re.getMessage().equals("Invalid firmware load arguments")) 
            {
                System.exit(1);
            } 
            else 
            {
                System.out.println("Reader Exception : " + re.getMessage());
            }
        } 
        catch (Exception re)
        {
            System.out.println("Exception : " + re.getMessage());
        }
    }  
    
}
