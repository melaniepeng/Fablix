import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleStarServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;

	// Create a dataSource which registered in web.xml
	String loginUser = "mytestuser";
	String loginPasswd = "mypassword";
	String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json"); // Response mime type

		// Retrieve parameter id from url request.
		String id = request.getParameter("id");
		
		// Output stream to STDOUT
		PrintWriter out = response.getWriter();

		try {
			// Get a connection from dataSource
			Class.forName("com.mysql.jdbc.Driver").newInstance();
	 		// create database connection
	 		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			
			// Construct a query with parameter represented by "?"
			String query = "select m.id, m.title, m.year, m.director, g.name as genre, g.id as gid, s.id as starid,s.name as starname, r.rating\n" + 
					"from movies m, genres_in_movies gm, genres g, stars_in_movies sm, stars s, ratings r\n" + 
					"where m.id =? and m.id = gm.movieId and gm.genreId = g.id and m.id = sm.movieId and sm.starId = s.id and r.movieId = m.id";

			// Declare our statement
			PreparedStatement statement = connection.prepareStatement(query);
			// Set the parameter represented by "?" in the query to the id we get from url,
			// num 1 indicates the first "?" in the query
			System.out.println(id);
			statement.setString(1,id);
			// Perform the query
			ResultSet rs = statement.executeQuery();

			JsonArray jsonArray = new JsonArray();

			// Iterate through each row of rs
			List<List<String>> newinfo = new ArrayList<List<String>>();
			 while (rs.next()) {
				 	System.out.println("ok");
	            	String id1 = rs.getString("id");
	            	String movietitle = rs.getString("title");
	            	String year = rs.getString("year");
	            	String director = rs.getString("director");
	            	String rating = rs.getString("rating");
	            	String gname = rs.getString("genre");
	            	//String sid = rs.getString("starid");
	            	String sname = rs.getString("starname");
	            	//String genreid = rs.getString("gid");
	            	System.out.println(id1);
	            	//newinfo.add(Arrays.asList(id1,movietitle, year, director, rating,gname,sname, sid,genreid));

				// Create a JsonObject based on the data we retrieve from rs

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("movie_id", id1);
				jsonObject.addProperty("movie_title", movietitle);
				jsonObject.addProperty("movie_year", year);
				jsonObject.addProperty("movie_director", director);
				jsonObject.addProperty("rating", rating);
				jsonObject.addProperty("genre", gname);
				//jsonObject.addProperty("star_id", sid);
				jsonObject.addProperty("star_name", sname);
				//jsonObject.addProperty("genreid", genreid);
				//jsonObject.add("movielist", (JsonElement) newinfo);
				
				jsonArray.add(jsonObject);
			}
			
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

			rs.close();
			statement.close();
			connection.close();
		} catch (Exception e) {
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());
			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);
		}
		out.close();

	}

}
