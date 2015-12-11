package de.usq;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.*;

@Path("/rest")  
public class WebController {

	@GET
	@Path("/test")
	@Produces("application/json")
	public String test() {
		return "[{\"key\": \"username\",\"type\": \"input\",\"templateOptions\": {\"label\": \"Username\",\"placeholder\": \"johndoe\",\"required\": true,\"description\": \"Descriptive text\"}}, {\"key\": \"password\",\"type\": \"input\",\"templateOptions\": {\"type\": \"password\",\"label\": \"Password\",\"required\": true},\"expressionProperties\": {\"templateOptions.disabled\": \"!model.username\"}}]";

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
	
	@GET
	@Path("/data/{id}")
	@Produces("application/json")
	public String data(@PathParam("id") String id) throws ClassNotFoundException 
	{ 
		Connection conn = null;
		String s = "error";
		try {
			conn = getConnection();
			Statement stm = conn.createStatement();
			ResultSet re = stm.executeQuery("SELECT * from datatable where id = "+id);
			
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
	
	public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try{
        	
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost:8889/testdb";
            String user = "root";
            String password = "root";

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

