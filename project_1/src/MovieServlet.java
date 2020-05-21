

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

//import com.google.gson.JsonArray;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Servlet implementation class MovieServlet
 */
@WebServlet("/MovieServlet")
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public String title = "";
	 @Resource(name = "jdbc/moviedb")
	  private DataSource dataSource;
	 String loginUser = "mytestuser";
     String loginPasswd = "mypassword";
     String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
	 
    /**
     * @see HttpServlet#HttpServlet()
     */
        // TODO Auto-generated constructor stub

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html"); 
		PrintWriter out = response.getWriter();
	        out.println("<html>");
	        out.println("<head><title>Projec_1</title></head>");
	        out.println("<style>\n" + 
	        					"body {background-color:#F7DEDE;front-family: Apple Chancery; "
	        					+ "}\n" + 
	        					"h1   {color: black;\n" + 
	        					"		padding-up: 30px;\n" + 
	        					"		padding-left: 30%}\n" + 
	        					"h2 {color: black;\n" + 
	        					"		padding-left: 500px}\n" +"table{padding-left:2000px"
	        					+ "font-size: 130px;}"
	        					+ "table{padding-left:500px}</style>" + 
	        			"<style>table, th, td {border: 1px solid black;\n" + 
	        					"border-collapse: collapse;}" + 
							   "th, td {padding: 5px;\n" + 
						"text-align: left;}" + "</style>");
	        
	        out.println("<div>\n" + 
	        		"	<h1> Welcome to the Kathy and Hui Min project_1</h1>\n" + 
	        		"	<h2> List of top 20 movies. </h2>	\n" + 
	        		"</div>");
	 
	        try {
	        	Class.forName("com.mysql.jdbc.Driver").newInstance();
        		// create database connection
        		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        		
	            // Declare our statement
	            Statement statement = connection.createStatement();

	            String query = "select m.id, m.title, m.year, m.director, mid.rating, g.name as gname, s.name as sname\n" + 
	            				"from (select movieId, rating from ratings order by rating desc limit 20) mid, movies m, genres_in_movies gm, genres g, stars_in_movies sm, stars s\n" + 
	            				"where mid.movieId = m.id and gm.movieId = mid.movieId and gm.genreId = g.id and mid.movieId = sm.movieId and s.id = sm.starId";
	            
	            // Perform the query
	            ResultSet rs = statement.executeQuery(query);
	          

	            // Iterate through each row of rs
	            
	            List<List<String>> info = new ArrayList<List<String>>();
	            while (rs.next()) {
	            	String id = rs.getString("id");
	            	String movietitle = rs.getString("title");
	            	String year = rs.getString("year");
	            	String director = rs.getString("director");
	            	String rating = rs.getString("rating");
	            	String gname = rs.getString("gname");
	            	String sname = rs.getString("sname");
        			info.add(Arrays.asList(id,movietitle, year, director, rating,gname,sname));
        		}
	            out.println("<body>");
				out.println("<table border = \"0\" cellspacing=\"8\">");
	            out.println("<tr><th>Movie ID</td>\n" +
							"<th>Movie Title</th>\n" +
							"<th>Movie Year</th>\n" +
							"<th>Movie Director</th>\n" +
							"<th>Movie Rating</th>" +
							"<th colspan=\""+ 3 +"\">Movie Genre</th>\n" +
							"<th colspan=\""+ 10 +"\">Movie Stars</th></tr>\n");
	            
	            String tempId = "";
	            int gcol = 1;
	            String tempGenre = "";
	            Set<String> stars = new HashSet<String>();
	            for(int i = 0; i < info.size(); i++)
	            {
	            	//out.println("<tr>");
	            	if (tempId.equals(info.get(i).get(0)) == false)
	            	{
	            		if(i != 0) 
	            		{
	            			if(gcol != 3 )
	            			{
	            				for(int j = 0; j < 3-gcol; j++)
	            				{
	            					out.print("<td> </td>");
	            				}
	            			}
	            			for(String s: stars)
	            			{
	            				out.print("<td>"+s+"</td>");
	            			}
	            			if(stars.size() != 10 )
	            			{
	            				for(int j = 0; j < 10-stars.size(); j++)
	            				{
	            					out.print("<td> </td>");
	            				}
	            			}
	            			stars = new HashSet<String>();
	            		}
	            		out.println("</tr>");
	            		out.println("<tr>");
		            	out.println("<td bgcolor = >"+info.get(i).get(0)+"</td>");
						out.println("<td><a href = \"SingleMServelet?id="+ info.get(i).get(0) +"\">"+info.get(i).get(1)+"</td>");
						out.println("<td>"+info.get(i).get(2)+"</td>");
						out.println("<td>"+info.get(i).get(3)+"</td>");
						out.println("<td>"+info.get(i).get(4)+"</td>");
						out.print("<td>"+info.get(i).get(5)+"</td>");
						tempId = info.get(i).get(0);
						tempGenre = info.get(i).get(5);
						gcol = 1;
						for(int j = 0; j < info.size(); j++)
						{
							if(tempId.equals(info.get(j).get(0)))
							{
								stars.add(info.get(j).get(6));
							}
						}
						//previous = 
	            	}
					else
					{
						if(tempGenre.equals(info.get(i).get(5)) == false)
						{
							gcol++;
							out.print("<td>"+info.get(i).get(5)+"</td>");
							tempGenre = info.get(i).get(5);
						}
						//scol++;
						//out.print("<td>"+info.get(i).get(6)+"</td>");
					}	
	            }
	            if(gcol != 3 )
    			{
    				for(int j = 0; j < 3-gcol; j++)
    				{
    					out.print("<td> </td>");
    				}
    			}
	            for(String s: stars)
    			{
    				out.print("<td>"+s+"</td>");
    			}
	            if(stars.size() != 10 )
    			{
    				for(int j = 0; j < 10-stars.size(); j++)
    				{
    					out.print("<td> </td>");
    				}
    			}
	            out.print("</tr>");
				
	            
        		out.println("</table>");
        		out.println("</body>");
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
        
        out.println("</html>");
        out.close();
        
	}

		
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
