package msgServer;

import java.io.IOException;
import java.sql.*;

public class JDBC implements Command {

	Connection myConn = null;
	Statement myStmt = null;
	ResultSet myRs = null;

	public void execute() throws IOException {
			try{
				// 1. Get a connection to database
				Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:9802/.....", "tawil", "12345");
				// 2. Create a statement
				Statement myStmt = myConn.createStatement();
				// 3. Execute SQL query
				ResultSet myRs = myStmt.executeQuery("select.....");
				// 4. Process the result set
				while (myRs.next()) {
					System.out.println(myRs.getString(0));
				}
			}
			
			catch (Exception exc){
				exc.printStackTrace();
			}
			finally {
				if (myRs != null) {
					try {
						myRs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				if (myStmt != null) {
					try {
						myStmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				if (myConn != null) {
					try {
						myConn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
		}
			
		}
}
