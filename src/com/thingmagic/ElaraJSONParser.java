/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thingmagic;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author sghatak
 */

public class ElaraJSONParser {

    //Parse GetInfo JSON command response
    public HashMap parseGetInfo(String response) throws JSONException
    {
        HashMap<String,String> hmap = new HashMap<>();
        ArrayList<String> regionsList = new ArrayList<>();
        JSONObject jsono = new JSONObject(response);
        String command = (String) jsono.get("Report");
        int errID = (Integer) jsono.get("ErrID");
        JSONArray freqRegSet = (JSONArray) jsono.get("FreqRegSet");
        for(int i=0; i < freqRegSet.length(); i++){
           regionsList.add(freqRegSet.get(i).toString());
        }
        int rdrBufSize = (Integer) jsono.get("RdrBufSize");
        String rdrModel = (String) jsono.get("RdrModel");
        String rdrSN = (String) jsono.get("RdrSN");
        int rdrTemp = (Integer) jsono.get("RdrTemp");
        int rdrTempPA = (Integer) jsono.get("RdrTempPA");
        String version = (String) jsono.get("Version");
        JSONArray _interfaces = (JSONArray) jsono.get("_Interfaces");
        JSONObject _type = (JSONObject) _interfaces.get(0);
        String type = (String) _type.get("_Type");
        JSONArray _sensors = (JSONArray) jsono.get("_Sensors");
        JSONObject _model = (JSONObject) _sensors.get(0);
        String model = (String) _model.get("_Model");
        String sn = (String) _model.get("_SN");
        String fVersion = (String) _model.get("_Version");
        hmap.put("report", command);
        hmap.put("ErrID", String.valueOf(errID));
        hmap.put("FreqRegSet", regionsList.toString());
        hmap.put("RdrBufSize", String.valueOf(rdrBufSize));
        hmap.put("RdrModel", rdrModel);
        hmap.put("rdrSN", rdrSN);
        hmap.put("rdrTemp", String.valueOf(rdrTemp));
        hmap.put("rdrTempPA", String.valueOf(rdrTempPA));
        hmap.put("version", version);
        hmap.put("type", type);
        hmap.put("model", model);
        hmap.put("sn",sn);
        hmap.put("fVersion",fVersion);
        return hmap;
    }

    //Parse GetInfo JSON command response
    public HashMap parseGetCfgWorkflowResponse(String response) throws JSONException
    {
        HashMap<String,String> hmap = new HashMap<>();
        JSONObject json = new JSONObject(response);
        String command = (String) json.get("Report");
        int errID = (Integer) json.get("ErrID");
        String mode = json.getString("Mode");
        String mode_cfg = json.getString("Mode.Cfg");
        JSONObject parseWorkflowCfgs = new JSONObject(mode_cfg);
        float readPwr = ((Integer)parseWorkflowCfgs.get("ReadPwr")).floatValue();
        float writePwr = ((Integer)parseWorkflowCfgs.get("WritePwr")).floatValue();
        int session = parseWorkflowCfgs.getInt("Session");
        String target = parseWorkflowCfgs.getString("Target");
        String gen2Q = parseWorkflowCfgs.getString("Gen2Q");
        boolean gen2QEnable = parseWorkflowCfgs.getBoolean("Gen2QEnable");
        int gen2InitQVal = parseWorkflowCfgs.getInt("Gen2QInitVal");
        int blf = parseWorkflowCfgs.getInt("BLF");
        String dataEncoding = parseWorkflowCfgs.getString("DataEncoding");
        int rfOnTime = parseWorkflowCfgs.getInt("RFOnTime");
        int rfOffTime = parseWorkflowCfgs.getInt("RFOffTime");
        hmap.put("report", command);
        hmap.put("ErrID", String.valueOf(errID));
        hmap.put("Mode", mode);
        hmap.put("ReadPwr", String.valueOf(readPwr));
        hmap.put("WritePwr", String.valueOf(writePwr));
        hmap.put("Session", String.valueOf(session));
        hmap.put("Target", target);
        hmap.put("Gen2Q", gen2Q);
        hmap.put("Gen2QEnable", String.valueOf(gen2QEnable));
        hmap.put("Gen2QInitVal", String.valueOf(gen2InitQVal));
        hmap.put("BLF",String.valueOf(blf));
        hmap.put("DataEncoding",dataEncoding);
        hmap.put("RFOnTime",String.valueOf(rfOnTime));
        hmap.put("RFOffTime",String.valueOf(rfOffTime));
        return hmap;
    }

