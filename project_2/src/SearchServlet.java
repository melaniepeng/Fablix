

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//import org.apache.jasper.tagplugins.jstl.core.Out;

/**
 * Servlet implementation class SearchSevlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 

    /**
     * @see HttpServlet#HttpServlet()
     */
	String getnum = "";
    String sort = "";
    String page = "";
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		long startquerytime = System.nanoTime();
		Map<String, String> info = new HashMap<>();
		Map<String, String> way = new HashMap<>();
		Map<String, String> brow = new HashMap<>();
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
        

        System.out.println();
        info.put("m.title", gettitle);
        info.put("m.year", getyear);   
        info.put("m.director", getdirector);
        info.put("s.name", getstar);
        way.put("num",getnum);
        way.put("type",type);
        way.put("direction",direction);
        if(page.equals("Search")) {
        	way.put("page","0");
        }else {
        	way.put("page", page);
        }
        brow.put("titlestart", titlestart);
        brow.put("gid", gid);
        //System.out.println(page.equals("Search"));
        //System.out.println(page.getClass() == String.class);
        while (info.values().remove(""));
        while (way.values().remove(""));
        while (brow.values().remove(""));
        response.setContentType("text/html"); 
        
		PrintWriter out = response.getWriter();
	        out.println("<html>");
	        /*out.println("<script type=\"text/javascript\">\n" + 
	        		"var queryString = decodeURIComponent(window.location.search);"
	        		+ "</script>");
	        String[] cartArray = request.getParameterValues("cartArray");
	        if(cartArray != null)
	        {
		        for(int i = 0; i < cartArray.length; i++)
		        {
		        	System.out.println(cartArray[i]);
		        }
	        }*/
		    //System.out.println(cartArray.toString());
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
	        					"}\n" +
	        			"</style>");
	        int npage = Integer.parseInt(way.get("page"));	
	        int spage = 0;
	        spage=npage-1;
	        if(spage<0) {
	        	spage = 0;
	        }
	        int opage = npage+1;
	        
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
	        System.out.println("Search"+a);
	        out.println("<script type=\"text/javascript\">\n" +
	        		"function toCheckout(){ \n"+
	        		"    var getval = addToCart;\n" + 
	        		"	console.log(getval);\n"+
	        		"    window.location.href=\"ShoppingServlet?cartArray=" + a +"&cartArray=\" + getval;\n" + 
	        		"}\n"+
	        		"</script>");
	        //System.out.print(opage);
	        out.println("<div class = \"top\">\n" + 
	        		"	<h1> Movies that you are interested</h1>\n" +
	        				//"<a href=\"SearchServlet?title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+spage+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+"&gid="+gid+"&cartArray="+a+"\"class=\"previous\">&laquo; Previous</a>\n" + 
	                		//"<a href=\"SearchServlet?title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+opage+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+"&gid="+gid+"&cartArray="+a+"\" class=\"next\">Next &raquo;</a>"+
	                		"<a href = \"\" class = \"previous\" onclick=\"toSearch(); return false;\">&laquo; Previous</a>\n" +
	                		"<a href = \"\" class = \"next\" onclick=\"toNextSearch(); return false;\">Next &raquo;</a>" +
	                		"<a href = \"\" class = \"goback\" onclick=\"to(); return false;\">Back to Search</a>" + 
	                		//"<a href = \"Search.html?cartArray="+a+"\" class = \"goback\" onclick>Back to Search</a>"+
	                		//"<a href = \"BrowsingServlet?cartArray="+a+"\" class = \"goback\">Back to Browsing</a>"+
	                		"<a href = \"\" class = \"goback\" onclick=\"toBrowsing(); return false;\">Back to Browsing</a>" + 
	                		"<a href = \"Main.html\">Back to Main</a>" +
	                		"<a href = \"\" onclick = \"toCheckout();return false;\">Checkout</a>"+
	                		"<form id=\"search_movie\" action=\"SearchServlet\">"+
	                		"<input type=\"hidden\" name=\"title\" value ="+gettitle+">"
	                		+"<input type=\"hidden\" name=\"year\" value ="+getyear+">"
	                		+"<input type=\"hidden\" name=\"director\" value ="+getdirector+">"
	                		+"<input type=\"hidden\" name=\"star\" value ="+getstar+">"
	                		
	                		+ "<label><b>Number of movies per page</b></label>\n" + 
	                			"<label><b>Number of movies per page</b></label>\n" + 
		                		"    <select name=\"num\">\n" + 
		                		"    <option value=\"10\">10</option>\n" + 
		                		"    <option value=\"20\">20</option>\n" + 
		                		"    <option value=\"25\">25</option>\n" + 
		                		"    <option value=\"50\">50</option>\n" + 
		                		"     <option value=\"\">N/A</option>\n" + 
		                		"  </select>\n" + 
		                	"<input type=\"hidden\" name=\"page\" value=0>"+
	                		" <label><b>Sorted type</b></label>\n" + 
	                		"    <select name=\"type\">\n" + 
	                		"    <option value=\"title\">title</option>\n" + 
	                		"    <option value=\"rating\">rating</option>\n" + 
	                		"    <option value=\"\">N/A</option>\n" + 
	                		"  </select>\n" + 
	                		"  	 <label><b>Sorted way</b></label>\n" + 
	                		"    <select name=\"direction\">\n" + 
	                		"    <option value=\"ASC\">ASC</option>\n" + 
	                		"    <option value=\"DESC\">DESC</option>\n" + 
	                		"  </select>\n" +
	                		"<input type = \"hidden\" name = \"titlestart\" value = \""+titlestart+ "\" >"+
	                		"<input type = \"hidden\" name = \"gid\" value = \""+gid+"\" >" +
	                		"<input type=\"submit\" class = \"btn\" value=\"Search\">"+
	                		"  </form>"+
	        		"</div>");
	        out.print("<p id=\"demo\"></p>\n"+
	        		"<script>\n" + 
	        		"var addToCart = [];\n" +
	        		"function myFunction(movieid) {\n" + 
	        		"	addToCart.push(movieid);\n" + 
	        		"	console.log(addToCart);\n"+
	        		//"	to();\n"+
	        		"}\n" + 
	        		"</script>"+
	        		"<script type=\"text/javascript\">\n" +
	        		"function toCheckout(){ \n"+
	        		"    window.location.href=\"ShoppingServlet?cartArray=" + a +"&cartArray=\"+addToCart;\n" + 
	        		"}\n"+
	        		"function toMovie(movieid){  \n" + 
	        		"    var getval =\"valueTest\";\n" + 
	        		"	window.location.href=\"SingleMServelet?id=\"+movieid+\"&title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+npage+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+"&gid="+gid+ "&cartArray=" + a +"&cartArray=\"+addToCart;\n" + 
	        		"}\n"+
	        		"function toGenre(genreid){ \n"+
	        		"    var getval = addToCart;\n" + 
	        		"	console.log(getval);\n"+
	        		"    window.location.href=\"SearchServlet?title=&year=&director=&star=&num=10&page=Search&type=title&direction=ASC&titlestart=&gid=\"+genreid+\"&cartArray=" + a +"&cartArray=\"+addToCart;\n" + 
	        		"}\n"+
	        		"function toStar(starid){ \n"+
	        		"	console.log(starid);\n"+
	        		"    window.location.href=\"SingleSServelet?id=\"+starid+\"&title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+npage+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+"&gid="+gid+ "&cartArray=" + a +"&cartArray=\"+addToCart;\n" + 
	        		"}\n"+
	        		"function to(){ \n"+
	        		"    window.location.href=\"Search.html?cartArray="+a+"&cartArray=\"+addToCart;\n" + 
	        		"}\n"+
	        		"function toSearch(){ \n"+
	        		"    window.location.href=\"SearchServlet?title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+spage+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+"&gid="+gid+"&cartArray="+a+"&cartArray=\"+addToCart;\n" + 
	        		"}\n"+
	        		"function toNextSearch(){ \n"+
	        		"    window.location.href=\"SearchServlet?title="+gettitle+"&year="+getyear+"&director="+getdirector+"&star="+getstar+"&num="+getnum+"&page="+opage+"&type="+type+"&direction="+direction+"&titlestart="+titlestart+"&gid="+gid+"&cartArray="+a+"&cartArray=\"+addToCart;\n" + 
	        		"}\n"+
	        		"function toBrowsing(){ \n"+
	        		"    window.location.href=\"BrowsingServlet?cartArray="+a+"&cartArray=\"+addToCart;\n" + 
	        		"}\n"+
	        		"</script>");
	        
	    ResultSet rs = null;
	    Connection dbcon = null;
	    java.sql.PreparedStatement Statement = null;
        try {
        	Context initCtx = new InitialContext();

	        Context envCtx = (Context) initCtx.lookup("java:comp/env");
	        
	        // Look up our data source
	        DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");

	        // the following commented lines are direct connections without pooling
	        //Class.forName("org.gjt.mm.mysql.Driver");
	        //Class.forName("com.mysql.jdbc.Driver").newInstance();
	        //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	   
	        // changes
	        dbcon = ds.getConnection();
	       
	    	Class.forName("com.mysql.jdbc.Driver").newInstance();
    		
            // Declare our statement
        	
            
            String query="";
            if(brow.containsKey("titlestart")) {
                query = "select m.id, m.title, m.year, m.director, r.rating, g.id as gid, g.name as gname,s.id as sid, s.name as sname\n"+ 
                  "from movies m, genres_in_movies gm, genres g, stars_in_movies sm, stars s, ratings r\n"+
                  "where m.id = gm.movieId and gm.genreId = g.id and m.id = sm.movieId and sm.starId = s.id and m.id = r.movieId and m.title like \""+brow.get("titlestart")+"%\"";
            }else if(brow.containsKey("gid")) {
            	query = "Select m.id, m.title ,m.director,g.id as gid, g.name as gname,m.year, r.rating , m.starid as sid, m.name as sname\n" + 
            			"from \n" + 
            			"(SELECT ms.id, ms.title, ms.director, ms.year, s.name, s.id as starid\n" + 
            			"FROM movies ms, genres g, genres_in_movies gm, stars s, stars_in_movies sm\n" + 
            			"where  g.id = gm.genreId and ms.id = gm.movieId and s.id = sm.starId and sm.movieId = ms.id and g.id = \""+brow.get("gid")+"\") m, genres g, genres_in_movies gm,ratings r\n" + 
            			"where g.id = gm.genreId and m.id = gm.movieId and r.movieId = m.id";
            }else if(info.isEmpty()&& brow.isEmpty()) {
            	query = "select m.id, m.title, m.year, m.director, r.rating, g.name as gname,s.id as sid, s.name as sname, g.id as gid\n" + 
            			"from movies m, genres_in_movies gm, genres g, stars_in_movies sm, stars s, ratings r\n" + 
            			"where m.id = gm.movieId and gm.genreId = g.id and m.id = sm.movieId and sm.starId = s.id and m.id = r.movieId";
            }else {
            	
            	Map<String, String> q = new HashMap<>();
            	for(Map.Entry<String, String> entry : info.entrySet()) { 
            		String getwhere = "";
	            	String getchar = "";
	    			String value = entry.getValue().replaceAll("\\s+","");
	    			getchar+="%";
	    			for(int i = 0; i < value.length(); i++)
	    			{
	    				   char c = value.charAt(i);
	    				   if(i == value.length()-1)
	    				   {
	    					   getchar+= c;
	    				   }
	    				   else
	    				   {
	    					   getchar+= c+"%";
	    				   }
	    			}
	    			getchar+="%";
	    			if(entry.getKey()=="s.name") {
	    				getwhere = entry.getKey()+" like \""+getchar+"\"";
	    				q.put(entry.getKey(),getwhere);
	    			}else {
	    				if(entry.getKey() == "m.year") {
	    					getwhere =  "and " + entry.getKey() + " = \""+entry.getValue()+"\"";
	    				}else {
	    					getwhere =" and "+entry.getKey()+" like \""+getchar+"\"";
		    				q.put(entry.getKey(),getwhere);	
	    				}
	    				
	    			}
            	}
            	
           
            		if(q.containsKey("s.name")){
            			query += "select m.id, m.title, m.year, m.director, r.rating, g.name as gname, s.id as sid, s.name as sname, g.id as gid from (SELECT mn.id, mn.title, mn.year, mn.director from movies mn, stars_in_movies sm, stars s\n" + 
            					"where "+q.get("s.name")+" and s.id = sm.starId and sm.movieId = mn.id)m, genres_in_movies gm, genres g, stars_in_movies sm, stars s, ratings r where  m.id = gm.movieId and gm.genreId = g.id and m.id = sm.movieId and sm.starId = s.id and m.id = r.movieId";
            			for(Map.Entry<String, String> cs : q.entrySet()) { 
            				if(cs.getKey()!="s.name") {
            					query+=cs.getValue();
            				}
            				
            			}
            		}else {
            			query = "select m.id, m.title, m.year, m.director, r.rating, g.name as gname, s.id as sid, s.name as sname, g.id as gid\n" + 
                    			"from movies m, genres_in_movies gm, genres g, stars_in_movies sm, stars s, ratings r\n" + 
                    			"where m.id = gm.movieId and gm.genreId = g.id and m.id = sm.movieId and sm.starId = s.id and m.id = r.movieId";
            			for(Map.Entry<String, String> ns: q.entrySet()) {
            				query += ns.getValue();
            			}
            		}
            }
            		if(way.containsKey("type")) {
            			if(way.get("type").equals("title")) {
            				query+=" order by m."+way.get("type")+" "+way.get("direction");
            				//System.out.println(query);
            			}else {
            				query+=" order by r."+way.get("type")+" "+way.get("direction");
            				//System.out.println(query);
            			}
            		}
	    			
    			
			
            System.out.println(query);
			// Perform the query
            
            // changes
            Statement =dbcon.prepareStatement(query);
            long startJDBCtime = System.nanoTime();
            // changes
            rs = Statement.executeQuery();
            long endJDBCtime = System.nanoTime();
            List<List<String>> newinfo = new ArrayList<List<String>>();
            while (rs.next()) {
            	String id = rs.getString("id");
            	String movietitle = rs.getString("title");
            	String year = rs.getString("year");
            	String director = rs.getString("director");
            	String rating = rs.getString("rating");
            	String gname = rs.getString("gname");
            	String sid = rs.getString("sid");
            	String sname = rs.getString("sname");
            	String genreid = rs.getString("gid");
    			newinfo.add(Arrays.asList(id,movietitle, year, director, rating,gname,sname, sid,genreid));
    		}
            rs.close();
    		Statement.close();
    		dbcon.close();
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
            int intpage = Integer.parseInt(way.get("page"));	
            if(way.containsKey("num")) {
            	intnum = Integer.parseInt(way.get("num"));	
            }
            int start = intpage*intnum +1;
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
            		if(diffMovie < start) 
            		{
            			//System.out.println("diffMovie < start");
            			;
            			
            		}
            		else 
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
	         
	            		if(currentNumR == numResult)
	            		{
	            			break;
	            		}
	            		currentNumR ++;
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
    		out.println("</body>");
    		
    		long endQueryTime = System.nanoTime();
    		File myfile = new File("text.txt");
			if(!myfile.exists()) {
				myfile.createNewFile();
	         } 
			BufferedWriter writer = new BufferedWriter(new FileWriter(myfile.getAbsolutePath(), true));
			writer.write("SearchServlet"+"\n");
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
		try {
    		rs.close();
    		Statement.close();
    		dbcon.close();
    	}
    	catch(SQLException ee)
    	{
    		ee.printStackTrace();
    	}
		
	}
    /*finally {
    	try {
    		rs.close();
    		Statement.close();
    		dbcon.close();
    	}
    	catch(SQLException e)
    	{
    		e.printStackTrace();
    	}
    }*/
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
