

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MovieListServlet
 */
@WebServlet("/BrowsingServlet")
public class BrowsingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String loginUser = "mytestuser";
	String loginPasswd = "mypassword";
	String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BrowsingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stu
		String[] cartArray = request.getParameterValues("cartArray");
		response.setContentType("text/html"); 
		PrintWriter out = response.getWriter();
	        out.println("<html>");
	        out.println("<head><title>Projec_2_Browsing</title></head>");
	        out.println("<style>\n" + 
	        					"body{background-image: url(\"bg-2.jpg\");\n" + 
	        					" background-repeat:no-repeat;\n" + 
	        					"front-family: Apple Chancery;"
	        					+ "position:center;}\n" + 
	        					"h3，h1{color: black;\n" + 
	        					"		padding-up: 30px;\n"+
	        					"padding-left: 2000px;}"+
	        			"<style>\n" + 
	        				"a {\n" + 
	        					"  text-decoration: none;\n" + 
	        					"  display: inline-block;\n" + 
	        					"  padding: 8px 16px;"+
	        					"}\n" + 
	        					"\n" + 
	        					"a:hover {\n" + 
	        					"  background-color: #ddd;\n" + 
	        					"  color: black;\n" + "background-color: black;\n"+
	        					"  color: white;"+
	        					"}\n" +".button {\n" + 
	        					"    background-color: black; /* Green */\n" + 
	        					"    border: none;\n" + 
	        					"    color: white;\n" + 
	        					"    padding: 15px 32px;\n" + 
	        					"    text-align: center;\n" + 
	        					"    text-decoration: none;\n" + 
	        					"    display: inline-block;\n" + 
	        					"    font-size: 10px;\n" + 
	        					"    margin: 4px 2px;\n" + 
	        					"    cursor: pointer;\n" + 
	        					"    opacity: 0.9;\n" + 
	        					"}"+
	        			"</style><body>");
	        
	        String a = "";
	        if(cartArray == null)
	        {
	        	System.out.println("NOT WORKING!"+cartArray);
	        }
	        else {
				for(int i = 0; i < cartArray.length; i++)
				{
					System.out.println("Moviecart:" +cartArray[i]);
					if(cartArray[i].equals("") == false)
					{
						if(i >= cartArray.length-1)
							a += cartArray[i];
						else
							a += cartArray[i] + ",";
					}
				}
			}
	        
	      
	    try 
	    {
	     	Class.forName("com.mysql.jdbc.Driver").newInstance();
	 		// create database connection
	 		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	 		
	         // Declare our statement
	         Statement statement = connection.createStatement();
	         String query="SELECT name, id FROM genres";
	         ResultSet rs = statement.executeQuery(query);
	         out.print("<h1> Browsing </h1>");
	         out.print("<h3>Choose the genre that your are interested in</h3>");
	         while (rs.next()) {
	        	 String name = rs.getString("name");
	        	 String id = rs.getString("id");
	        	 out.print("<a href = \"SearchServlet?title=&year=&director=&star=&num=10&type=title&direction=ASC&titlestart=&page=Search&gid="+id+"&cartArray="+ a +"\">"+ name +"</a>");
	        	 out.print(" | </body>");
	         }
	         rs.close();
	         query = "select DISTINCT(SUBSTRING(m.title, 1,1)) as first\n" + 
	         		"from movies m\n" + 
	         		"order by first";
	         rs = statement.executeQuery(query);
	         out.print("<h3>Choose the alphabet of the movies that your are interested in</h3>");
	         while (rs.next()) {
	        	 String name = rs.getString("first");
	        	 if (Pattern.matches("^[a-zA-Z0-9]+$", name))
	        	 {
	        		 out.print("<a href = \"SearchServlet?title=&year=&director=&star=&num=10&type=title&direction=ASC&titlestart=" + name +"&page=Search&gid=&cartArray="+ a +"\">"+ name +"</a>");
	        		 out.print(" | </body>");
	        	 }
	         }
	         out.print("<script type=\"text/javascript\">\n" +
	         		"function toCheckout(){ \n"+
	                 "    window.location.href=\"ShoppingServlet?cartArray=" + a +"&cartArray=\"\n" + 
	                 "}\n"+
	                 "</script>");
	         out.println("<button type = \"button\" class = \"button\" onclick=\"toCheckout()\">Checkout</button>");
	         out.println("</html>");
	         rs.close();
	         statement.close();
         
         
	    }catch (Exception e) {
 		/*
 		 * After you deploy the WAR file through tomcat manager webpage,
 		 *   there's no console to see the print messages.
 		 * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
 		 * 
 		 * To view the last n lines (for example, 100 lines) of messages you can use:
 		 *   tail -100 catalina.out
 		 * This can help you debug your program after deploying it on AWS.
 		 */
 		e.printStackTrace();
 		out.println("<body>");
 		out.println("<p>");
 		out.println("Exception in doGet: " + e.getMessage());
 		out.println("</p>");
 		out.print("</body>");
 	}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
