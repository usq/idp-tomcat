package de.usq;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.ws.rs.*;
import com.google.gson.*;

@Path("/rest")  
public class WebController {
	
	//
	// ----------------- TEST -----------------
	//

	@GET
	@Path("/test")
	@Produces("application/json")
	public String test() {
		return "[{\"key\": \"username\",\"type\": \"input\",\"templateOptions\": {\"label\": \"Username\",\"placeholder\": \"johndoe\",\"required\": true,\"description\": \"Descriptive text\"}}, {\"key\": \"password\",\"type\": \"input\",\"templateOptions\": {\"type\": \"password\",\"label\": \"Password\",\"required\": true},\"expressionProperties\": {\"templateOptions.disabled\": \"!model.username\"}}]";
	}
	
	@GET
	@Path("/parse")
	@Produces("application/json")
	public String parse() {
		String json = "{\"key\": \"username\",\"type\": \"input\", \"object\": { \"name\": \"myObject\", \"array\": [\"Parsing succeded\", \"second\", \"third\"]}}";
		System.out.println(json);
		String parsedJSON = parse(json);
		System.out.println(parsedJSON);
		return parsedJSON;
	}
	
	public String parse(String jsonLine) {
	    JsonElement jelement = new JsonParser().parse(jsonLine);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    jobject = jobject.getAsJsonObject("object");
	    JsonArray jarray = jobject.getAsJsonArray("array");
	    String result = jarray.get(0).toString();
	    return result;
	}
	
	//
	// ----------------- FORM -----------------
	//
	
	@POST
	@Path("/form")
	@Produces("application/json")
	public String createForm(String data)
	{
		System.out.println("got: " + data);
		String response = "";
		Connection conn = null;
		try {
			conn = getConnection();
			
			String query = "insert into testtable (formtext) values (?)";
			
			PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, data);
			
			int affectedRows = statement.executeUpdate();
			
			ResultSet result = statement.getGeneratedKeys();
			if(result.next())
			{
				long id = result.getLong(1);
				response = "{\"identifier\":\""+ id + "\"}";
			}
			else
			{
				response = "error inserting";
			}

		} catch (SQLException e) {
			response = "{error: true }";
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		
		System.out.println(response);
		return response;
	}
	
