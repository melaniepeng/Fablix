

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class MetaDataServlet
 */
@WebServlet("/MetaDataServlet")
public class MetaDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
 */
    public MetaDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
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
			"	border: 1px solid black;\n" +
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
        out.println("<div><h1> MetaData</h1>");
        out.println("<h3><a href = \"Insert.html\">BACK</a></h3></div>");
        out.println("<body><table bgcolor= #ccffef border = \"0\" cellspacing=\"0\">");
        out.println("<tr><th>Table Name</th>\n"+
        				"<th>Column Name</th>\n" + 
        				"<th>Type</th>");
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
    		DatabaseMetaData databaseMetaData = dbcon.getMetaData();
    		ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
    		out.println();
    		
    		while(resultSet.next())
    		{
    			String tablename = resultSet.getString("TABLE_NAME");
    			System.out.println(tablename);
    			ResultSet columns = databaseMetaData.getColumns(null,null, tablename, null);
    			int rowCount = 0;
    			if (columns.last()) {//make cursor to point to the last row in the ResultSet object
    	             rowCount = columns.getRow();
    	             columns.beforeFirst(); //make cursor to point to the front of the ResultSet object, just before the first row.
    			}
    			out.println("<tr><td rowspan=\""+ rowCount +"\">"+ tablename +" </td>");
    			while(columns.next())
    			{
	    			String columnName = columns.getString("COLUMN_NAME");
	    		    String datatype = columns.getString("TYPE_NAME");
	    		    System.out.print(columnName + "\t");
	    		    System.out.println(datatype);
	    		    out.println("<td>"+columnName+"</td>");
	    		    out.println("<td>"+datatype+"</td></tr>");
    			}
    		}
    		out.println("</table></body>");
    		out.close();
    		dbcon.close();
    		
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
     		//PrintWriter out = response.getWriter();
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
