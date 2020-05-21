
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
		String gettitle = request.getParameter("title");
        String getyear = request.getParameter("year");
        String getdirector = request.getParameter("director");
        String getstar = request.getParameter("star");
        String getnum = request.getParameter("num");
        String type = request.getParameter("type");
        String direction = request.getParameter("direction");
        String page = request.getParameter("page");
        String titlestart = request.getParameter("titlestart");
        String gid = request.getParameter("gid");
        String[] cartArray = request.getParameterValues("cartArray");
        
		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><style>\n" + 
				"body{background-image: url(\"bg-2.jpg\");\n" + 
				" background-repeat:no-repeat;\n" + 
				"front-family: Apple Chancery;"
				+ "}\n" + 
				"h1   {color: black;\n" + 
				"		padding-up: 30px;\n" + 
				"		padding-left: 40%}\n" +  
		"<style>\n" + 
			"table {\n" + 
			"  border-collapse: collapse;\n" + 
			"  border-spacing: 0;\n" + 
			"  width: 100%;"
			+ 
			"}\n" + 
			"\n" + 
			"th, td {\n" + 
			"  text-align: left;\n" + 
			"  padding: 16px;\n" + 
			"}\n" + 
			"\n" + 
			"tr:nth-child(even) {\n" + 
			"  background-color: white\n" + 
			"}" + "a {\n" + 
				"  text-decoration: none;\n" + 
				"  display: inline-block;\n" + 
				"  padding: 8px 16px;"
				+ "\n" + 
				"}\n" + 
				"\n" + 
				"a:hover {\n" + 
				"  background-color: #ddd;\n" + 
				"  color: black;\n" + "background-color: black;\n"+
				"  color: white;"+
				"}\n" +".button:hover {\n" + 
						"  opacity: 1;\n" + 
						"}\n" + 
						".button {\n" + 
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
				
		"</style></head>");
        out.println("<div><h1> Single Movie</h1></div>");
        String a = "";
        System.out.println("Searchcart: "+cartArray);
        if(cartArray == null)
        {
        	System.out.println("NOT WORKING!"+cartArray);
        }
        else {
        /*if(cartArray.length != 0 ||Arrays.asList(cartArray).contains("null") == false)
        {*/ 
			for(int i = 0; i < cartArray.length; i++)
			{
				System.out.println("Searchcart: "+cartArray[i] + "\t" +cartArray[i].getClass().getName() );
				if(cartArray[i].equals("") == false)
				{
					if(i >= cartArray.length-1)
						a += cartArray[i];
					else
						a += cartArray[i] + ",";
				}
			}
        }
        out.println("<script>\n" + 
        		"var addToCart = [];\n" +
        		"function myFunction(movieid) {\n" + 
        		"	addToCart.push(movieid);\n" + 
        		"	console.log(addToCart);\n"+
        		//"	to();\n"+
        		"}\n" + 
        		"</script>"+
        		"<script type=\"text/javascript\">\n" +
        		"function toCheckout(){ \n"+
        		"    var getval = addToCart;\n" + 
        		"	console.log(getval);\n"+
        		"    window.location.href=\"ShoppingServlet?cartArray=" + a +"&cartArray=\" + getval;\n" + 
        		"}\n"+
        		"function toSearch(){ \n"+
        		"    var getval = addToCart;\n" + 
        		"	console.log(getval);\n"+
        		"    window.location.href=\"SearchServlet?title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+page+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+"&gid="+gid+"&cartArray=" + a +"&cartArray=\"+addToCart;\n" + 
        		"}\n"+
        		"function toStar(starid){ \n"+
        		"	console.log(starid);\n"+
        		"    window.location.href=\"SingleSServelet?id=\"+starid+\"&title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+page+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+"&gid="+gid+ "&cartArray=" + a +"&cartArray=\"+addToCart;\n" + 
        		"}\n"+
        		"</script>");
        
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
			out.println("<table bgcolor= #ccffef border = \"0\" cellspacing=\"0\">");
			out.println("<tr><th>  </td>\n" +
					 	"<th>Movie ID</td>\n" +
							"<th>Movie Title</th>\n" +
							"<th>Movie Year</th>\n" +
							"<th>Movie Director</th>\n" +
							"<th colspan=\""+ uniqueGenre.size() +"\">Movie Genre</th>\n" +
							"<th colspan=\""+ idName.size() +"\">Movie Stars</th>\n" +
							"<th>Movie Rating</th></tr>");
			out.println("<td><button type=\"button\" onclick=\" myFunction(\'" + mid +"\')\">Add to Cart</button></td>");
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
				out.print("<td><a href = \"\" onclick = \" toStar(\'"+entry.getValue()+"\'); return false;\">" + entry.getKey () + "</a></td>");
	            //out.print("<td><a href = \"SingleSServelet?id="+entry.getValue()+ "&title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+page+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+ "&gid=" + gid +"&cartArray="+ a +"\">"+entry.getKey()+"</a></td>");
	        }
			out.println("<td>"+rating+"</td>");
			out.println("</table>");
			out.print("</body>");
			out.println("<div><h4><a href = \"\" onclick = \" toSearch();return false;\">Return to Movie Lists</a></h4></div>");
			out.println("<button type = \"button\" class = \"button\" onclick=\"toCheckout()\">Checkout</button>");
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