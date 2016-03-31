package de.usq;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.ws.rs.*;



@Path("/rest")  
public class WebController {

	@GET
	@Path("/test")
	@Produces("application/json")
	public String test() {
		return "[{\"key\": \"username\",\"type\": \"input\",\"templateOptions\": {\"label\": \"Username\",\"placeholder\": \"johndoe\",\"required\": true,\"description\": \"Descriptive text\"}}, {\"key\": \"password\",\"type\": \"input\",\"templateOptions\": {\"type\": \"password\",\"label\": \"Password\",\"required\": true},\"expressionProperties\": {\"templateOptions.disabled\": \"!model.username\"}}]";
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
			ResultSet re = stm.executeQuery("SELECT ID from testtable ");
			StringBuffer b = new StringBuffer();
			b.append("{\"formList\":[");
			while(re.next())
			{
				int i = re.getInt(1);
				b.append("{ \"id\": ");
				b.append(i);	
				b.append(", \"label\": \"");
				b.append(i);
				b.append("\" } ,");
			}
			
			
			b.deleteCharAt(b.length() - 1); //remove last ,
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
			ResultSet re = stm.executeQuery("SELECT * from testtable where id = "+id);
			
			if(re.next())
			{
				s = re.getString(2);				
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
	@Path("/form/{id}")
	@Produces("application/json")
	public String updateForm(@PathParam("id") String id, String data)
	{
		String response = "";
		Connection conn = null;
		try {
			conn = getConnection();
			String query = "update testtable set formtext=? where id = ?";
			System.out.println("preparing");
			PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, data);
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
				response = "{\"identifier\":\""+ id + "\"}";
			}
			else
			{
				response = "error inserting";
			}

		} catch (SQLException e) {
			response = "{error: true }";
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
	@Path("/data/ids")
	@Produces("application/json")
	public String dataIDs() throws ClassNotFoundException
	{
		Connection conn = null;
		String s = "error";
		try {
			
			conn = getConnection();
			Statement stm = conn.createStatement();
			ResultSet re = stm.executeQuery("SELECT ID from datatable ");
			StringBuffer b = new StringBuffer();
			b.append("{\"dataList\":[");
			while(re.next())
			{
				int i = re.getInt(1);
				b.append("{ \"id\": ");
				b.append(i);	
				b.append(", \"label\": \"");
				b.append(i);
				b.append("\" } ,");
			}
			
			b.deleteCharAt(b.length() - 1); //remove last ,
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
				s = re.getString(2);				
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
	@Path("/data/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public String updateData(@PathParam("id") String id,String json)
	{
		System.out.println("json: " + json);
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("UPDATE datatable set content =? where id =?");
			statement.setString(1, json);
			statement.setInt(2, Integer.parseInt(id));
			
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

