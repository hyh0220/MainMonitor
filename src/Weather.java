/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author yanhaohu
 */
public class Weather extends Thread{
    private String filename;
    private PrintWriter writer;
    private String time;
    public Weather(){
        
    }
    public void run(){
        while(!isInterrupted()){
            try {
                Date d = new Date();
                int Min=d.getMinutes();
                SimpleDateFormat fn = new SimpleDateFormat ("yyyy.MM.dd'weather'");
                SimpleDateFormat tm = new SimpleDateFormat ("hhmmss");
                SimpleDateFormat dt = new SimpleDateFormat ("yyyyMMdd");
                String oldtime = dt.format(d);
                filename = fn.format(d);
                File file = new File("/Users/yanhaohu/Desktop/output/"+oldtime+"/"+filename+".txt");
                file.getParentFile().mkdir();
                writer = new PrintWriter (file);
                while(!isInterrupted()){
                    try {
                        try{
                            Date day = new Date();
                            time = tm.format(day);
                            //stop and create new file
                            String D = dt.format(day);
                            if (!D.equals(oldtime)){
                                break;
                            }
                            //test use
                            if (day.getMinutes()-Min>=1){
                                break;
                            }
                            // get URL from weather underground
                            URL JsonURL = new URL("http://api.wunderground.com/api/4fd6e9b1874ba8a5/conditions/q/IA/IOWA_CITY.json");
                            InputStream Json = JsonURL.openStream();
                            InputStreamReader J = new InputStreamReader(Json);
                            //Parse Json file
                            JSONParser jsonParser = new JSONParser();
                            JSONObject jsonObject = (JSONObject) jsonParser.parse(J);
                            //getting information
                            JSONObject current = (JSONObject) jsonObject.get("current_observation");
                            double temperature = (double) current.get("temp_f");
                            String weather = (String) current.get("weather");
                            String visibility = (String) current.get("visibility_mi");
                            String rain1h = (String) current.get("precip_1hr_in");
                            String rainday = (String) current.get("precip_today_in");
                            //printout information
                            System.out.println("Weather:"+time+","+temperature+","+weather+","+visibility+","+rain1h+","+rainday);
                            writer.println(time+","+temperature+","+weather+","+visibility+","+rain1h+","+rainday);
                            
                        } catch (        IOException | ParseException ex) {
                            Logger.getLogger(Weather.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Thread.sleep(1200000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        writer.close();
                        MainMonitor.TA.insert("Weather interrupted\n", 1);
                    }
                }
                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Weather.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
