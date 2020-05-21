
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

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
 * Servlet implementation class InsertMovieServlet
 */
@WebServlet("/InsertMovieServlet")
public class InsertMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	static long endJDBCtime4;
	static long startJDBCtime4;
	static long endJDBCtime5;
	static long startJDBCtime5;
	static long endJDBCtime6;
	static long startJDBCtime6;
	static long endJDBCtime7;
	static long startJDBCtime7;
	static long endJDBCtime8;
	static long startJDBCtime8;
	static long endJDBCtime9;
	static long startJDBCtime9;
	static long endJDBCtime10;
	static long startJDBCtime10;
	static long endJDBCtime11;
	static long startJDBCtime11;
	static long endJDBCtime12;
	static long startJDBCtime12;
    public InsertMovieServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @SuppressWarnings("resource")
	private static String add_movie(String movietitle, int movieyear, String moviedirector, String star, String genre, float rating) throws Exception{
    	
    	long startquerytime = System.nanoTime();
    	Context initCtx = new InitialContext();

        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        
        // Look up our data source
        DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");

        // the following commented lines are direct connections without pooling
        //Class.forName("org.gjt.mm.mysql.Driver");
        //Class.forName("com.mysql.jdbc.Driver").newInstance();
        //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
   

        Connection dbcon = ds.getConnection();
       
    	Class.forName("com.mysql.jdbc.Driver").newInstance();
 		// create database connection
		String query = "";
		String newid = "";
		//find the max id and create a new id after the max id
		query = "select max(id) as id from movies";
		java.sql.PreparedStatement Statement =dbcon.prepareStatement(query);
		long startJDBCtime = System.nanoTime();
		ResultSet moviers = Statement.executeQuery();
		long endJDBCtime = System.nanoTime();
		String movieid = "";
		while (moviers.next()) {
			movieid = moviers.getString("id");
		}
		int movienum = Integer.parseInt(movieid.replace("tt",""))+1;
		newid = "tt"+ Integer.toString(movienum);
		//insert movie into db
		query = "INSERT INTO movies (id, title, year, director) VALUES (?,?,?,?)";
		Statement =dbcon.prepareStatement(query);
		Statement.setString(1, newid);
		Statement.setString(2, movietitle);
		Statement.setInt(3, movieyear);
		Statement.setString(4, moviedirector);
		long startJDBCtime1 = System.nanoTime();
		Statement.executeUpdate();
		long endJDBCtime1 = System.nanoTime();
	
		
		query = "INSERT INTO ratings (movieId, rating, numVotes) VALUES (?,?,?)";
		Statement =dbcon.prepareStatement(query);
		Statement.setString(1, newid);
		Statement.setFloat(2, rating);
		Statement.setInt(3,0);
		long startJDBCtime2 = System.nanoTime();
		Statement.executeUpdate();
		long endJDBCtime2 = System.nanoTime();
		
		
		//find if the star exists
		query = "select * from stars where name = ?;";
		Statement =dbcon.prepareStatement(query);
		Statement.setString(1, star);
		long startJDBCtime3 = System.nanoTime();
		ResultSet samestarrs = Statement.executeQuery();
		long endJDBCtime3 = System.nanoTime();
		if(samestarrs.next())
		{
			String samestarid = "";
			samestarrs.beforeFirst();
			while(samestarrs.next())
			{
				samestarid = samestarrs.getString("id");
			}
			query = "INSERT INTO  stars_in_movies (starId, movieId) VALUES (?,?)";
			Statement =dbcon.prepareStatement(query);
			Statement.setString(1, samestarid);
			Statement.setString(2, newid);
			startJDBCtime4 = System.nanoTime();
			Statement.executeUpdate();
			endJDBCtime4 = System.nanoTime();
			
		}
		else
		{	
			query = "SELECT * FROM moviedb.stars\n" + 
    				"order by id Desc\n" + 
    				"limit 1;";
			Statement =dbcon.prepareStatement(query);
			startJDBCtime12= System.nanoTime();
			ResultSet starrs = Statement.executeQuery();
			endJDBCtime12 = System.nanoTime();
			String starid = "";
			starrs.beforeFirst();
			while (starrs.next()) {
				starid = starrs.getString("id");
			}
			int starnum = Integer.parseInt(starid.replace("nm",""))+1;
			String newstarid = "nm"+ Integer.toString(starnum);
			query = "INSERT INTO  stars (id, name,birthYear) VALUES (?,?,?)";
			Statement =dbcon.prepareStatement(query);
			Statement.setString(1, newstarid);
			Statement.setString(2, star);
			Statement.setNull(3, Types.INTEGER);
			startJDBCtime5 = System.nanoTime();
			Statement.executeUpdate();
			endJDBCtime5 = System.nanoTime();

			query = "INSERT INTO  stars_in_movies (starId, movieId) VALUES (?,?)";
			Statement =dbcon.prepareStatement(query);
			Statement.setString(1, newstarid);
			Statement.setString(2, newid);
		    startJDBCtime6 = System.nanoTime();
			Statement.executeUpdate();
			endJDBCtime6 = System.nanoTime();

		}
		
		//find if genre exists
		query = "select * from genres where name = ?;";
		Statement =dbcon.prepareStatement(query);
		Statement.setString(1, genre);
		startJDBCtime7 = System.nanoTime();
		ResultSet samegenrers = Statement.executeQuery();
		endJDBCtime7 = System.nanoTime();
		if(samegenrers.next())
		{
			int samegenreid = 0;
			samegenrers.beforeFirst();
			while(samegenrers.next())
			{
				samegenreid = samegenrers.getInt("id");
			}
			query = "INSERT INTO  genres_in_movies (genreid, movieid) VALUES (?,?)";
			Statement =dbcon.prepareStatement(query);
			Statement.setInt(1, samegenreid);
			Statement.setString(2, newid);
			startJDBCtime8 = System.nanoTime();
			Statement.executeUpdate();
			endJDBCtime8 = System.nanoTime();
			
		}
		else
		{
			query = "SELECT * FROM moviedb.genres\n" + 
    				"order by id Desc\n" + 
    				"limit 1;";
			Statement = dbcon.prepareStatement(query);
			startJDBCtime9 = System.nanoTime();
			ResultSet genrers = Statement.executeQuery();
			endJDBCtime9 = System.nanoTime();
			int genreid = 0;
			genrers.beforeFirst();
			while (genrers.next()) {
				genreid = genrers.getInt("id");
			}
			int genrenum = genreid+1;
			query = "INSERT INTO  genres (id, name) VALUES (?,?)";
			Statement =dbcon.prepareStatement(query);
			Statement.setInt(1, genrenum);
			Statement.setString(2, genre);
			startJDBCtime10 = System.nanoTime();
			Statement.executeUpdate();
			endJDBCtime10= System.nanoTime();
			query = "INSERT INTO  genres_in_movies (genreId, movieId) VALUES (?,?)";
			Statement =dbcon.prepareStatement(query);
			Statement.setInt(1, genrenum);
			Statement.setString(2, newid);
			startJDBCtime11 = System.nanoTime();
			Statement.executeUpdate();
			endJDBCtime11 = System.nanoTime();
	
		}
		
		
		long endQueryTime = System.nanoTime();
		File myfile = new File("text.txt");
		if(!myfile.exists()) {
			myfile.createNewFile();
         } 
		BufferedWriter writer = new BufferedWriter(new FileWriter(myfile.getAbsolutePath(), true));
		writer.write("InsertMovieServlet"+"\n");
		Map<String, String> info = new HashMap<>();
         long totalJDBCtime = endJDBCtime - startJDBCtime;
         long totalJDBCtime1 = endJDBCtime1 - startJDBCtime1;
         long totalJDBCtime2 = endJDBCtime2 - startJDBCtime2;
         long totalJDBCtime3 = endJDBCtime3 - startJDBCtime3;
         long totalJDBCtime4 = endJDBCtime4 - startJDBCtime4;
         long totalJDBCtime5 = endJDBCtime5 - startJDBCtime5;
         long totalJDBCtime6 = endJDBCtime6 - startJDBCtime6;
         long totalJDBCtime7 = endJDBCtime7 - startJDBCtime7;
         long totalJDBCtime8 = endJDBCtime8 - startJDBCtime8;
         long totalJDBCtime9 = endJDBCtime9 - startJDBCtime9;
         long totalJDBCtime10 = endJDBCtime10 - startJDBCtime10;
         long totalJDBCtime11 = endJDBCtime11 - startJDBCtime11;
         long totalJDBCtime12 = endJDBCtime12 - startJDBCtime12;
         info.put("totalJDBCtime", Long.toString(totalJDBCtime));
         info.put("totalJDBCtime1", Long.toString(totalJDBCtime1));
         info.put("totalJDBCtime2", Long.toString(totalJDBCtime2));
         info.put("totalJDBCtime3", Long.toString(totalJDBCtime3));
         info.put("totalJDBCtime4", Long.toString(totalJDBCtime4));
         info.put("totalJDBCtime5", Long.toString(totalJDBCtime5));
         info.put("totalJDBCtime6", Long.toString(totalJDBCtime6));
         info.put("totalJDBCtime7", Long.toString(totalJDBCtime7));
         info.put("totalJDBCtime8", Long.toString(totalJDBCtime8));
         info.put("totalJDBCtime9", Long.toString(totalJDBCtime9));
         info.put("totalJDBCtime10", Long.toString(totalJDBCtime10));
         info.put("totalJDBCtime11", Long.toString(totalJDBCtime11));
         info.put("totalJDBCtime12", Long.toString(totalJDBCtime12));
         while (info.values().remove(""));
         long totalQuerytime = endQueryTime - startquerytime;
	     writer.write("totalQuerytime ");
	     writer.write( Long.toString(totalQuerytime)+"\n");
         for(Map.Entry<String, String> entry : info.entrySet()) { 
        	 writer.write("totalJDBCtime ");
        	 writer.write(entry.getValue()+"\n");
         }
         
	   
	     writer.close();
	     
		
    	return newid;
    }
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");  // Response mime type
		String moviename = request.getParameter("moviename").toLowerCase();
		String year = request.getParameter("year");
		String director = request.getParameter("director").toLowerCase();
		String starname = request.getParameter("starname").toLowerCase();
		String genre = request.getParameter("genre").toLowerCase();
		String rating = request.getParameter("rating");
		
		System.out.println(moviename + "\t"+ year + "\t" + director + "\t" + starname + "\t" + genre);
		try 
		{
			long startquerytime = System.nanoTime();
			// Get a connection from dataSource
			PrintWriter out = response.getWriter();
        	Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");

            // the following commented lines are direct connections without pooling
            //Class.forName("org.gjt.mm.mysql.Driver");
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
       
            if (ds == null)
                out.println("ds is null.");

            Connection dbcon = ds.getConnection();
            if (dbcon == null)
                out.println("dbcon is null.");
            
	    	Class.forName("com.mysql.jdbc.Driver").newInstance();
	 		// create database connection
	    	String query = "SELECT * FROM moviedb.movies WHERE title = ? and year = ? and director = ?;";
    		java.sql.PreparedStatement Statement =dbcon.prepareStatement(query);
    		Statement.setString(1, moviename);
    		Statement.setInt(2, Integer.parseInt(year));
    		Statement.setString(3, director);
    		long startJDBCtime = System.nanoTime();
			ResultSet rs = Statement.executeQuery();
			long endJDBCtime = System.nanoTime();
			System.out.println(rs.next());
		
			if(rs.next() == false)
			{
				System.out.println("movie does not match");
				String movieid = add_movie(moviename, Integer.parseInt(year), director, starname, genre, Float.parseFloat(rating));
				out.println("<body>");
	     		out.println("<p>");
	     		out.println("Successfully insert the "+moviename);
	     		out.println("</p>");
	     		out.println("<h3><a href= \"Insert.html\" >BACK </a></h3>");
	     		out.print("</body>");
				//query = "INSERT INTO  movies (id, title, director) VALUES (?,?,?)";
			}
			else
			{
				System.out.println("movie match");
	     		out.println("<body>");
	     		out.println("<p>");
	     		out.println("Movie already exists");
	     		out.println("</p>");
	     		out.println("<h3><a href= \"Insert.html\" >BACK </a></h3>");
	     		out.print("</body>");
			}
			out.close();
			Statement.close();
			dbcon.close();
			long endQueryTime = System.nanoTime();
			File myfile = new File("text.txt");
				if(!myfile.exists()) {
					myfile.createNewFile();
		         } 
				BufferedWriter writer = new BufferedWriter(new FileWriter(myfile.getAbsolutePath(), true));
				writer.write("InsertMovieServlet"+"\n");
		         long totalJDBCtime = endJDBCtime - startJDBCtime;
			     long totalQuerytime = endQueryTime - startquerytime;
			     writer.write("totalQuerytime ");
			     writer.write( Long.toString(totalQuerytime)+"\n");
			     writer.write("totalJDBCtime ");
			     writer.write(Long.toString(totalJDBCtime)+"\n");
			     writer.close();
			     
		}
		
		catch (Exception e) {
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
     		PrintWriter out = response.getWriter();
     		out.println("<body>");
     		out.println("<p>");
     		out.println("Exception in doGet: " + e.getMessage());
     		out.println("</p>");
     		out.print("</body>");
     		out.close();
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
