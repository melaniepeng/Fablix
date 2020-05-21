import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

 
public class SaxActor extends DefaultHandler {
	  
	   boolean name = false;
	   boolean birth = false;
	   Map<String, String> info = new HashMap<>();
	   String Name = "";
	   String Bob = "";
	   static long endJDBCtime1;
	   static long startJDBCtime1;
	   static long endJDBCtime2;
	   static long startJDBCtime2;
    /* 此方法有三个参数
       arg0是传回来的字符数组，其包含元素内容
       arg1和arg2分别是数组的开始位置和结束位置 */
    @Override
    public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
        String content = new String(arg0, arg1, arg2);
        //System.out.println(content);
        
        super.characters(arg0, arg1, arg2);
        if (name) {
        	Name = new String(arg0, arg1, arg2);
            //System.out.println("Name: " + Name);
            name = false;
         } else if (birth) {
        	Bob =  new String(arg0, arg1, arg2);
            //System.out.println("Birthday: " + Bob);
            birth = false;
            info.put(Name, Bob);
     
            
         }
        
        
    }
 
    @Override
    public void endDocument() throws SAXException {
        System.out.println("\n…………End Parsing…………");
        long startquerytime = System.nanoTime();

        String loginUser = "mytestuser";
		String loginPasswd = "mypassword";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		//System.out.println(info);
		try {
		//System.out.println("went to ");
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
			String query = String.format("SELECT name FROM moviedb.stars;");
			java.sql.PreparedStatement Statement =dbcon.prepareStatement(query);
			long startJDBCtime = System.nanoTime();
			ResultSet rs = Statement.executeQuery();
			long endJDBCtime = System.nanoTime();
			ArrayList<String> db = new ArrayList<String>();
			while (rs.next()) {
				String abname = rs.getString("name");
				db.add(abname);
		}
		//System.out.println("ok");
		int count=0;
		for(Map.Entry<String, String> ns: info.entrySet()) {
			//System.out.println("ok2");
			//System.out.println(ns.getKey());
			//System.out.println(db.contains(ns.getKey()));
			System.out.println("check"+ns.getKey()+ns.getValue());
			System.out.println("value:"+ns.getValue());
			if(db.contains(ns.getKey())) {
				count++;
				System.out.println("starname: "+ns.getKey());
				System.out.println("starbrith: "+ns.getValue());
			}else{
				//System.out.println("ok3");
				String query1 = String.format("SELECT max(id) as id FROM moviedb.stars;");
				Statement =dbcon.prepareStatement(query1);
				startJDBCtime1 = System.nanoTime();
				ResultSet rs1 = Statement.executeQuery();
				endJDBCtime1 = System.nanoTime();

				String id ="";
				//System.out.println("ok4");
				if (rs1.next()) {
					//System.out.println("ok5");
					id = rs1.getString("id");
					//System.out.println(id);
				}
				id = id. replace("nm","");
				//System.out.println(id);
				int num = Integer.parseInt(id)+1;
				//System.out.println(num);
				String reid = "nm"+ Integer.toString(num);
				//System.out.println(reid);
				String query2 = String.format("INSERT INTO stars VALUES(?,?,?);");
				Statement =dbcon.prepareStatement(query2);
				Statement.setString(1, reid);
				Statement.setString(2, ns.getKey());
				//System.out.println(ns.getValue().equals(" "));
				try
	            {	
	            	Integer.parseInt(ns.getValue());
	            	System.out.println("ok");
	            	System.out.println("pa"+ns.getValue().replaceAll("\\s","").equals(""));
	            	if(ns.getValue().replaceAll("\\s","").equals("")){
						Statement.setNull(3, Types.INTEGER);
					}
	            	else {
							Statement.setInt(3, Integer.parseInt(ns.getValue()));
						}
	            }
	            catch (Exception e) {
	            	//e.printStackTrace();
	            	//newMovie.setYear(Integer.parseInt(new String(arg0, arg1, arg2).trim()));
	            	System.out.println("ok2");
					Statement.setNull(3, Types.INTEGER);
				}
				
				//System.out.println(query2);
				System.out.println(reid+ns.getKey()+ns.getValue());
				startJDBCtime2 = System.nanoTime();
				Statement.executeUpdate();
				endJDBCtime2 = System.nanoTime();
				System.out.println(count);
				System.out.println("finish");
				dbcon.commit();
				
			}
				
			}
				long endQueryTime = System.nanoTime();
				File myfile = new File("text.txt");
					if(!myfile.exists()) {
						myfile.createNewFile();
			         } 
					BufferedWriter writer = new BufferedWriter(new FileWriter(myfile.getAbsolutePath(), true));
					writer.write("SaxActor"+"\n");
					Map<String, String> info = new HashMap<>();
			         long totalJDBCtime = endJDBCtime - startJDBCtime;
			         long totalJDBCtime1 = endJDBCtime1 - startJDBCtime1;
			         long totalJDBCtime2 = endJDBCtime2 - startJDBCtime2;
			         info.put("totalJDBCtime", Long.toString(totalJDBCtime));
			         info.put("totalJDBCtime1", Long.toString(totalJDBCtime1));
			         info.put("totalJDBCtime2", Long.toString(totalJDBCtime2));
			         while (info.values().remove(""));
			         long totalQuerytime = endQueryTime - startquerytime;
				     writer.write("totalQuerytime ");
				     writer.write( Long.toString(totalQuerytime)+"\n");
			         for(Map.Entry<String, String> entry : info.entrySet()) { 
			        	 writer.write("totalJDBCtime ");
			        	 writer.write(entry.getValue());
			         }
				     writer.close();
		     
	//	connection.commit();
		}catch (Exception e){
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
    	if (arg2.equalsIgnoreCase("actor")) {
    		//System.out.println();
    	
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
    	if (arg2.equalsIgnoreCase("stagename")) {
    		name = true;
         } else if (arg2.equalsIgnoreCase("dob")) {
        	 birth = true;
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
        File inputfile = new File("WebContent/actors63.xml");
        SaxActor movie = new SaxActor();
        parser.parse(inputfile, movie);
    }*/
    
}