    //Parse GetCfg all JSON command response
    public HashMap parseGetCfgAllResponse(String response) throws JSONException
    {
        HashMap<String,String> hmap = new HashMap<>();
        JSONObject json = new JSONObject(response);
        String command = (String) json.get("Report");
        int errID = (Integer) json.get("ErrID");
        String mode = json.getString("Mode");
        String mode_cfg = json.getString("Mode.Cfg");
        JSONObject parseWorkflowCfgs = new JSONObject(mode_cfg);
        float readPwr = ((Integer)parseWorkflowCfgs.get("ReadPwr")).floatValue();
        float writePwr = ((Integer)parseWorkflowCfgs.get("WritePwr")).floatValue();
        int session = parseWorkflowCfgs.getInt("Session");
        String target = parseWorkflowCfgs.getString("Target");
        String gen2Q = parseWorkflowCfgs.getString("Gen2Q");
        boolean gen2QEnable = parseWorkflowCfgs.getBoolean("Gen2QEnable");
        int gen2InitQVal = parseWorkflowCfgs.getInt("Gen2QInitVal");
        int blf = parseWorkflowCfgs.getInt("BLF");
        String dataEncoding = parseWorkflowCfgs.getString("DataEncoding");
        int rfOnTime = parseWorkflowCfgs.getInt("RFOnTime");
        int rfOffTime = parseWorkflowCfgs.getInt("RFOffTime");
        String usbKBEnable = json.getString("_USBKBEnable");
        String kbDataFormat = json.getString("_KBDataFormat");
        String readMode = json.getString("ReadMode");
        String kbFieldSeparator = json.getString("_KBFieldSeparator");
        String kbRecordSeparator = json.getString("_KBRecordSeparator");
        String audioVolume = json.getString("_AudioVolume");
        String dateTime = json.getString("DateTime");
        String rdrStart = json.getString("RdrStart");
        String freqReg = json.getString("FreqReg");
        boolean spotInvCnt = json.getBoolean("SpotInvCnt");
        boolean spotRSSI = json.getBoolean("SpotRSSI");
        boolean spotAnt = json.getBoolean("SpotAnt");
        boolean spotDT = json.getBoolean("SpotDT");
        boolean spotPhase = json.getBoolean("SpotPhase");
        boolean spotProf = json.getBoolean("SpotProf");
        boolean spotRz = json.getBoolean("SpotRz");
        boolean spotFreq = json.getBoolean("SpotFreq");
        boolean spotGen2_BI = json.getBoolean("SpotGen2_BI");
        boolean spotGen2_Q = json.getBoolean("SpotGen2_Q");
        boolean spotGen2_LF = json.getBoolean("SpotGen2_LF");
        boolean spotGen2_Target = json.getBoolean("SpotGen2_Target");
        boolean spotGPIO = json.getBoolean("SpotGPIO");
        boolean spotProt = json.getBoolean("SpotProt");
        boolean spotSensor = json.getBoolean("SpotSensor");
        hmap.put("report", command);
        hmap.put("ErrID", String.valueOf(errID));
        hmap.put("Mode", mode);
        hmap.put("ReadPwr", String.valueOf(readPwr));
        hmap.put("WritePwr", String.valueOf(writePwr));
        hmap.put("Session", String.valueOf(session));
        hmap.put("Target", target);
        hmap.put("Gen2Q", gen2Q);
        hmap.put("Gen2QEnable", String.valueOf(gen2QEnable));
        hmap.put("Gen2QInitVal", String.valueOf(gen2InitQVal));
        hmap.put("BLF", String.valueOf(blf));
        hmap.put("DataEncoding", dataEncoding);
        hmap.put("RFOnTime", String.valueOf(rfOnTime));
        hmap.put("RFOffTime", String.valueOf(rfOffTime));
        hmap.put("_USBKBEnable", usbKBEnable);
        hmap.put("_KBDataFormat", kbDataFormat);
        hmap.put("ReadMode", readMode);
        hmap.put("_KBFieldSeparator", kbFieldSeparator);
        hmap.put("_KBRecordSeparator", kbRecordSeparator);
        hmap.put("_AudioVolume", audioVolume);
        hmap.put("DateTime", dateTime);
        hmap.put("RdrStart", rdrStart);
        hmap.put("FreqReg", freqReg);
        hmap.put("SpotInvCnt",String.valueOf(spotInvCnt));
        hmap.put("SpotRSSI",String.valueOf(spotRSSI));
        hmap.put("SpotAnt",String.valueOf(spotAnt));
        hmap.put("SpotDT",String.valueOf(spotDT));
        hmap.put("SpotPhase",String.valueOf(spotPhase));
        hmap.put("SpotProf",String.valueOf(spotProf));
        hmap.put("SpotRz",String.valueOf(spotRz));
        hmap.put("SpotFreq",String.valueOf(spotFreq));
        hmap.put("SpotGen2_BI",String.valueOf(spotGen2_BI));
        hmap.put("SpotGen2_Q",String.valueOf(spotGen2_Q));
        hmap.put("SpotGen2_LF",String.valueOf(spotGen2_LF));
        hmap.put("SpotGen2_Target",String.valueOf(spotGen2_Target));
        hmap.put("SpotGPIO",String.valueOf(spotGPIO));
        hmap.put("SpotProt",String.valueOf(spotProt));
        hmap.put("SpotSensor",String.valueOf(spotSensor));
        return hmap;
    }

