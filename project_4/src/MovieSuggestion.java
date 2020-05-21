
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

// server endpoint URL
@WebServlet("/hero-suggestion")
public class MovieSuggestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String loginUser = "mytestuser";
	String loginPasswd = "mypassword";
	String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
	/*
	 * populate the Super hero hash map.
	 * Key is hero ID. Value is hero name.
	 */
	
	
    
    public MovieSuggestion() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// setup the response json arrray
			JsonArray jsonArray = new JsonArray();
			String [] stop = {"a", "around", "and", "every", "for", "from", "in", "is", "it", "not", "on", "one", "the", "to", "under"};
			List<String> list = Arrays.asList(stop);
			// get the query string from parameter
			String query = request.getParameter("query");
			String add ="";
			String[] splited = query.split("\\s+");
			int i =0;
			System.out.println("Suggestions");
			for(i=0; i<splited.length; i++){
				if(!list.contains(splited[i])) {
					add+="+"+splited[i]+"*";
				}
			 }
			System.out.println(add);
			// return the empty json array if query is null or empty
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			connection.setAutoCommit(false);
			String result = String.format("select m.id, m.title\n" + 
					"from movies m, genres_in_movies gm, genres g, stars_in_movies sm, stars s, ratings r\n" + 
					"where m.id = gm.movieId and gm.genreId = g.id and m.id = sm.movieId and sm.starId = s.id and m.id = r.movieId and match (title) against (\""+add+"\"in boolean mode);");
			java.sql.PreparedStatement Statement =connection.prepareStatement(result);
			ResultSet rs = Statement.executeQuery();
			HashMap<String, String> Movielist = new HashMap<>();
			while (rs.next()) {
				String id = rs.getString("id");
				String title = rs.getString("title");
				Movielist.put(id,title);
			}
			System.out.println(Movielist);
			if (query == null || query.trim().isEmpty() || Movielist.isEmpty()) {
				response.getWriter().write(jsonArray.toString());
				return;
			}	
			
			// search on superheroes and add the results to JSON Array
			// this example only does a substring match
			// TODO: in project 4, you should do full text search with MySQL to find the matches on movies and stars
			int b=0;
			for (Map.Entry<String,String> entry : Movielist.entrySet()) {
				jsonArray.add(generateJsonObject(entry.getKey(),  entry.getValue()));
				b++;
				if(b==10) {
					break;
				}
			}
			
			response.getWriter().write(jsonArray.toString());
			return;
		} catch (Exception e) {
			System.out.println(e);
			response.sendError(500, e.getMessage());
		}
	}
	
	/*
	 * Generate the JSON Object from hero to be like this format:
	 * {
	 *   "value": "Iron Man",
	 *   "data": { "heroID": 11 }
	 * }
	 * 
	 */
	private static JsonObject generateJsonObject(String id, String movietitle) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", movietitle);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("heroID", id);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}


}
