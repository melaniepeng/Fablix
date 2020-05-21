

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class SingleSServelet
 */
@WebServlet("/SingleSServelet")
public class SingleSServelet extends HttpServlet {
	private static final long serialVersionUID = 3L;
       
	// Create a dataSource which registered in web.xml
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");  // Response mime type
		long startquerytime = System.nanoTime();
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
        out.println("<div><h1> Single Star</h1></div>");
        String a = "";
		if(cartArray == null)
        {
        	System.out.println("NOT WORKING!"+cartArray);
        }
        else {
			for(int i = 0; i < cartArray.length; i++)
			{
				System.out.println("Starcart: " + cartArray[i]);
				if(cartArray[i].equals("") == false)
				{
					if(i == cartArray.length-1)
						a += cartArray[i];
					else
						a += cartArray[i] + ",";
				}
			}
		}
        out.print("<script type=\"text/javascript\">\n" +
        		"function toCheckout(){ \n"+
                "    window.location.href=\"ShoppingServlet?cartArray=" + a +"\";\n" + 
                "}\n"+
                "</script>");
		try 
		{
			// Get a connection from dataSource
			Context initCtx = new InitialContext();

	        Context envCtx = (Context) initCtx.lookup("java:comp/env");
	        
	        // Look up our data source
	        DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");

	        // the following commented lines are direct connections without pooling
	        //Class.forName("org.gjt.mm.mysql.Driver");
	        //Class.forName("com.mysql.jdbc.Driver").newInstance();
	        //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	   

	        Connection dbcon = ds.getConnection();
	       
	    	Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Declare our statement

			// Construct a query with parameter represented by "?"
			String query = "select s.id, s.name, s.birthYear, m.id as moviesID, m.title\n" + 
							"from movies m, stars_in_movies sm, stars s\n" + 
							"where s.id = ? and m.id = sm.movieId and sm.starId = s.id";
			
			java.sql.PreparedStatement Statement =dbcon.prepareStatement(query);
			Statement.setString(1, id);
			long startJDBCtime = System.nanoTime();
			ResultSet rs = Statement.executeQuery();
			long endJDBCtime = System.nanoTime();

			// Declare our statement

		   // Set the parameter represented by "?" in the query to the id we get from url,
		   // num 1 indicates the first "?" in the query
			//statement.setString(1, id);
			
			// Perform the query
			
			
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
			out.println("<table bgcolor= #ccffef cellspacing=\"0\">");
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
				out.print("<td><a href = \"SingleMServelet?id="+ entry.getValue() + "&title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+page+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+ "&gid=" + gid +"&cartArray="+ a +"\">"+entry.getKey()+"</a></td>");
	        }
			out.println("</table>");
			out.print("</body>");
			//out.println("<div><h4><a href = \"MovieServlet?\"> Return to Movie Lists</h4></div>");
			out.println("<div><h4><a href=\"SearchServlet?title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+page+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+ "&gid=" + gid +"&cartArray="+ a +"\">Return to Movie Lists</h4></div>");
			out.println("<button type = \"button\" class = \"button\" onclick=\"toCheckout(); return false;\">Checkout</button>");
			out.println("</html>");
			rs.close();
    		Statement.close();
    		long endQueryTime = System.nanoTime();
    		File myfile = new File("text.txt");
 			if(!myfile.exists()) {
 				myfile.createNewFile();
 	         } 
 			BufferedWriter writer = new BufferedWriter(new FileWriter(myfile.getAbsolutePath(), true));
 			writer.write("SingleSServlet"+"\n");
 	         long totalJDBCtime = endJDBCtime - startJDBCtime;
 		     long totalQuerytime = endQueryTime - startquerytime;
 		     writer.write("totalQuerytime ");
 		     writer.write( Long.toString(totalQuerytime)+"\n");
 		     writer.write("totalJDBCtime ");
 		     writer.write(Long.toString(totalJDBCtime)+"\n");
 		     writer.close();
 		     
   
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