    public String formJSONCommand(String command)
    {
        final String commandHeader = "Cmd";
        final String commandWorkflow = "Mode";
        final String commandRdrStart = "RdrStart";
        final String commandProfile = "Prof";
        JSONObject obj = new JSONObject();
        try
        {
            switch(command){
                case "GetInfoFieldsAll":
                    obj.put(commandHeader, "GetInfo");
                    JSONArray infoarray = new JSONArray();
                    infoarray.put("ALL");
                    obj.put("Fields", infoarray);
                    break;
                case "GetCfgFieldsAll":
                    obj.put(commandHeader, "GetCfg");
                    JSONArray cfgarrayall = new JSONArray();
                    cfgarrayall.put("ALL");
                    obj.put("Fields", cfgarrayall);
                    break;
                case "ActivateUpdateMode":
                    obj.put(commandHeader, "ActivateUpdateMode");
                    break;
                case "StartUpdate":
                    obj.put(commandHeader,"StartUpdate");
                    obj.put("Section","App");
                    obj.put("Password","0x02254410");
                    break;
                case "EndUpdate":
                    obj.put(commandHeader, "EndUpdate");
                    break;
                case "GetCfgFieldsDateTime":
                    obj.put(commandHeader,"GetCfg");
                    JSONArray cfgarraydatetime = new JSONArray();
                    cfgarraydatetime.put("DateTime");
                    obj.put("Fields", cfgarraydatetime);
                    break;
                case "GetCfgFieldsMode":
                    obj.put(commandHeader,"GetCfg");
                    JSONArray cfgArrayWorkflow = new JSONArray();
                    cfgArrayWorkflow.put("Mode");
                    obj.put("Fields", cfgArrayWorkflow);
                    break;
                case "StartRZ":
                    obj.put(commandHeader,"StartRZ");
                    break;
                case "StopRZ":
                    obj.put(commandHeader, "StopRZ");
                    break;
                case "Reboot":
                    obj.put(commandHeader,"Reboot");
                    break;
                case "GetGPIOs":
                    obj.put(commandHeader,"GetGPIOs");
                    break;
                case "ReadFields":
                    obj.put(commandHeader,"ReadFields");
                    break;
                case "SetCfgModeHDR":
                    obj.put(commandHeader, "SetCfg");
                    obj.put(commandWorkflow,"HDR");
                    break;
                case "SetCfgModeMonitor":
                    obj.put(commandHeader,"SetCfg");
                    obj.put(commandWorkflow, "MONITOR");
                    break;
                case "SetCfgModeTagCommission":
                    obj.put(commandHeader,"SetCfg");
                    obj.put(commandWorkflow, "TagCommission");
                    break;
                case "SetCfgModeTagUpdate":
                    obj.put(commandHeader,"SetCfg");
                    obj.put(commandWorkflow, "TagUpdate");
                    break;
                case "SetCfgFieldsRdrStartActive":
                    obj.put(commandHeader,"SetCfg");
                    obj.put(commandRdrStart, "ACTIVE");
                    break;
                case "SetCfgFieldsRdrStartNotActive":
                    obj.put(commandHeader,"SetCfg");
                    obj.put(commandRdrStart, "NOTACTIVE");
                    break;
                case "GetCfgFieldsRdrStart":
                    obj.put(commandHeader, "GetCfg");
                    JSONArray getcfgRdrStart = new JSONArray();
                    getcfgRdrStart.put("RdrStart");
                    obj.put("Fields", getcfgRdrStart);
                    break;
                case "SaveFields":
                    obj.put(commandHeader,"SaveFields");
                    break;
                case "ClearFields":
                    obj.put(commandHeader,"DefaultFields");
                    break;
                case "SetCfgSingleReadAutonomousTrue":
                    obj.put(commandHeader,"SetCfg");
                    obj.put(commandWorkflow, "SingleRead");
                    obj.put("Autonomous", "true");
                    break;
                case "SetCfgSingleReadAutonomousFalse":
                    obj.put(commandHeader,"SetCfg");
                    obj.put(commandWorkflow, "SingleRead");
                    obj.put("Autonomous", "false");
                    break;
                case "ThisTagProf":
                    obj.put(commandHeader, "ThisTag");
                    JSONArray profNum = new JSONArray();
                    profNum.put(1);
                    obj.put(commandProfile, profNum);
                    break;
                case "SetCfgHeartbeat":
                    obj.put(commandHeader,"SetCfg");
                    obj.put("HBPeriod", 1.0);
                    break;
                case "SetCfgStopHeartbeat":
                    obj.put(commandHeader,"SetCfg");
                    obj.put("HBPeriod", 0.0);
                    break;
                case "GetCfgFieldsRegion":
                    obj.put(commandHeader,"GetCfg");
                    obj.put("_Class", "_TM_PRIV_HWINFO_");
                    JSONArray regionArray = new JSONArray();
                    regionArray.put("Region");
                    obj.put("Fields", regionArray);
                    break;
                case "SetCfgUSBKBEnable":
                    obj.put(commandHeader,"SetCfg");
                    obj.put("_USBKBEnable", true);
                    break;
                case "SetCfgUSBKBDisable":
                    obj.put(commandHeader,"SetCfg");
                    obj.put("_USBKBEnable", false);
                    break;
            }
        }
        catch(JSONException ex)
        {
            java.util.logging.Logger.getLogger(ElaraJSONParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.toString();
    }

    /**
     * Frames the JSON message required for executing write data profile.
     *
     * @param memBank - the memory bank to write data.
     * @param startAddress - the start address of the memory to write data.
     * @param wordCount - the number of words to write.
     * @param retryLimit - the number of times write data operation is to be repeated, if fails upon first attempt.
     * @param writeData - the hexadecimal data to be written(in words).
     * @param check - When set to "true", the reader shall read the written data to confirm it was correctly written.
     * @param isDTSelected - When set to true, user wants to write current date and time to the specified memory bank
     * @return the framed JSON string to send to reader.
     */
    public String setProfileWriteData(String memBank, int startAddress, int wordCount, int retryLimit, String writeData, boolean check, boolean isDTSelected)
    {
        final String commandProfile = "Write";
        final String commandHeader = "Cmd";
        JSONObject obj = new JSONObject();
        ElaraUtil utils = new ElaraUtil();
        try
        {
            obj.put(commandHeader,"SetProf");
            
            JSONArray setWriteProperties = new JSONArray();
            JSONArray setWriteProfile = new JSONArray();
            
            setWriteProfile.put(utils.getMemBank(memBank));
            setWriteProfile.put(startAddress);
            setWriteProfile.put(wordCount);
            setWriteProfile.put(retryLimit);
            
            JSONArray writeDataVal = new JSONArray();
            if(isDTSelected)
            {
                writeDataVal.put("DT");
            }
            else
            {
                writeDataVal.put("VAL");
                writeDataVal.put(utils.splitToNChar(writeData,4));
            }
            setWriteProfile.put(writeDataVal);
            setWriteProperties.put(setWriteProfile);
            setWriteProperties.put(check);
            
            obj.put(commandProfile, setWriteProperties);
        }
        catch(JSONException ex)
        {
            java.util.logging.Logger.getLogger(ElaraJSONParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.toString();
    }

    /**
     * Check for the RAIN command being sent is successful or not.
     *
     * @param response - the response received.
     * @return commandStatus - whether it is successful or not.
     */
    public boolean isCommandSuccess(String response) throws JSONException
    {
        boolean commandStatus;
        JSONObject jsonObj = new JSONObject(response);
        int errID = (Integer) jsonObj.get("ErrID");
        if(errID == 0)
        {
            commandStatus = true;
        }
        else
        {
            commandStatus = false;
        }
        return commandStatus;
    }

    /**
     * Return the error information received from the response of RAIN command.
     *
     * @param response - the response received to RAIN command.
     * @return errorMsg - the ErrorInfo field value from response.
     */
    public String errorInfo(String response) throws JSONException
    {
        JSONObject jsonObj;
        String errorMsg = "";
        try
        {
            jsonObj = new JSONObject(response);
            errorMsg =  jsonObj.getString("ErrInfo");
        }
        catch(JSONException ex)
        {
            java.util.logging.Logger.getLogger(ElaraJSONParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return errorMsg;
    }

    public boolean parseBootLoaderResponse(String response)
    {
        boolean commandStatus = false;
        try {
            
            JSONObject jsonObj = new JSONObject(response);
            String version = (String) jsonObj.get("Version");
            if(version.isEmpty() || version.length() ==0 || version == null)
            {
                commandStatus = false;
            }
            else
            {
                if(!version.contains("."))
                {
                    commandStatus = true;
                }
            }
            
        } catch (JSONException ex) {
            commandStatus = false;
            Logger.getLogger(ElaraJSONParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return commandStatus;
    }
    
    public String customJSONCommand(String command, StringBuffer sb)
    {
        final String commandHeader = "Cmd";
        final String commandWorkflow = "Workflow";
        JSONObject obj = new JSONObject();
        try
        {
            switch(command)
            {
                case "SetCfgFieldsDateTime":
                    obj.put(commandHeader,"SetCfg");
                    obj.put("DateTime", sb.toString());
                    break;
                case "SetCfgAudioVolume":
                    obj.put(commandHeader,"SetCfg");
                    obj.put("_AudioVolume",sb.toString());
                    break;
                case "SetCfgKBDataFormat":
                    obj.put(commandHeader,"SetCfg");
                    obj.put("_KBDataFormat",sb.toString());
                    break;
                case "SetRzReadPwr":
                    obj.put(commandHeader,"SetRZ");
                    obj.put("ID",1);
                    obj.put("ReadPwr",Double.parseDouble(sb.toString()));
                    break;
                case "SetCfgFreqReg":
                    obj.put(commandHeader,"SetCfg");
                    obj.put("FreqReg",sb.toString());
                    break;
                case "SetCfgMetadata":
                    obj.put(commandHeader, "SetCfg");
                    String[] fields = sb.toString().split(",");
                    for (String field : fields) 
                    {
                        String[] values = field.split(":");
                        if(values[1].equalsIgnoreCase("true"))
                        {
                            obj.put(values[0], true);
                        }
                        else
                        {
                            obj.put(values[0], false);
                        }
                        
                    }
                    break;
            }
        }
        catch(JSONException ex)
        {
            java.util.logging.Logger.getLogger(ElaraJSONParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.toString();
    }
    
    public HashMap parseDateTime(String response)
    {
    
        HashMap<String, String> dateTimeHMap =  new HashMap<>();
        try 
        {
            JSONObject jsono = new JSONObject(response);
            String report = (String) jsono.get("Report");
            int ErrID = (Integer) jsono.get("ErrID");
            String DateTime = (String) jsono.get("DateTime");
            dateTimeHMap.put("report", report);
            dateTimeHMap.put("ErrID", String.valueOf(ErrID));
            dateTimeHMap.put("DateTime", DateTime);
        } 
        catch (JSONException ex) 
        {
            Logger.getLogger(ElaraJSONParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateTimeHMap;
    }
    
    public String[][] formatDateTime(String value)
    {
            int startIndex = 0;
            int tIndex = value.indexOf("T");
            String dateString = value.substring(startIndex, tIndex);
            String timeString = value.substring(tIndex+1,value.length()-1);
            String[] date = dateString.split("-");
            String[] time = timeString.split(":");
            String[][] dateTimeArray = {date, time};
            return dateTimeArray;
    }
    
    public String getCurrentTime()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
    
    public static final LocalDate currentDate(){
        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date , formatter);
        return localDate;
    }
}
