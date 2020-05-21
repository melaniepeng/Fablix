package project_1_try;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// this annotation maps this Java Servlet Class to a URL
@WebServlet("/stars")
public class t extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public t() {
        super();
    }
    public String title="";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // change this to your own mysql username and password
        String loginUser = "root";
        String loginPasswd = "Kathy981294";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
        // set response mime type
        response.setContentType("text/html"); 

        // get the printwriter for writing response
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Projec_1</title></head>");
        out.println("<style>\n" + 
        		"body {background-color:#F7DEDE;}\n" + 
        		"h1   {color: black;\n" + 
        		"		front-family: Apple Chancery;\n" + 
        		"		padding-up: 30px;\n" + 
        		"		padding-left: 30%}\n" + 
        		"h2 {color: black;\n" + 
        		"		front-family: Apple Chancery;\n" + 
        		"		padding-left: 550px}\n" + 
        		"</style>");
        out.println("<div>\n" + 
        		"	<h1> Welcome to the Kathy and Hui Min project_1</h1>\n" + 
        		"	<h2> Which part you want to choose? Movie or star?</h2>	\n" + 
        		"</div>");
        out.println("<script> "
        		+ "function to(a){ \n" +  
        		" window.location.href=\"NewFile3.html?valus=\"+a;}</script>");
        
        try {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();
        		// create database connection
        		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        		// declare statement
        		Statement statement = connection.createStatement();
        		// prepare query
        		String query = "select m.title\n" + 
        				"from\n" + 
        				"(select movieId, rating from ratings order by rating desc limit 20) mid, movies m\n" + 
        				"where m.id = mid.movieId\n" + 
        				"";
        		// execute query
        		ResultSet resultSet = statement.executeQuery(query);
        		out.println("<body>");
        		
        		// add table header row
        		out.println("<tr>");
        		out.println("<td>title</td>");
        		out.println("</tr>");
        		
        		// add a row for every star result
        		while (resultSet.next()) {
        			// get a star from result set
        			String movietitle = resultSet.getString("title");
        			out.print("<table>");
        			out.println("<tr>");
        			title=movietitle.replace(" ","_");
        			out.println("<td id = \"open\"><th><a href = \"javascript:void(0)\" onclick=to(\""+ title +"\")>"+movietitle+"</a></th></td>");
        			
        		
        			out.println("</tr>");
        		}
        		
        		out.println("</table>");
        		
        		out.println("</body>");
        		
        		resultSet.close();
        		statement.close();
        		connection.close();
        		
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
        		e.printStackTrace();
        		
        		out.println("<body>");
        		out.println("<p>");
        		out.println("Exception in doGet: " + e.getMessage());
        		out.println("</p>");
        		out.print("</body>");
        }
        
        out.println("</html>");
        out.close();
        
	}


}
