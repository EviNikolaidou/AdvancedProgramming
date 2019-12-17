package msgServer;

import java.util.Hashtable;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Class to execute the login command.
 */
public class LoginCommand implements Command {
	private BufferedWriter out;
	private BufferedReader in;
	private MsgSvrConnection conn;

	/**
	 * Execute the command.
	 */
	public void execute() throws IOException {
		// We first have to read the rest of the request from the BufferedReader
		// First read the username
		String username = in.readLine();
		// Now read the password
		String password = in.readLine();
		// if no-one is logged in for this connection at the moment....
		if (conn.getCurrentUser() == null) {
			// to print out logging info, use the server connection's
			// userMsg method and ensure verbose is set to true in the
			// MsgSvrConnection class
			conn.userMsg("LoginCommand: Got user: " + username + " (" + password + ")");
			conn.userMsg("LoginCommand: Server thinks passwd is " + conn.getServer().getUserPassword(username));
			// If the username and password are not null...
			if (password != null && username != null &&
			// and the password is correct...
					password.equals(conn.getServer().getUserPassword(username))) {
				// OK, we can log this user in
				// Set the current user for this connection
				conn.setCurrentUser(username);
				// Reply to the client to say the request was successful
				out.write("200\r\n");
				out.flush();
				try {
					TimerTest logInReminders = new TimerTest(in, out, conn);
					logInReminders.execute();
				} catch (IOException | IndexOutOfBoundsException e) {
					out.write("You have no reminders set.\r\n");
					out.flush();
				}
			} else {
				// Use the ErrorCommand class to report errors
				// In this case, the password is incorrect
				(new ErrorCommand(in, out, conn, "Incorrect Password")).execute();
			}
		} else {
			// Use the ErrorCommand class to report errors
			// In this case, someone has already been logged in on this
			// connection
			(new ErrorCommand(in, out, conn, "Another user is currently logged in")).execute();
		}

	}

	/**
	 * Construct a new LoginCommand object
	 * 
	 * @param BufferedReader
	 *            in A reader to read the request from the client connection
	 * @param BufferedWriter
	 *            out A writer to write the response to the client
	 * @param MsgServerConnection
	 *            serverConn The class modelling the connection between this
	 *            server and the client
	 */
	public LoginCommand(BufferedReader in, BufferedWriter out, MsgSvrConnection serverConn) {
		this.out = out;
		this.in = in;
		this.conn = serverConn;
	}
}