	@GET
	@Path("/form/ids")
	@Produces("application/json")
	public String formIDs() throws ClassNotFoundException
	{
		Connection conn = null;
		String s = "error";
		try {
			
			conn = getConnection();
			Statement stm = conn.createStatement();
			ResultSet re = stm.executeQuery("SELECT ID,FORMTEXT from testtable");
			
			StringBuffer b = new StringBuffer();
			b.append("{\"formList\":[");
			
			boolean isEmpty = true;
			while(re.next())
			{
				isEmpty = false;
				
				int id = re.getInt(1);
				b.append("{ \"id\": ");
				b.append(id);
				
				String formular = re.getString(2);
				String metadata = getMetadataStringFromFormularString(formular);
				b.append(", \"metadata\": ");
				b.append(metadata);
				b.append(" } ,");
			}
			
			if (!isEmpty)
			{
				b.deleteCharAt(b.length() - 1); //remove last ,
			}
			
			b.append("]}");
			
			s = b.toString();
			System.out.println(s);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return s;
	}
	
	@GET
	@Path("/form/{id}")
	@Produces("application/json")
	public String form(@PathParam("id") String id) throws ClassNotFoundException 
	{ 
		Connection conn = null;
		String s = "error";
		try {
			
			conn = getConnection();
			Statement stm = conn.createStatement();
			ResultSet re = stm.executeQuery("SELECT FORMTEXT from testtable where id = "+id);
			
			if(re.next())
			{
				s = re.getString(1);
			}
			
			System.out.println(s);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return s;
	}
	
	@PUT
	@Path("/form")
	@Produces("application/json")
	public String updateForm(String data)
	{
		String response = "";
		Connection conn = null;
		try {
			conn = getConnection();
			String query = "update testtable set formtext=? where id = ?";
			System.out.println("preparing");
			PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, data);
			String id = getIdStringFromFormularString(data);
			statement.setString(2, id);
			System.out.println("executing: " + statement.toString());
			int affectedRows = statement.executeUpdate();
			System.out.println("affected: " + affectedRows);
			
			if(affectedRows == 1)
			{
				response = "{\"update\":\"ok\"}";
			}
			else
			{
				response = "error inserting";
			}

		} catch (SQLException e) {
			response = "{error: true }";
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		System.out.println(response);
		return response;
	}
	
	
	//
	// ----------------- DATA -----------------
	//
	
	@POST
	@Path("/data")
	@Produces("application/json")
	public String createData(String data)
	{
		System.out.println("got: " + data);
		String response = "";
		Connection conn = null;
		try {
			conn = getConnection();
			String query = "insert into datatable (content) values (?)";
			
			PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, data);
			
			int affectedRows = statement.executeUpdate();
			
			ResultSet result = statement.getGeneratedKeys();
			if(result.next())
			{
				long id = result.getLong(1);
				response = "{ \"identifier\":\""+ id + "\" }";
			}
			else
			{
				response = "error inserting";
			}

		} catch (SQLException e) {
			response = "{ error: true }";
			System.out.println(response);
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(response);
		return response;
	}
	
	@GET
	@Path("/data/ids/{formid}")
	@Produces("application/json")
	public String dataIDs(@PathParam("formid") String formid) throws ClassNotFoundException
	{
		Connection conn = null;
		String s = "error";
		try {
			conn = getConnection();
			Statement stm = conn.createStatement();
			
			PreparedStatement statement = conn.prepareStatement("SELECT CONTENT from datatable");
			
			ResultSet re = statement.executeQuery();
			
			StringBuffer b = new StringBuffer();
			b.append("{\"dataList\":[");
			
			boolean isEmpty = true;
			while(re.next())
			{
				isEmpty = false;
				
				String data = re.getString(1);
				
				String currentFormId = getFormIdStringFromDataString(data);
				
				if (currentFormId == formid) {
					String id = getIdStringFromDataString(data);
					String metadata= getMetadataStringFromDataString(data);
					
					b.append("{ \"id\": ");
					b.append(id);
					
					b.append(", \"metadata\": \"");
					b.append(metadata);
					b.append("\" } ,");
				}
			}
			
			if (!isEmpty)
			{
				b.deleteCharAt(b.length() - 1); //remove last ,
			}
			
			b.append("]}");
			
			s = b.toString();
			System.out.println(s);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return s;
	}
	
	@GET
	@Path("/data/{id}")
	@Produces("application/json")
	public String data(@PathParam("id") String id) throws ClassNotFoundException 
	{
		Connection conn = null;
		String s = "error";
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT * from datatable where id =?");
			statement.setInt(1, Integer.parseInt(id));
			
			ResultSet re = statement.executeQuery();
			if(re.next())
			{
				s = re.getString(4);				
			}
			
			System.out.println(s);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return s;
	}
	
	@PUT
	@Path("/data")
	@Consumes("application/json")
	@Produces("application/json")
	public String updateData(String json)
	{
		System.out.println("json: " + json);
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("UPDATE datatable set content =? where id =?");
			
			String id = getIdStringFromDataString(json);
			
			statement.setString(1, json);
			statement.setString(2, id);
			
			statement.executeUpdate();
			statement.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	
	//
	// ----------------- HELPER -----------------
	//
	
	// formular > id	
	public String getIdStringFromFormularString(String formularString) {
		JsonElement dataElement = new JsonParser().parse(formularString);
		JsonObject  idObject = dataElement.getAsJsonObject();
	    idObject = idObject.getAsJsonObject("id");
	    String idString = idObject.toString();
	    return idString;
	}
	
	// formular > metadata
	public String getMetadataStringFromFormularString(String formularString) {
		JsonElement formularElement = new JsonParser().parse(formularString);
	    JsonObject  metadataObject = formularElement.getAsJsonObject();
	    metadataObject = metadataObject.getAsJsonObject("metadata");
	    String metadataString = metadataObject.toString();
	    return metadataString;
	}

	// data > id	
	public String getIdStringFromDataString(String dataString) {
		//right now same shit as for forms
	    return getIdStringFromFormularString(dataString);
	}
	
	// data > formid	
	public String getFormIdStringFromDataString(String dataString) {
		JsonElement dataElement = new JsonParser().parse(dataString);
	    JsonObject metadataObject = dataElement.getAsJsonObject();
	    metadataObject = metadataObject.getAsJsonObject("metadata");
	    JsonObject formIdObject = metadataObject.getAsJsonObject("form_id");
	    String formIdString = formIdObject.toString();
	    return formIdString;
	}
	
	// data > metadata	
	public String getMetadataStringFromDataString(String dataString) {
		//right now same shit as for forms
	    return getMetadataStringFromFormularString(dataString);
	}
	
	
	public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try{
        	
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://192.168.99.100:3306/testdb";
            String user = "root";
            String password = "";
            
            url = "jdbc:mysql://localhost:8889/testdb";
            user = "root";
            password = "root";


            conn = (Connection) DriverManager.getConnection(url, user, password);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(InstantiationException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
            return conn;

    }
}

