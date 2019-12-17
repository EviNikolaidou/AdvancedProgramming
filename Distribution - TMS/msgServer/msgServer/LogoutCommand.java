package msgServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Class to execute the logout command.
 */
public class LogoutCommand implements Command {
	private BufferedWriter out;
	private BufferedReader in;
	private MsgSvrConnection conn;

	/**
	 * Construct a new LogoutCommand object
	 * 
	 * @param BufferedReader
	 *            in A reader to read the request from the client connection
	 * @param BufferedWriter
	 *            out A writer to write the response to the client
	 * @param MsgServerConnection
	 *            serverConn The class modelling the connection between this
	 *            server and the client
	 */
	public LogoutCommand(BufferedReader in, BufferedWriter out, MsgSvrConnection serverConn) {
		this.out = out;
		this.in = in;
		this.conn = serverConn;
	}

	/**
	 * Execute the command.
	 */
	public void execute() throws IOException {
		// read the rest of the request...
		// the client should have sent a username
		String username = in.readLine();
		// if the username is the same user who is currently logged in
		if (username.equals(conn.getCurrentUser())) {
			// clear the current user
			conn.setCurrentUser(null);
			// write a successful response to the client
			out.write("200\r\n");
			out.flush();
		} else if (conn.getCurrentUser() == null) // no user logged in
		{
			// Use the ErrorCommand class to send error messages...
			// The user is already logged out
			(new ErrorCommand(in, out, conn, "Already logged out")).execute();
		} else // some other user is logged in
		{
			// Use the ErrorCommand class to send error messages...
			// It's the wrong username, someone else is currently logged in
			(new ErrorCommand(in, out, conn, "Another user is currently logged in")).execute();
		}
	}
}
