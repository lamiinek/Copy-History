/*

CLIPBOARD HISTORY

This program will monitor the content of the clipboard and save it to a file
That way we'll be able to see evrything we have copied and when we have copied them


*/
package copyhistory;

/**
 *
 * @author lamine
 */

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CopyHistory {

    /**
     * @param args the command line arguments
     */
    
    private static String time;
    
    public static void main(String[] args) {
        
        saveClipData();
        
    }
    
    
    // get text in the clipboard
    public static String getClipText(){
        String clipContent = "";
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable content = cb.getContents(null); // always put null in it =D
        if(content != null && content.isDataFlavorSupported(DataFlavor.stringFlavor)){
            try{
                clipContent = String.valueOf(content.getTransferData(DataFlavor.stringFlavor));
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return clipContent;
    }
    
    public static void saveClipData(){
        ArrayList<String> copies = new ArrayList<>();
        String file = "cliphistory.txt";
        String logFile = "log_cliphistory.txt";
        PrintWriter writer = null;
        int i = 0;
        while(true){
            try{
                String text = getClipText();
                time = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime());
                if(!copies.contains(text)){
                    //System.out.println(text);// for debugging only
                    copies.add(text);
                    writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true))); // True means that we want to append to the file
                    writer.println(time+" | "+text);
                    Thread.sleep(30000);
                }
                // clear the list every 100 copies
                if(copies.size() >= 100){
                    String lastCopied = copies.get(copies.size()-1); // get the last element in the arraylist
                    copies.clear(); // clear the list
                    copies.add(lastCopied); // add the last element in the list to avoid duplicates
                    //throw new IllegalArgumentException("ohhh snap!"); // test of the log by throwing an exception ;)
                }
            }catch(Exception e){ 
                System.out.println(e.getMessage());
                try {
                    PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
                    w.println(time + "|" + e.getMessage());
                    w.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClipboardWatcher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }finally{
                writer.close(); // close the file once we are done writing in it
            }
            i++;
        }
        
    }
    
}
