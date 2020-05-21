
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet("/SingleMServelet") // annotation 
public class SingleMServelet extends HttpServlet {
	private static final long serialVersionUID = 2L;
	static long endJDBCtime1;
	static long startJDBCtime1;
	// Create a dataSource which registered in web.xml
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long startquerytime = System.nanoTime();
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
        System.out.print(id.isEmpty());
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
    		String query = "";
    		if(id.isEmpty()) {
    			System.out.println("sstitle"+gettitle);
    			query ="select m.id, m.title, m.year, m.director, g.name as genre, g.id as gid, s.id as starid,s.name as starname, r.rating\n" + 
    					"from (SELECT * FROM movies where title = \""+gettitle+"\") m, genres_in_movies gm, genres g, stars_in_movies sm, stars s, ratings r \n" + 
    					"where m.id = gm.movieId and gm.genreId = g.id and m.id = sm.movieId and sm.starId = s.id and r.movieId = m.id;";
    			
    		}else {
    			query = "select m.id, m.title, m.year, m.director, g.name as genre, g.id as gid, s.id as starid,s.name as starname, r.rating\n" + 
							"from movies m, genres_in_movies gm, genres g, stars_in_movies sm, stars s, ratings r\n" + 
							"where m.id =\""+id+"\"and m.id = gm.movieId and gm.genreId = g.id and m.id = sm.movieId and sm.starId = s.id and r.movieId = m.id";
    		}

			// Declare our statement

		   // Set the parameter represented by "?" in the query to the id we get from url,
		   // num 1 indicates the first "?" in the query
			//statement.setString(1, id);
			// Perform the query
    		java.sql.PreparedStatement Statement =dbcon.prepareStatement(query);
    		long startJDBCtime = System.nanoTime();
			ResultSet rs = Statement.executeQuery();
			long endJDBCtime = System.nanoTime();

			// Iterate through each row of rs
			List<List<String>> newinfo = new ArrayList<List<String>>();
			String id1="";
			String movietitle="";
			String year="";
			String director="";
			String rating="";
			String gname="";
			String sid="";
			String sname="";
			String genreid="";
			
            while (rs.next()) {
            	id1 = rs.getString("id");
            	movietitle = rs.getString("title");
            	year = rs.getString("year");
            	director = rs.getString("director");
            	rating = rs.getString("rating");
            	gname = rs.getString("genre");
            	sid = rs.getString("starid");
            	sname = rs.getString("starname");
            	genreid = rs.getString("gid");
    			newinfo.add(Arrays.asList(id1,movietitle, year, director, rating,gname,sname, sid,genreid));
    			
    		}
            if(newinfo.isEmpty()) {
            	String add ="";
    			String[] splited = gettitle.split("\\s+");
    			int i =0;
    			for(i=0; i<splited.length; i++){
    				add+="+"+splited[i]+"*";
    			 }
            	query = "select m.id, m.title, m.year, m.director, g.name as genre, g.id as gid, s.id as starid,s.name as starname, r.rating\n" + 
    					"from movies m, genres_in_movies gm, genres g, stars_in_movies sm, stars s, ratings r\n" + 
    					"where m.id = gm.movieId and gm.genreId = g.id and m.id = sm.movieId and sm.starId = s.id and m.id = r.movieId and match (title) against (\""+add+"\"in boolean mode);";
            	long startJDBCtime1 = System.nanoTime();
            	ResultSet result = Statement.executeQuery(query);
            	long endJDBCtime1 = System.nanoTime();
    			while (result.next()) {
                	id1 = result.getString("id");
                	movietitle = result.getString("title");
                	year = result.getString("year");
                	director = result.getString("director");
                	rating = result.getString("rating");
                	gname = result.getString("genre");
                	sid = result.getString("starid");
                	sname = result.getString("starname");
                	genreid = result.getString("gid");
        			newinfo.add(Arrays.asList(id1,movietitle, year, director, rating,gname,sname, sid,genreid));
        			
        		}
    			
            }
            out.println("<body>");
			out.println("<table  bgcolor= #ccffef border = \"0\" cellspacing=\"0\">");
            out.println("<tr><th>  </td>\n" +
            			"<th>Movie ID</td>\n" +
						"<th>Movie Title</th>\n" +
						"<th>Movie Year</th>\n" +
						"<th>Movie Director</th>\n" +
						"<th>Movie Rating</th>" +
						"<th colspan=\""+ 3 +"\">Movie Genre</th>\n" +
						"<th colspan=\""+ 10 +"\">Movie Stars</th></tr>\n");
            
