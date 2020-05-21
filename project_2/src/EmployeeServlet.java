import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jasypt.util.password.StrongPasswordEncryptor;

import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "EmployeeServlet", urlPatterns = "/api/Employee")
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    private static boolean verifyCredentials(String email, String password) throws Exception 
    {
    	long startquerytime = System.nanoTime();

    	System.out.print("2"+email+password);
    	
    	Context initCtx = new InitialContext();

        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        
        // Look up our data source
        DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");

        // the following commented lines are direct connections without pooling
        //Class.forName("org.gjt.mm.mysql.Driver");
        //Class.forName("com.mysql.jdbc.Driver").newInstance();
        //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
   

        Connection dbcon = ds.getConnection();
        //dbcon.setAutoCommit(false);
    	Class.forName("com.mysql.jdbc.Driver").newInstance();
 		// create database connection
    	
 		
		String query = String.format("SELECT * from employees where email=?");
		java.sql.PreparedStatement statement = dbcon.prepareStatement(query);
		statement.setString(1, email);
		long startJDBCtime = System.nanoTime();
		
		ResultSet rs = statement.executeQuery();
		 long endJDBCtime = System.nanoTime();
		

		boolean success = false;
		if (rs.next()) {
		    // get the encrypted password from the database
			
			String encryptedPassword = rs.getString("password");
			System.out.println("40！！！！"+encryptedPassword);
			// use the same encryptor to compare the user input password with encrypted password stored in DB
			success = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
			System.out.println("5!!!!"+success);
		}
		

		rs.close();
		statement.close();
		dbcon.close();
		long endQueryTime = System.nanoTime();
		 File myfile = new File("text.txt");
		if(!myfile.exists()) {
			myfile.createNewFile();
         } 
		BufferedWriter writer = new BufferedWriter(new FileWriter(myfile.getAbsolutePath(), true));
		writer.write("EmployeeServlet"+"\n");
         long totalJDBCtime = endJDBCtime - startJDBCtime;
	     long totalQuerytime = endQueryTime - startquerytime;
	     writer.write("totalQuerytime ");
	     writer.write( Long.toString(totalQuerytime)+"\n");
	     writer.write("totalJDBCtime ");
	     writer.write(Long.toString(totalJDBCtime)+"\n");
	     writer.close();
		
		System.out.println("verify " + email + " - " + password);

		return success;
	}
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter(); 
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.print(username+" "+password);
     
        /**
         * This example only allows username/password to be anteater/123456
         * In real world projects, you should talk to the database to verify username/password
         */
        try {
        		System.out.print(verifyCredentials("9"+username, password));
        		if(verifyCredentials(username, password)) {
				System.out.print(verifyCredentials(username, password));
			   
				// Login succeeds
	            // Set this user into current session
	            String sessionId = ((HttpServletRequest) request).getSession().getId();
	            Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
	            request.getSession().setAttribute("user", new User(username));

	            JsonObject responseJsonObject = new JsonObject();
	            responseJsonObject.addProperty("status", "success");
	            responseJsonObject.addProperty("message", "success");

	            response.getWriter().write(responseJsonObject.toString());
			}
		
			else {
            // Login fails

	            JsonObject responseJsonObject = new JsonObject();
	            responseJsonObject.addProperty("status", "fail");
	            responseJsonObject.addProperty("message", "wrong information");
	            response.getWriter().write(responseJsonObject.toString());
			}
        		 
        } catch (Exception e) {
    		/*
    		 * After you deploy the WAR file through tomcat manager webpage,
    		 *   there's no console to see the print messages.
    		 * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
    		 * 
    		 * To view the last n lines (for example, 100 lines) of messages you can use:
    		 *   tail -100 catalina.out
    		 * This can help you debug your program after deploying it on AWS.
    		 */
    		e.printStackTrace();}

    
}
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
  }
