import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
public class file {
	public static void main(String[] args) throws IOException {
		
		 try { 
	         String data = " Tutorials Point is a best website in the world";
	         File f1 = new File("/Users/kathy/Desktop/nice.txt");
	         if(!f1.exists()) {
	        	 System.out.println("create file");
	            f1.createNewFile();
	         } 
	         
	         BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/kathy/Desktop/nice.txt", true));
	        
	         writer.write("ndwafawfa");
	         
	         writer.close();
	         System.out.println("Done");
	         System.out.println(f1.getAbsolutePath());
	      } catch(IOException e){
	         e.printStackTrace();
	      }
		
		
		
	}


	
}