            String tempId = "";
            int gcol = 0;
            String tempGenre = "";
            int intnum = 0; 
            Map<String,String> stars = new HashMap<String,String>();
            //System.out.print(start);
            int numResult = intnum;
            int currentNumR = 0;
            int diffMovie = 0;
            String sameId = "";
            Set <String> sameGenre = new HashSet<String>();
            for(int i = 0; i < newinfo.size(); i++)
            {
            	//out.println("<tr>");
            	if (tempId.equals(newinfo.get(i).get(0)) == false)
            	{
            		if(sameId.equals(newinfo.get(i).get(0)) == false)
            		{
            			sameId = newinfo.get(i).get(0);
            			diffMovie ++;
            		}
            		//System.out.println("start:" + start + " different:" + diffMovie + " MovieName:" + newinfo.get(i).get(1));
      
	            		if(i != 0) 
	            		{
	            			if(gcol != 3 )
	            			{
	            				for(int j = 0; j < 3-gcol; j++)
	            				{
	            					out.print("<td> </td>");
	            				}
	            			}
	            			for(Map.Entry<String, String> entry : stars.entrySet())
	            			{
	            				out.print("<td><a href = \"\" onclick = \" toStar(\'"+entry.getKey()+"\'); return false;\">" + entry.getValue() + "</a></td>");
	            				//out.print("<td><a href = \"SingleSServelet?id=" + entry.getKey() + "&title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+npage+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+"&gid="+gid+"\">" +entry.getValue()+"</a></td>");
	            			}
	            			if(stars.size() != 10 )
	            			{
	            				for(int j = 0; j < 10-stars.size(); j++)
	            				{
	            					
	            					out.print("<td> </td>");
	            				}
	            			}
	            			out.println("</tr>");
	            			stars = new HashMap<String,String>();
	            			sameGenre = new HashSet<String>();
	            		}
	         
	            		//System.out.println("different:" + diffMovie + " currentNumR:" + currentNumR + " startNum:" + start + " numResult:" + numResult);
	            		out.println("<tr>");
	            		//out.println("<td><button type=\"submit\" name=\"cartArray\" class=\"btn\" value=\""+ newinfo.get(i).get(0) +"\">Add to Cart</button></td>");
	            		//addToCart += "&cartArray=" + cartArray;
	            		out.println("<td><button type=\"button\" onclick=\" myFunction(\'" + newinfo.get(i).get(0) +"\')\">Add to Cart</button></td>");
	            		//out.print("<form><input type=\"button\" class = \"btn\" name = \"cartArray\"value=\""+newinfo.get(i).get(0)+"\"></form>");
	            		//System.out.println(addToCart);
		            	out.println("<td>"+newinfo.get(i).get(0)+"</td>");
		            	out.print("<td><a href = \"\" onclick = \" toMovie(\'"+newinfo.get(i).get(0)+"\'); return false;\">" + newinfo.get(i).get(1) + "</a></td>");
						//out.println("<td><a href = \"SingleMServelet?id="+ newinfo.get(i).get(0) + "&title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+npage+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+"&gid="+gid+"&cartArray="+ a +"\">"+newinfo.get(i).get(1)+"</a></td>");
						out.println("<td>"+newinfo.get(i).get(2)+"</td>");
						out.println("<td>"+newinfo.get(i).get(3)+"</td>");
						out.println("<td>"+newinfo.get(i).get(4)+"</td>");
						out.print("<td><a href = \"\" onclick = \" toGenre(\'"+newinfo.get(i).get(8)+"\'); return false;\">" + newinfo.get(i).get(5) + "</a></td>");
						//out.print("<td><a href = \"SearchServlet?title=&year=&director=&star=&num=10&type=title&direction=ASC&titlestart=&page=Search&gid="+newinfo.get(i).get(8)+"&cartArray="+ a +"\">" + newinfo.get(i).get(5) +"</a></td>");
						tempId = newinfo.get(i).get(0);
						tempGenre = newinfo.get(i).get(5);
						sameGenre.add(newinfo.get(i).get(5));
						gcol = 1;
						for(int j = 0; j < newinfo.size(); j++)
						{
							if(tempId.equals(newinfo.get(j).get(0)))
							{
								stars.put(newinfo.get(j).get(7), newinfo.get(j).get(6));
							}
						}
					//previous = 
            		}
            	
				else
				{
					if(tempGenre.equals(newinfo.get(i).get(5)) == false && sameGenre.contains(newinfo.get(i).get(5)) == false)
					{
						//System.out.println();
						//System.out.println("tempGenre: " + tempGenre+" getGenre: "+newinfo.get(i).get(5));
						gcol++;
						out.print("<td><a href = \"\" onclick = \" toGenre(\'"+newinfo.get(i).get(8)+"\'); return false;\">" + newinfo.get(i).get(5) + "</a></td>");
						//out.print("<td><a href = \"SearchServlet?title=&year=&director=&star=&num=10&type=title&direction=ASC&titlestart=&page=Search&gid="+newinfo.get(i).get(8)+"&cartArray="+ a +"\">" + newinfo.get(i).get(5) +"</a></td>");
						sameGenre.add(newinfo.get(i).get(5));
						tempGenre = newinfo.get(i).get(5);
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
            for(Map.Entry<String, String> entry : stars.entrySet())
			{
            	out.print("<td><a href = \"\" onclick = \" toStar(\'"+entry.getKey()+"\'); return false;\">" + entry.getValue() + "</a></td>");
				//out.print("<td><a href = \"SingleSServelet?id=" + entry.getKey() + "&title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+npage+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+"&gid="+gid+"&cartArray="+ a +"\">" +entry.getValue()+"</a></td>");
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
    		out.println("<div><h4><a href = \"\" onclick = \" toSearch();return false;\">Return to Movie Lists</a></h4></div>");
    		out.println("<div><h4><a href = \"Main.html\">Return to Search bar</a></h4></div>");
			out.println("<button type = \"button\" class = \"button\" onclick=\"toCheckout()\">Checkout</button>");
    		out.println("</body>");
    		rs.close();
    		Statement.close();
    		long endQueryTime = System.nanoTime();

    		File myfile = new File("text.txt");
			if(!myfile.exists()) {
				myfile.createNewFile();
	         } 
			BufferedWriter writer = new BufferedWriter(new FileWriter(myfile.getAbsolutePath(), true));
			writer.write("SingleMServlet"+"\n");
			Map<String, String> info = new HashMap<>();
	         long totalJDBCtime = endJDBCtime - startJDBCtime;
	         long totalJDBCtime1 = endJDBCtime1 - startJDBCtime1;
	         info.put("totalJDBCtime", Long.toString(totalJDBCtime));
	         info.put("totalJDBCtime1", Long.toString(totalJDBCtime1));
		     long totalQuerytime = endQueryTime - startquerytime;
		     writer.write("totalQuerytime ");
		     writer.write( Long.toString(totalQuerytime)+"\n");
		     while (info.values().remove(""));
		     for(Map.Entry<String, String> entry : info.entrySet()) { 
	        	 writer.write("totalJDBCtime ");
	        	 writer.write(entry.getValue()+"\n");
	         }
	         
		    
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
}