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
		System.out.println("Request received: /rest/test");
		String response = "[{\"key\": \"username\",\"type\": \"input\",\"templateOptions\": {\"label\": \"Username\",\"placeholder\": \"johndoe\",\"required\": true,\"description\": \"Descriptive text\"}}, {\"key\": \"password\",\"type\": \"input\",\"templateOptions\": {\"type\": \"password\",\"label\": \"Password\",\"required\": true},\"expressionProperties\": {\"templateOptions.disabled\": \"!model.username\"}}]"; 
		System.out.println(response);
		System.out.println();
		return response;
	}
	
	//
	// ----------------- FORM -----------------
	//
	
	@POST
	@Path("/form")
	@Produces("application/json")
	public String createForm(String data)
	{
		System.out.println("POST Request received: /rest/form"); 
		System.out.println("Got data: "+data); 
		
		String response = "";
		Connection conn = null;
		try {
			conn = getConnection();
			
			String query = "insert into testtable (formtext) values (?)";
			
			PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, data);
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			statement.close();
			
			if(result.next())
			{	
				long id = result.getLong(1);
				
				//replace the id
				String updatedData = data.replaceAll("\"form_id\":\"###REPLACE_FORM_ID###\"", "\"form_id\":\""+id+"\"");				
				
				//update the data set with its just generated ID
				PreparedStatement updateStatement = conn.prepareStatement("UPDATE testtable set formtext =? where id =?");
				updateStatement.setString(1, updatedData);
				updateStatement.setLong(2, id);
				updateStatement.executeUpdate();
				updateStatement.close();
				
				response = "{ \"identifier\":\""+ id + "\" }";
			}
			else
			{
				response = "error inserting";
			}

		} catch (SQLException e) {
			response = "{ \"error\": true }"; 
			System.out.println(response);
			System.out.println();
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
		System.out.println();
		return response;
	}
	
	@GET
	@Path("/form/ids")
	@Produces("application/json")
	public String formIDs() throws ClassNotFoundException
	{
		System.out.println("GET Request received: /rest/ids"); 
		
		String response = "";
		Connection conn = null;
		try {
			System.out.println("lol");
			conn = getConnection();
			Statement stm = conn.createStatement();
			ResultSet result = stm.executeQuery("SELECT FORMTEXT from testtable");
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("{\"formList\":[");
			
			boolean isEmpty = true;
			while(result.next())
			{
				isEmpty = false;
				
				String formular = result.getString(1);
				String metadata = getMetadataStringFromFormularString(formular);
				buffer.append("{ \"metadata\": ");
				buffer.append(metadata);
				buffer.append("} ,");
			}
			
			if (!isEmpty)
			{
				buffer.deleteCharAt(buffer.length() - 1); //remove last ,
			}
			
			buffer.append("]}");
			
			response = buffer.toString();
			
		} catch (SQLException e) {
			response = "{ \"error\": true }"; 
			System.out.println(response);
			System.out.println();
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
		System.out.println();
		return response;
	}
	
	@GET
	@Path("/form/{id}")
	@Produces("application/json")
	public String form(@PathParam("id") String id) throws ClassNotFoundException 
	{ 
		System.out.println("GET Request received: /rest/form/"+id); 
		
		String response = ""; 
		Connection conn = null;
		try {
			
			conn = getConnection();
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery("SELECT FORMTEXT from testtable where id = "+id);
			
			if(result.next())
			{
				response = result.getString(1);
			}
			
		} catch (SQLException e) {
			response = "{ \"error\": true }"; 
			System.out.println(response);
			System.out.println();
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
		System.out.println();
		return response;
	}
	
	@PUT
	@Path("/form")
	@Produces("application/json")
	public String updateForm(String data)
	{
		System.out.println("PUT Request received: /rest/form"); 
		System.out.println("Got data: "+data);
		
		String response = "";
		Connection conn = null;
		try {
			conn = getConnection();
			
			String query = "update testtable set formtext=? where id = ?";
			PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, data);
			String id = getIdStringFromFormularString(data);
			statement.setString(2, id);
			int affectedRows = statement.executeUpdate();
			statement.close();
			
			if(affectedRows == 1)
			{
				response = "{ \"update\":\"ok\" }";
			}
			else
			{
				response = "error inserting";
			}

		} catch (SQLException e) {
			response = "{ \"error\": true }";
			System.out.println(response);
			System.out.println();
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
		System.out.println();
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
		System.out.println("POST Request received: /rest/data"); 
		System.out.println("Got data: "+data);
		
		String response = "";
		Connection conn = null;
		try {
			conn = getConnection();
			String query = "insert into datatable (content) values (?)";
			
			PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, data);
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			statement.close();
			
			if(result.next())
			{
				long id = result.getLong(1);
				
				//replace the id
				String updatedData = data.replaceAll("\"data_id\":\"###REPLACE_DATA_ID###\"", "\"data_id\":\""+id+"\"");				
				
				//update the data set with its just generated ID
				PreparedStatement updateStatement = conn.prepareStatement("UPDATE datatable set content =? where id =?");
				updateStatement.setString(1, updatedData);
				updateStatement.setLong(2, id);
				updateStatement.executeUpdate();
				updateStatement.close();
				
				response = "{ \"identifier\":\""+ id + "\" }";
			}
			else
			{
				response = "error inserting";
			}

		} catch (SQLException e) {
			response = "{ \"error\": true }"; 
			System.out.println(response);
			System.out.println();
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
		System.out.println();
		return response;
	}
	
	@GET
	@Path("/data/ids/{formid}")
	@Produces("application/json")
	public String dataIDs(@PathParam("formid") String formid) throws ClassNotFoundException
	{
		System.out.println("GET Request received: /rest/data/ids/"+formid); 
		
		String response = "";
		Connection conn = null;
		try {
			conn = getConnection();
			conn.createStatement();
			
			PreparedStatement statement = conn.prepareStatement("SELECT CONTENT from datatable");
			
			ResultSet result = statement.executeQuery();
			statement.close();
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("{\"dataList\":[");
			
			boolean isEmpty = true;
			while(result.next())
			{
				String data = result.getString(1);
				
				String currentFormId = getFormIdStringFromDataString(data);
				
				if (currentFormId.equals(formid)) {
					isEmpty = false;
					
					String metadata= getMetadataStringFromDataString(data);
					
					buffer.append("{ \"metadata\": ");
					buffer.append(metadata);
					buffer.append("} ,");
				}
			}
			
			if (!isEmpty)
			{
				buffer.deleteCharAt(buffer.length() - 1); //remove last ,
			}
			
			buffer.append("]}");
			
			response  = buffer.toString();
			
		} catch (SQLException e) {
			response  = "{ \"error\": true }"; 
			System.out.println(response );
			System.out.println();
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		 
		System.out.println(response );
		System.out.println();
		return response ;
	}
	
	@GET
	@Path("/data/{id}")
	@Produces("application/json")
	public String data(@PathParam("id") String id) throws ClassNotFoundException 
	{
		System.out.println("GET Request received: /rest/data/"+id); 
		
		String response = "";
		Connection conn = null;
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT CONTENT from datatable where id =?");
			statement.setInt(1, Integer.parseInt(id));
			ResultSet result = statement.executeQuery();
			statement.close();
			
			if(result.next())
			{
				response = result.getString(1);				
			}
			
		} catch (SQLException e) {
			response = "{ \"error\": true }"; 
			System.out.println(response);
			System.out.println();
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
		System.out.println();
		return response;
	}
	
	@PUT
	@Path("/data")
	@Consumes("application/json")
	@Produces("application/json")
	public String updateData(String data)
	{
		System.out.println("PUT Request received: /rest/data"); 
		System.out.println("Got data: "+data);
		
		String response = "";
		Connection conn = null;
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("UPDATE datatable set content =? where id =?");
			statement.setString(1, data);
			int affectedRows = statement.executeUpdate();
			statement.close();
			
			if(affectedRows == 1)
			{
				response = "{ \"update\":\"ok\" }";
			}
			else
			{
				response = "error inserting";
			}
			
		} catch (SQLException e) {
			response = "{ \"error\": true }"; 
			System.out.println(response);
			System.out.println();
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	
	
	//
	// ----------------- HELPER -----------------
	//
	
	// formular > id	
	public String getIdStringFromFormularString(String formularString) {
		JsonElement formElement = new JsonParser().parse(formularString);
		JsonObject  metadataObject = formElement.getAsJsonObject();
		metadataObject = metadataObject.getAsJsonObject("metadata");
	    String idString = metadataObject.get("form_id").toString();
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
		JsonElement dataElement = new JsonParser().parse(dataString);
		JsonObject  metadataObject = dataElement.getAsJsonObject();
		metadataObject = metadataObject.getAsJsonObject("metadata");
	    String idString = metadataObject.get("data_id").toString();
	    return idString;
	}
	
	// data > formid	
	public String getFormIdStringFromDataString(String dataString) {
		JsonElement dataElement = new JsonParser().parse(dataString);
	    JsonObject metadataObject = dataElement.getAsJsonObject();
	    metadataObject = metadataObject.getAsJsonObject("metadata");
	    String formIdString = metadataObject.get("form_id").getAsString();
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

