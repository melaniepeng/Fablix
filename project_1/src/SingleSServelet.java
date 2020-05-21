

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class SingleSServelet
 */
@WebServlet("/SingleSServelet")
public class SingleSServelet extends HttpServlet {
	private static final long serialVersionUID = 3L;
       
	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
	String loginUser = "mytestuser";
	String loginPasswd = "mypassword";
	String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");  // Response mime type
		
		// Retrieve parameter id from url request.
		String id = request.getParameter("id");

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		out.println("<html>");
        out.println("<head>\n" + 
        			"<title>Single Star</title>" + 
        			"<style>table, th, td {border: 1px solid black;\n" + 
        								   "border-collapse: collapse;}" + 
        					"th, td {padding: 5px;\n" + 
        							"text-align: left;} body {background-color:#F7DEDE;front-family: Apple Chancery;\"\n" + 
        							"	        		+ \"}" + 
        			"</style>" + 
        			"</head>");
        out.println("<div><h1> Single Star</h1></div>");
		try 
		{
			// Get a connection from dataSource
			Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		
            // Declare our statement
            Statement statement = connection.createStatement();

			// Construct a query with parameter represented by "?"
			String query = "select s.id, s.name, s.birthYear, m.id as moviesID, m.title\n" + 
							"from movies m, stars_in_movies sm, stars s\n" + 
							"where s.id = \""+ id + "\" and m.id = sm.movieId and sm.starId = s.id";

			// Declare our statement

		   // Set the parameter represented by "?" in the query to the id we get from url,
		   // num 1 indicates the first "?" in the query
			//statement.setString(1, id);
			
			// Perform the query
			ResultSet rs = statement.executeQuery(query);
			
			// Iterate through each row of rs
			String name = "";
			String byear = "";
			//ArrayList<String> movieId = new ArrayList<String>();
			//ArrayList<String> movieName = new ArrayList<String>();
			Map<String,String> movieIdName = new HashMap<String,String>();
			while (rs.next()) {
				name = rs.getString("name");
				byear = rs.getString("birthYear");
				//movieId.add(rs.getString("moviesID"));
				//movieName.add(rs.getString("title"));
				movieIdName.put(rs.getString("title"), rs.getString("moviesID"));
			}
			out.println("<body>");
			out.println("<table cellspacing=\"8\">");
			out.println("<tr><th>Star Name</td>\n" +
							"<th>Star Birth Year</th>" + 
							"<th colspan=\""+ movieIdName.size() +"\">Movie(s) Star In</th></tr>");
			out.println("<td>"+name+"</td>");
			if (byear == null)
			{
				out.println("<td>N/A</td>");
			}
			else
			{
				out.println("<td>"+byear+"</td>");
			}
			/*for(int i = 0; i < movieName.size(); i++)
			{
				out.print("<td><a href = \"SingleMServelet?id="+ movieId.get(i) +"\">"+movieName.get(i)+"</a></td>");
			}*/
			for (Map.Entry<String,String> entry : movieIdName.entrySet())
	        {
				out.print("<td><a href = \"SingleMServelet?id="+ entry.getValue() +"\">"+entry.getKey()+"</a></td>");
	        }
			out.println("</table>");
			out.print("</body>");
			out.println("<div><h4><a href = \"MovieServlet?\"> Return to Movie Lists</h4></div>");
			out.println("</html>");
			rs.close();
    		statement.close();
   
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
