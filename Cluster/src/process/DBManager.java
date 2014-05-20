package process;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	static String dbURL = "jdbc:mysql://127.0.0.1/cluster";
	public static ResultSet readFromTable(String tableName){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			String username = "root";
			String password = "root";

			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(dbURL, username, password);

			stmt = conn.createStatement();

			if (stmt.execute("select * from "+tableName)) {
				rs = stmt.getResultSet();
			} else {
				System.err.println("select failed");
			}
		} catch (ClassNotFoundException ex) {
			System.err.println("Failed to load mysql driver");
			System.err.println(ex);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
		return rs;
	}
	// JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   
	   //  Database credentials
	   static final String USER = "root";
	   static final String PASS = "root";
	   
	   public static void createIPTable() { 
	   Connection conn = null;
	   Statement stmt = null;
	   try{
	      //STEP 2: Register JDBC driver
	      Class.forName("com.mysql.jdbc.Driver");

	      //STEP 3: Open a connection
	      System.out.println("Connecting to a selected database...");
	      conn = DriverManager.getConnection(dbURL, USER, PASS);
	      System.out.println("Connected database successfully...");
	      
	      //STEP 4: Execute a query
	      System.out.println("Creating table in given database...");
	      stmt = conn.createStatement();
	      
	      String sql = "CREATE TABLE node ( ip VARCHAR(20) NOT NULL, loads VARCHAR(2),dates DATE,PRIMARY KEY (`ip`));";

	      stmt.executeUpdate(sql);
	      System.out.println("Created table in given database...");
	   }catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   }catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   }finally{
	      //finally block used to close resources
	      try{
	         if(stmt!=null)
	            conn.close();
	      }catch(SQLException se){
	      }// do nothing
	      try{
	         if(conn!=null)
	            conn.close();
	      }catch(SQLException se){
	         se.printStackTrace();
	      }//end finally try
	   }//end try
	   System.out.println("Goodbye!");
	}//end main
	///fire database querry return true if successfully operation otherwise false
	public static boolean fireQuerry(String querry){
		boolean isComplete = false;

		Connection conn = null;
		Statement stmt = null;
		try {
			String username = "root";
			String password = "root";

			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(dbURL, username, password);

			stmt = conn.createStatement();
			stmt.execute(querry);
			isComplete = true;
			
		} catch (Exception e) {
			isComplete = false;
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
		return isComplete;
	}
}
