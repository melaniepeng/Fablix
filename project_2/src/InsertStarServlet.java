

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

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
 * Servlet implementation class InsertStarServlet
 */
@WebServlet("/InsertStarServlet")
public class InsertStarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    public InsertStarServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		long startquerytime = System.nanoTime();
		response.setContentType("text/html");  // Response mime type
		String starname = request.getParameter("starname");
		System.out.println(starname);
		String birth = request.getParameter("birth");
		Map<String, String> info = new HashMap<>();
		info.put("starname", starname);
		info.put("birth", birth);
		while (info.values().remove(""));
		try 
		{
			// Get a connection from dataSource
			Context initCtx = new InitialContext();

	        Context envCtx = (Context) initCtx.lookup("java:comp/env");
	        
	        // Look up our data source
	        DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");

	        // the following commented lines are direct connections without pooling
	        //Class.forName("org.gjt.mm.mysql.Driver");
	        //Class.forName("com.mysql.jdbc.Driver").newInstance();
	        //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	   

	        Connection dbcon = ds.getConnection();
	        //dbcon.setAutoCommit(false);
	    	Class.forName("com.mysql.jdbc.Driver").newInstance();
	 		// create database connection
    		
    		String query = "SELECT * FROM moviedb.stars\n" + 
    				"order by id Desc\n" + 
    				"limit 1;";
    		java.sql.PreparedStatement Statement =dbcon.prepareStatement(query);
    		long startJDBCtime = System.nanoTime();

			ResultSet rs = Statement.executeQuery();
			 long endJDBCtime = System.nanoTime();
			String id = "";
			while (rs.next()) {
				id = rs.getString("id");
			}
			int num = Integer.parseInt(id.replace("nm",""))+1;
			System.out.print(num);
			java.sql.PreparedStatement Statement1;
			String newid = "nm"+ Integer.toString(num);
	
			// Construct a query with parameter represented by "?"
			System.out.println(info);
    		if(info.containsKey("birth")) {
    			query = "INSERT INTO  stars (id, name, birthYear) VALUES (?,?,?)";
    			Statement1 =dbcon.prepareStatement(query);
    			Statement1.setString(1, newid);
    			Statement1.setString(2, starname);
    			Statement1.setString(3, birth);
    			
    		}else {
    			query = "INSERT INTO  stars (id, name,birthYear) VALUES (?,?,?)";
    			Statement1 =dbcon.prepareStatement(query);
    			Statement1.setString(1, newid);
    			Statement1.setString(2, starname);
    			Statement1.setNull(3, Types.INTEGER);
    		}
    		long startJDBCtime1 = System.nanoTime();
			Statement1.execute();
			long endJDBCtime1 = System.nanoTime();
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
     		out.println("<body>");
     		out.println("<p>");
     		out.println("Successfully insert the "+starname);
     		out.println("</p>");
     		out.println("<h3><a href= \"Insert.html\" >BACK </a></h3>");
     		out.print("</body>");
     		out.println("</head>");
     		out.println("</html>");
     		out.close();
     		dbcon.close();
     		Statement.close();
     		Statement1.close();
     		long endQueryTime = System.nanoTime();
     		
     		File myfile = new File("text.txt");
			if(!myfile.exists()) {
				myfile.createNewFile();
	         } 
			BufferedWriter writer = new BufferedWriter(new FileWriter(myfile.getAbsolutePath(), true));
			writer.write("InsertStarServlet"+"\n");
			Map<String, String> info1 = new HashMap<>();
	         long totalJDBCtime = endJDBCtime - startJDBCtime;
	         long totalJDBCtime1 = endJDBCtime1 - startJDBCtime1;
		     long totalQuerytime = endQueryTime - startquerytime;
		     info1.put("totalJDBCtime", Long.toString(totalJDBCtime));
	         info1.put("totalJDBCtime1", Long.toString(totalJDBCtime1));
		     writer.write("totalQuerytime ");
		     writer.write( Long.toString(totalQuerytime)+"\n");
		     while (info1.values().remove(""));
		     for(Map.Entry<String, String> entry : info1.entrySet()) { 
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
