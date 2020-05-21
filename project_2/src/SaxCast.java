	import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.parsers.SAXParser;
	import javax.xml.parsers.SAXParserFactory;

	import org.xml.sax.Attributes;
	import org.xml.sax.SAXException;
	import org.xml.sax.helpers.DefaultHandler;
	import java.io.BufferedWriter;
	import java.io.File;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
	 
	public class SaxCast extends DefaultHandler {
		long startquerytime = System.nanoTime();
		   boolean Director = false;
		   boolean Mid = false;
		   boolean Title = false;
		   boolean Actor = false;
		   Connection dbcon;
		   
		   String actorid = "";
		   String actorname = "";
		   String movieid = "";
		   String movietitle = "";
		   
		   Map<String, Movies> movieinfo = new HashMap<String, Movies>();
		   Map<String,String> starsinfo = new HashMap<String,String>();
		   Map<String,String> stars_in_movies = new HashMap<String,String>();
		   Map<String,String> stars_in_moviestobeinserted = new HashMap<String,String>();
		   Map<String,String> inconsistent = new HashMap<String,String>();

		   public SaxCast() throws Exception
		   {
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
		    	dbcon.setAutoCommit(false);
	    		String query = "SELECT * from stars;";
	    		java.sql.PreparedStatement Statement =dbcon.prepareStatement(query);
	    		ResultSet starrs = Statement.executeQuery();
	    		while(starrs.next())
	    		{
	    			starsinfo.put(starrs.getString("id"), starrs.getString("name"));
	    		}
	    		query = "SELECT * from stars_in_movies;";
	    		Statement =dbcon.prepareStatement(query);
	    		ResultSet star_in_rs = Statement.executeQuery();
	    		while(star_in_rs.next())
	    		{
	    			stars_in_movies.put(star_in_rs.getString("starId"), star_in_rs.getString("movieId"));
	    		}
	    		query = "SELECT * from movies;";
	    		Statement =dbcon.prepareStatement(query);
	    		ResultSet moviers = Statement.executeQuery();
	    		while(moviers.next())
	    		{
	    			movieinfo.put(moviers.getString("id"), new Movies(moviers.getString("id"), moviers.getString("title"), moviers.getInt("year"), moviers.getString("director")));
	    		}
		   }
		  
		   private void insertStarMovie(String movieid, String actorid)
		   {
			   for(Map.Entry<String, String> movie: stars_in_movies.entrySet())
			   {
				   if(movie.getKey().equals(actorid) && movie.getValue().equals(movieid))
				   {
					   inconsistent.put(actorid, movieid);
					   System.out.println(actorid + " and " + movieid + "already exists!!!");
					   return;
				   }
			   }
			   /*for(Map.Entry<String, String> movie: stars_in_movies.entrySet())
			   {
				   if(movie.getKey().equals(actorid) && movie.getValue().equals(movieid))
				   {
					   inconsistent.put(actorid, movieid);
					   return;
				   }
			   }*/
			   if(movieinfo.containsKey(movieid) && starsinfo.containsKey(actorid))
			   {
				   stars_in_moviestobeinserted.put(actorid, movieid);
			   }
			   else {
				   inconsistent.put(actorid, movieid);
				   System.out.println(actorid + " and " + movieid + " does not exists!!!");
			   }
		   }

		   private void insertDB() throws SQLException
		   {
			   java.sql.PreparedStatement Statement;
			   for(Map.Entry<String, String> movie: stars_in_moviestobeinserted.entrySet())
			   {
				   String query = "INSERT INTO stars_in_movies (starId, movieId) VALUES (?,?);";
				   Statement =dbcon.prepareStatement(query);
				   Statement.setString(1, movie.getKey());
				   Statement.setString(2, movie.getValue());
				   Statement.executeUpdate();
			   }
		   }
	 
	    /* 此方法有三个参数
	       arg0是传回来的字符数组，其包含元素内容
	       arg1和arg2分别是数组的开始位置和结束位置 */
	    @Override
	    public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
	        //String content = new String(arg0, arg1, arg2);
	        //System.out.println(content);
	        
	        super.characters(arg0, arg1, arg2);
	        if (Mid) {
	            //System.out.println("Mid: " + new String(arg0, arg1, arg2));
	            movieid = new String(arg0, arg1, arg2).trim();
	            Mid = false;
	         } else if (Title) {
	            //System.out.println("Title: " + new String(arg0, arg1, arg2));
	            movietitle = new String(arg0, arg1, arg2).trim();
	            Title = false;
	         } else if (Director) {
	            //System.out.println("Director: " + new String(arg0, arg1, arg2));
	            Director = false;
	         } else if (Actor) {
	        	String actor = new String(arg0, arg1, arg2).trim();
	            //System.out.println("Actor: " + actor);
	            if(starsinfo.containsValue(actor))
	            {
	            	for(Map.Entry<String, String> star: starsinfo.entrySet())
	            	{
	            		if(actor.equals(star.getValue()))
	            		{
	            			actorid = star.getKey();
	            		}
	            	}
	            	insertStarMovie(movieid, actorid);
	            }
	            Actor = false;
	         }
	        
	        
	    }
	 
	    @Override
	    public void endDocument() throws SAXException {
	        System.out.println("\n…………End Parsing…………");
	        try {
				insertDB();
				dbcon.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        super.endDocument();
	    }
	 
	    /* arg0是名称空间
	       arg1是包含名称空间的标签，如果没有名称空间，则为空
	       arg2是不包含名称空间的标签 */
	    @Override
	    public void endElement(String arg0, String arg1, String arg2)
	            throws SAXException {
	    	if (arg2.equalsIgnoreCase("filmc")) {
	    		//System.out.println();
	    	}
	    	else if(arg2.equalsIgnoreCase("m"))
	    	{
	    		
	    	}
	    	 super.endElement(arg0, arg1, arg2);
	    }
	    @Override
	    public void startDocument() throws SAXException {
	        System.out.println("…………Start Parsing…………\n");
	        super.startDocument();
	    }
	 
	    /*arg0是名称空间
	      arg1是包含名称空间的标签，如果没有名称空间，则为空
	      arg2是不包含名称空间的标签
	      arg3很明显是属性的集合 */
	    @Override
	    public void startElement(String arg0, String arg1, String arg2,
	            Attributes arg3) throws SAXException {
	    	if (arg2.equalsIgnoreCase("f")) {
	    		Mid = true;
	         } else if (arg2.equalsIgnoreCase("t")) {
	        	 Title = true;
	         } else if (arg2.equalsIgnoreCase("is")) {
	        	 Director = true;
	         }
	         else if (arg2.equalsIgnoreCase("a")) {
	        	 Actor = true;
	         }else if(arg2.equalsIgnoreCase("m"))
	         {
	        	 //Stars 
	        	 actorid = "";
	        	 actorname = "";
	        	 movieid = "";
	        	 movietitle = "";
	         }
	    	
	        //System.out.print(arg2 + ":");
	        super.startElement(arg0, arg1, arg2, arg3);
	    }
	    
	    /*public static void main(String[] args) throws Exception {
	        // 1.实例化SAXParserFactory对象
	        SAXParserFactory factory = SAXParserFactory.newInstance();
	        // 2.创建解析器
	        SAXParser parser = factory.newSAXParser();
	        // 3.获取需要解析的文档，生成解析器,最后解析文档
	        File inputfile = new File("WebContent/casts124.xml");
	        SaxCast movie = new SaxCast();
	        parser.parse(inputfile, movie);
	    }*/
	    
	}


