import java.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Scanner;

class PythonInJava {
    public static void main(String args[]) {
    	
        try {
        	
        	Date beforeProcessDate = new Date(System.currentTimeMillis());
        	System.out.println("Before the Call to Process : "  + beforeProcessDate);
            Process p = Runtime.getRuntime().exec("python C:/LPDebug/lp_fina_new_5_final.py C:/LPDebug/Asian/lpopt_input.dat");
            
        	Date afterProcessDate = new Date(System.currentTimeMillis());
        	System.out.println("After the Call to Process : "  + afterProcessDate);

        	BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            
        	Date afterBRDate = new Date(System.currentTimeMillis());
        	System.out.println("After the Call to Process : "  + afterBRDate );
        	
        	System.out.println(in.readLine());
        	
        	in.close();
        	
        	
        } 
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
}



