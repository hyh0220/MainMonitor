import java.awt.*;        // using AWT containers and components
import java.awt.event.*;  // using AWT events and listener interfaces
import java.util.logging.Level;
import java.util.logging.Logger;
 
// An AWT GUI program inherits the top-level container java.awt.Frame
public class MainMonitor extends Frame implements ActionListener, WindowListener{
   Weather weather = new Weather();
   CambusMonitor Red = new CambusMonitor("uiowa","red");
   CambusMonitor Blue = new CambusMonitor("uiowa","blue");
   CambusMonitor Studio = new CambusMonitor("uiowa","studart");
   CambusMonitor Hawkdorm= new CambusMonitor("uiowa","hawkdorm");
   CambusMonitor hawkexpress= new CambusMonitor("uiowa","hawkexpress");
   CambusMonitor interdorm= new CambusMonitor("uiowa","interdorm");
   CambusMonitor mayflower= new CambusMonitor("uiowa","mayflower");
   CambusMonitor mts= new CambusMonitor("uiowa","mts");
   CambusMonitor pentnight= new CambusMonitor("uiowa","pentnight");
   CambusMonitor pentacrest= new CambusMonitor("uiowa","pentacrest");
        
   CambusMonitor seventhave= new CambusMonitor("iowa-city","seventhave");
   CambusMonitor brdwy= new CambusMonitor("iowa-city","brdwy");
   CambusMonitor brdwynw= new CambusMonitor("iowa-city","brdwynw");
   CambusMonitor crosspark= new CambusMonitor("iowa-city","crosspark");
   CambusMonitor manville= new CambusMonitor("iowa-city","manville");
        
   CambusMonitor tenthst= new CambusMonitor("coralville","tenthst");
   CambusMonitor amexpress= new CambusMonitor("coralville","amexpress");
   CambusMonitor amnorthlib= new CambusMonitor("coralville","amnorthlib");
   CambusMonitor lantern= new CambusMonitor("coralville","lantern");
   private Label lblInput;     // declare input Label
   static public TextArea TA;
   static public TextField TF;
   private Button btn;
   private Button btn2;
   private int sum = 0;        // accumulated sum, init to 0
   int i=0,k=0;
   Running R = new Running();
   
   public MainMonitor() {
      setLayout(new FlowLayout());
      lblInput = new Label("Cambus Data Fetching System "); // construct Label
      add(lblInput);
      TF = new TextField("/Users/yanhaohu/Desktop/output/");
      add(TF);
      btn = new Button("Start"); 
      addWindowListener(this);
      add(btn);
      btn.addActionListener(this);
      btn2 = new Button("Stop");
      add(btn2);
      btn2.addActionListener(this);
      TA = new TextArea();
      add(TA);
      TA.setEditable(false);
      TA.setSize(10, 10);
      setTitle("LOL");  // "this" Frame sets title
      setSize(700, 250);  // "this" Frame sets initial window size
      setVisible(true);   // "this" Frame shows
   }
 
   /** The entry main() method
     * @param args */
   public static void main(String[] args) {
      // Invoke the constructor to setup the GUI, by allocating an anonymous instance
      new MainMonitor();
   }
   
 public class Running extends Thread{
    public void run(){
        do {
            TA.insert("\n"+Integer.toString(k),1);
            k++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } while (!isInterrupted());
        
    }
}

   @Override
   public void actionPerformed(ActionEvent evt) {
          if(evt.getSource()==btn){
          System.out.println("Started");
          TA.setText("\nRunning...\n");
          weather.start();
          Red.start();
          Blue.start();
          Studio.start();
          Hawkdorm.start();
          hawkexpress.start();
          interdorm.start();
          mayflower.start();
          mts.start();
          pentnight.start();
          pentacrest.start();
        
          seventhave.start();
          brdwy.start();
          brdwynw.start();
          crosspark.start();
          manville.start();
        
          tenthst.start();
          amexpress.start();
          amnorthlib.start();
          lantern.start();
      }
      else if (evt.getSource()==btn2) {
          weather.interrupt();
          Red.interrupt();
          Blue.interrupt();
          Studio.interrupt();
          Hawkdorm.interrupt();
          hawkexpress.interrupt();
          interdorm.interrupt();
          mayflower.interrupt();
          mts.interrupt();
          pentnight.interrupt();
          pentacrest.interrupt();
        
          seventhave.interrupt();
          brdwy.interrupt();
          brdwynw.interrupt();
          crosspark.interrupt();
          manville.interrupt();
        
          tenthst.interrupt();
          amexpress.interrupt();
          amnorthlib.interrupt();
          lantern.interrupt();
          
          TA.insert("Stopped\n",1);
          System.out.println("Stoped");
       }
   }
   

   @Override
   public void windowClosing(WindowEvent e) {
      System.out.println("Closing");
      System.exit(0);
      // terminate the program
   }
    @Override
    public void windowOpened(WindowEvent e) {
        System.out.println("hello"); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) { }
    @Override
    public void windowDeactivated(WindowEvent e) { }
}

    

