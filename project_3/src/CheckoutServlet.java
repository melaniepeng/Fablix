import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = "/api/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
	 @Resource(name = "jdbc/moviedb")
	  private DataSource dataSource;
	String loginUser = "mytestuser";
    String loginPasswd = "mypassword";
    String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String sum = request.getParameter("count");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String number = request.getParameter("number");
        String date = request.getParameter("date");
        System.out.println(firstname +" "+ lastname+ " "+number+" "+date);

        /**
         * This example only allows username/password to be anteater/123456
         * In real world projects, you should talk to the database to verify username/password
         */
        try {
        	Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		
            // Declare our statement
            Statement statement = connection.createStatement();

            String query = "SELECT firstname,id,lastname,expiration FROM creditcards;";
            
            // Perform the query
            ResultSet rs = statement.executeQuery(query);
          

            // Iterate through each row of rs
            
            Map<String, List> info = new HashMap<>();
            while (rs.next()) {
            	String tablefirstname = rs.getString("firstname");
            	String tabkelastname = rs.getString("lastname");
            	String tableid = rs.getString("id");
            	String expiration = rs.getString("expiration");
            	List people = new ArrayList<>();
            	people.add(tablefirstname);
            	people.add(tabkelastname);
            	people.add(expiration);
            	info.put(tableid, people);
    		}
            System.out.println(info);
            rs.close();
    		statement.close();
            
        if (info.containsKey(number) && firstname.equals(info.get(number).get(0)) && lastname.equals(info.get(number).get(1)) && date.equals(info.get(number).get(2)) ) {
            // Login succeeds
            // Set this user into current session
            String sessionId = ((HttpServletRequest) request).getSession().getId();
            Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
            request.getSession().setAttribute("user", new User(number));

            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");

            response.getWriter().write(responseJsonObject.toString());
        } else {
            // Login fails
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            if (!info.containsKey(number)) {
                responseJsonObject.addProperty("message", "wrong card information");
            }else if(!info.get(number).contains(firstname) && !info.get(number).contains(lastname)) {
            	responseJsonObject.addProperty("message", "incorrect username");
            } else if(!date.equals(info.get(number).get(2))) {
            	responseJsonObject.addProperty("message", "incorrect date");
        	}else {
            	responseJsonObject.addProperty("message", "incorrect name information");
            }
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
}
