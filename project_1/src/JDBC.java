import java.sql.*;
import java.util.*;
public class JDBC {
	   
	    public static void main(String[] arg) throws Exception {

	        // Incorporate mySQL driver
	        Class.forName("com.mysql.jdbc.Driver").newInstance();

	        // Connect to the test database
	        Connection connection = DriverManager.getConnection("jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false",
	                "root", "Kathy981294");

	        // Create an execute an SQL statement to select all of table"Stars" records
	        Statement select = connection.createStatement();
	        ResultSet result = select.executeQuery("select m.id, m.title, m.year, m.director, mid.rating, g.name as genres, s.name as stars\n" + 
	        		"from (select movieId, rating from ratings order by rating desc limit 20) mid, movies m, genres_in_movies gm, genres g, stars_in_movies sm, stars s\n" + 
	        		"where mid.movieId = m.id and gm.movieId = mid.movieId and gm.genreId = g.id and mid.movieId = sm.movieId and s.id = sm.starId;");

	        // Get metatdata from stars; print # of attributes in table
	        System.out.println("The results of the query");
	        ResultSetMetaData metadata = result.getMetaData();
	        System.out.println("There are " + metadata.getColumnCount() + " columns");

	        // Print type of each attribute
	        for (int i = 1; i <= metadata.getColumnCount(); i++)
	            System.out.println("Type of column " + i + " is " + metadata.getColumnTypeName(i));

	        // print table's contents, field by field
	        while (result.next()) {
	        	Vector v = new Vector(0, 0);
	        	v.addElement(result.getString("id"));
	        	System.out.println("title = " + result.getString("id"));
	            System.out.println("title = " + result.getString("title"));
	            System.out.println("year = " + result.getString("year"));
	            System.out.println("director = " + result.getString("director"));
	            System.out.println("rating = " + result.getString("rating"));
	            System.out.println("name = " + result.getString("genres"));
	            System.out.println("name = " + result.getString("stars"));
	            System.out.println();
	        }
	       
	        
	        
	      
	 }
	}


