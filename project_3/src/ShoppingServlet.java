

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class CheckoutServlet
 */
@WebServlet("/ShoppingServlet")
public class ShoppingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource(name = "jdbc/moviedb")
	  private DataSource dataSource;
	String loginUser = "mytestuser";
	String loginPasswd = "mypassword";
	String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html"); 
		String delete = request.getParameter("delete");
		String[] cartArray = request.getParameterValues("cartArray");
		String quantity = request.getParameter("quantity");
		String id = request.getParameter("id");
		Map<String, String> change = new HashMap<>();
		change.put(id,quantity);
		 while (change.values().remove(""));
		if(cartArray != null)
		{
			for(int i = 0; i < cartArray.length; i++)
			{	
				System.out.println("at Shopping cart: ");
				String[] sub1 = cartArray[i].split(",");
				for(int j = 0; j < sub1.length; j++)
				{
					System.out.println(sub1[j]);
				}
				//System.out.println(cartArray[i].split(","));
				//System.out.println("at Shopping cart: "+cartArray[i] + " ");
			}
			//System.out.println();
		}
		PrintWriter out = response.getWriter();
	        out.println("<html>");
	        out.println("<head><title>Projec_2</title></head>");
	        out.println("<style>\n" + 
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
	        				"  text-align: center;\n" + 
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
	        			"</style>");
	        /*String a = "";
	        System.out.println("Searchcart: "+cartArray);
	        if(cartArray == null)
	        {
	        	System.out.println("NOT WORKING!"+cartArray);
	        }
	        else {
	        if(cartArray.length != 0 ||Arrays.asList(cartArray).contains("null") == false)
	        {
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
	        }*/
	        //out.println("<div><input type=\"hidden\" name=\"delete\" value ="+delete+"><div>");
	        out.println("<h1>Shopping Cart</h1>");
	        try {
	        	Class.forName("com.mysql.jdbc.Driver").newInstance();
	    		// create database connection
	    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	    		
	            // Declare our statement
	            Statement statement = connection.createStatement();
	            Map<String,Integer> count = new HashMap<String,Integer>();
	            Map<String,String> info = new HashMap<String,String>();
	            for(int i = 0; i < cartArray.length; i ++)
	            {
	            	String[] sub = cartArray[i].split(",");
	            	for(int j = 0; j < sub.length; j++)
	            	{
	            		
			            String query = "select m.id, m.title\n" + 
										"from movies m\n" + 
										"where m.id = \""+ sub[j] + "\"";
			            ResultSet rs = statement.executeQuery(query);
			            while(rs.next())
			            {
			            	String mid = rs.getString("id");
							String title = rs.getString("title");
							if(count.containsKey(mid))
							{
								count.put(mid, count.get(mid)+1);
							}
							else 
							{
								count.put(mid, 1);
							}
							info.put(mid, title);
								
			            }
	            	}
			            
	            }
	            out.println("<body>");
				out.println("<table bgcolor= #ccffef border = \"0\" cellspacing=\"0\">");
				out.println("<tr>"+
						 	"<th>Movie ID</td>\n" +
								"<th>Movie Title</th>\n" +
								"<th>Quantity</th>"+
								"<th>Remove</th></tr>");
				System.out.println(count);
				System.out.println(info);
				if(delete != null)
				{
					info.remove(delete);
					count.remove(delete);
					System.out.println("after delete");
					System.out.println(count);
					System.out.println(info);
				}
				int sum = 0;
				int num = 0;
				System.out.println(change.containsKey(id) + quantity);
				System.out.println("change"+change);
				String a = "";
				for (Map.Entry<String,String> entry : info.entrySet())
		        {
					a += entry.getKey() + ",";
		        }
				for (Map.Entry<String,String> entry : info.entrySet())
		        {	
					if(change.containsKey(id)) {
						if(entry.getKey().equals(id)) {
							int intquantity = Integer.parseInt(quantity);	
							num = intquantity;
							count.put(id, intquantity);
							System.out.println(change.containsKey(id) + quantity);
							System.out.println("change2"+change);
							System.out.println(count);
						}else {
							num = count.get(entry.getKey());
						}
					}
					out.print("<tr><td>" + entry.getKey() + "</td>");
					out.print("<td>" + entry.getValue() + "</td>");
					//out.print("<td>" + count.get(entry.getKey()) + "</td>");
					out.print("<form action = \"ShoppingServlet\"> <td><input type=\"number\" name=\"quantity\" min = 1, value =\""+num+"\" /><br /></td>"
							+ "<input type = \"hidden\" name=delet><input type = \"hidden\" name= cartArray value ="+a+"><input type = \"hidden\" name=id value = \""+entry.getKey()+"\"></form>");
					num++;
					out.print("<td><a href = \"ShoppingServlet?cartArray="+a+"&delete="+ entry.getKey() +"\">REMOVE</a></td></tr>");
					sum += count.get(entry.getKey());
				}
				out.print("</table>");
	            out.print("<h3><a href=\"checkout.html?count="+sum+"&cartArray="+a+"\">Purchase</a></h3>");
	            out.print("<h3><a href=\"BrowsingServlet?cartArray="+a+"\" class=\"button\">Back to Browsing</a></h3>");
	            out.print("<h3><a href=\"Search.html?cartArray="+a+"\" class=\"button\">Back to Search</a></h3></body>");
				
	        }catch (Exception e) {
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
