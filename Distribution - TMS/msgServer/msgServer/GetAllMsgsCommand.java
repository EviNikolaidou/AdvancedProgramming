package msgServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GetAllMsgsCommand implements Command {
	private BufferedReader in;
	private BufferedWriter out;
	private MsgSvrConnection conn;

	public GetAllMsgsCommand(BufferedReader in, BufferedWriter out, MsgSvrConnection serverConn) {
		this.in = in;
		this.out = out;
		this.conn = serverConn;
	}

	public void execute() throws IOException {
		String user = in.readLine();
		// declare a variable user of type String and use it to get the user
		// from the input stream

		if (conn.getCurrentUser() != null && conn.getCurrentUser().equals(user)) {
			// check if current user is not equal to null and current user is
			// equal to the user (use the method getCurrentUser())
			Message msgs = null;
			ArrayList<String> holding = new ArrayList<String>();

			int i = 1;
			while ((msgs = conn.getServer().getMessages().getNextMessage(user)) != null) {
				holding.add("" + MsgProtocol.MESSAGE + "\r\n" + "" + i + "\r\n" + msgs.getSender() + "\r\n"
						+ msgs.getDate() + "\r\n" + msgs.getContent() + "\r\n");
				i++;
			}

			// check if msgs is not equal to null
			if (holding.get(0) == null) {
				(new ErrorCommand(in, out, conn, "You have no messages")).execute();
			} else {
				try {
				while (holding != null) {
					out.write(holding.get(0));
					holding.remove(0);
				}
				out.flush();
				} catch (IndexOutOfBoundsException e) {
					// prevents exception when no messages left
				}
			}
			// capture adequate errors (No messages) or (You are not logged on)
		} else {
			(new ErrorCommand(in, out, conn, "You are not logged on")).execute();
		}
	}
}
