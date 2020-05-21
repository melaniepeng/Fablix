
import javax.annotation.Resource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet("/SingleMServelet") // annotation 
public class SingleMServelet extends HttpServlet {
	private static final long serialVersionUID = 2L;

	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
	String loginUser = "mytestuser";
    String loginPasswd = "mypassword";
    String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");  // Response mime type

		// Retrieve parameter id from url request.
		String id = request.getParameter("id");

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		out.println("<html>");
        out.println("<head>\n" + 
        			"<title>Single Movie</title>" + 
        			"<style>table, th, td {border: 1px solid black;\n" + 
        								   "border-collapse: collapse;}" + 
        					"th, td {padding: 5px;\n" + 
        							"text-align: left;} body {background-color:#F7DEDE;front-family: Apple Chancery;\"\n" + 
        							"	        		+ \"}" + 
        					
        			"</style>" + 
        			"</head>");
        out.println("<div><h1> Single Movie</h1></div>");
		try {
			// Get a connection from dataSource
			Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		
            // Declare our statement
            Statement statement = connection.createStatement();

			// Construct a query with parameter represented by "?"
			String query = "select m.id, m.title, m.year, m.director, g.name as genre, s.id as starid,s.name as starname, r.rating\n" + 
							"from movies m, genres_in_movies gm, genres g, stars_in_movies sm, stars s, ratings r\n" + 
							"where m.id = \""+ id + "\" and m.id = gm.movieId and gm.genreId = g.id and m.id = sm.movieId and sm.starId = s.id and r.movieId = m.id";

			// Declare our statement

		   // Set the parameter represented by "?" in the query to the id we get from url,
		   // num 1 indicates the first "?" in the query
			//statement.setString(1, id);
			// Perform the query
			ResultSet rs = statement.executeQuery(query);
			// Iterate through each row of rs
			String mid = "";
			String title = "";
			String year = "";
			String director = "";
			Set<String> uniqueGenre = new HashSet<String>();
			String rating = "";
			Map<String,String> idName = new HashMap<String,String>();
			while (rs.next()) {
				mid = rs.getString("id");
				title = rs.getString("title");
				year = rs.getString("year");
				director = rs.getString("director");
				uniqueGenre.add(rs.getString("genre"));
				idName.put(rs.getString("starname"), rs.getString("starid"));
				rating = rs.getString("rating");
			}
			out.println("<body>");
			out.println("<table border = \"0\" cellspacing=\"8\">");
			out.println("<tr><th>Movie ID</td>\n" +
							"<th>Movie Title</th>\n" +
							"<th>Movie Year</th>\n" +
							"<th>Movie Director</th>\n" +
							"<th colspan=\""+ uniqueGenre.size() +"\">Movie Genre</th>\n" +
							"<th colspan=\""+ idName.size() +"\">Movie Stars</th>\n" +
							"<th>Movie Rating</th></tr>");
			out.println("<td bgcolor = >"+mid+"</td>");
			out.println("<td>"+title+"</td>");
			out.println("<td>"+year+"</td>");
			out.println("<td>"+director+"</td>");
			for(String g: uniqueGenre)
			{
				out.print("<td>"+g+"</td>");
			}
			for (Map.Entry<String,String> entry : idName.entrySet())
	        {
	            out.print("<td><a href = \"SingleSServelet?id="+entry.getValue()+"\">"+entry.getKey()+"</a></td>");
	        }
			out.println("<td>"+rating+"</td>");
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
}