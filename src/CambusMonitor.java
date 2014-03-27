
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yanhaohu
 */
public class CambusMonitor extends Thread{
    private String Bus;
    private String Agency;
    //constructor
    public CambusMonitor(String Agency, String bus){
        this.Bus=bus;
        this.Agency=Agency;
    }
    public void run(){
        String oldtime;
        while (!isInterrupted()){
            Date day=new Date();
            SimpleDateFormat Sp = new SimpleDateFormat ("yyyyMMdd");
            oldtime = Sp.format(day);
            
            //current minute, for testing purpose
//            int Min=day.getMinutes();

            String filename = generateFilename();
            Map map = new HashMap();
            Hashtable ht = new Hashtable();
            Enumeration ID = ht.keys();
            String id = "";
            String location = "";

            //temp stores previous location
            PrintWriter writer;
            String fileroot = MainMonitor.TF.getText();

            try{
                File file = new File(fileroot+filename+"/"+filename+Bus+".txt");
                file.getParentFile().mkdirs();
                writer = new PrintWriter (file);
                URL xmlURL=new URL("http://api.ebongo.org/buslocation?agency="+Agency+"&route="+Bus+"&api_key=xApBvduHbU8SRYvc74hJa7jO70Xx4XNO");
                InputStream xml;
                DocumentBuilderFactory docBuilderFactory;
                docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc;
                //continously fetch data
                while(!isInterrupted()){
                    Date d = new Date();
                    String Time = generateTime();
                    //for test purpose, only run for 1 minutes
//                    if (d.getMinutes()-Min>=1){
//                        break;
//                    }
                    //run again when date changes
                    String D = Sp.format(d);
                    if (!D.equals(oldtime)) {
                        break;
                    }
                    try {
                        //get API xml file
                       xml = xmlURL.openStream();
                       doc = docBuilder.parse (xml);
                       xml.close();

                        // normalize text representation
                        doc.getDocumentElement ().normalize ();
                        //System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());


                        NodeList listOfPredictions = doc.getElementsByTagName("bus");
                        int totalPredictions = listOfPredictions.getLength();
                        //System.out.println("Total no of predictions : " + totalPredictions);

                        //loop through all prediction (buses)
                        for(int s=0; s<listOfPredictions.getLength() ; s++){
                            String output =Bus+",";
                            Node firstPredictionNode = listOfPredictions.item(s);
                            if(firstPredictionNode.getNodeType() == Node.ELEMENT_NODE){

                                Element firstPredictionElement = (Element)firstPredictionNode;

                                //get id
                                NodeList idList = firstPredictionElement.getElementsByTagName("id");
                                Element idElement = (Element)idList.item(0);

                                NodeList textIdList = idElement.getChildNodes();
                                //System.out.println("id : " + ((Node)textIdList.item(0)).getNodeValue().trim());
                                output=output+((Node)textIdList.item(0)).getNodeValue().trim()+",";
                                id =((Node)textIdList.item(0)).getNodeValue().trim(); //get current id

                                //get lat
                                NodeList latList = firstPredictionElement.getElementsByTagName("lat");
                                Element latElement = (Element)latList.item(0);

                                NodeList textLatList = latElement.getChildNodes();
                                //System.out.println("lat : " + ((Node)textLatList.item(0)).getNodeValue().trim());
                                output=output+((Node)textLatList.item(0)).getNodeValue().trim()+",";
                                location = ((Node)textLatList.item(0)).getNodeValue().trim();

                                //get lng
                                NodeList lngList = firstPredictionElement.getElementsByTagName("lng");
                                Element lngElement = (Element)lngList.item(0);

                                NodeList textLngList = lngElement.getChildNodes();
                                //System.out.println("lng : " + ((Node)textLngList.item(0)).getNodeValue().trim());
                                output = output +((Node)textLngList.item(0)).getNodeValue().trim()+",";
                                location += ((Node)textLngList.item(0)).getNodeValue().trim(); //get current location

                                //get heading
                                NodeList headingList = firstPredictionElement.getElementsByTagName("heading");
                                Element headingElement = (Element)headingList.item(0);

                                NodeList textHeadingList = headingElement.getChildNodes();
                                //System.out.println("heading : " + ((Node)textHeadingList.item(0)).getNodeValue().trim());
                                output = output + ((Node)textHeadingList.item(0)).getNodeValue().trim();
                            }
                            System.out.println("OutPut: "+Time+output);
                            //initialize hashtable
                            if (ht.get(id) == null){
                                ht.put(id, "");
                            }
                            //if the location changes, wirte to file
                            if (! ht.get(id).equals(location)){
                                writer.println(Time+output);    
                            }
                            //match id woth location
                            ht.put(id, location);
                        }
                    }
                    catch (SAXParseException err) {
                        System.out.println ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
                        System.out.println(" " + err.getMessage ());
                    }
                    catch (SAXException e) {
                        Exception x = e.getException ();
                    } catch (IOException ex) {
                        Logger.getLogger(CambusMonitor.class.getName()).log(Level.SEVERE, null, ex);
                        MainMonitor.TA.insert("IO Error", 1);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        writer.close();
                        MainMonitor.TA.insert(Bus+" interrupted\n", 1);
                    }
                }
                writer.close();
            }
            catch (MalformedURLException | ParserConfigurationException | FileNotFoundException ex) {
                Logger.getLogger(CambusMonitor.class.getName()).log(Level.SEVERE, null, ex);
                MainMonitor.TA.insert("URL Error", 1);
            }   
        }
    }
    private String generateFilename(){
        String filename;
        Date d = new Date();
        SimpleDateFormat fn = new SimpleDateFormat ("yyyyMMdd");
        filename = fn.format(d);
        return filename;
    }
    //generate time
    private String generateTime(){
        String time;
        Date d = new Date();
        SimpleDateFormat fn = new SimpleDateFormat ("hhmmss");
        time =fn.format(d)+",";
        return time;   
    }
}
