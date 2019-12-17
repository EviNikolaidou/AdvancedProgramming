package msgServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class GetMsgCommand implements Command {
	private BufferedReader in;
	private BufferedWriter out;
	private MsgSvrConnection conn;

	public GetMsgCommand(BufferedReader in, BufferedWriter out, MsgSvrConnection serverConn) {
		this.in = in;
		this.out = out;
		this.conn = serverConn;
	}

	public void execute() throws IOException {
		String user = in.readLine();
		if (conn.getCurrentUser() != null && conn.getCurrentUser().equals(user)) {
			Message m = null;
			if ((m = conn.getServer().getMessages().getNextMessage(user)) != null) {
				out.write("" + MsgProtocol.MESSAGE + "\r\n");
				out.write("" + 1 + "\r\n");
				out.write(m.getSender() + "\r\n");
				out.write(m.getDate() + "\r\n");
				out.write(m.getContent() + "\r\n");
				out.flush();
			} else {
				(new ErrorCommand(in, out, conn, "No messages")).execute();
			}
		} else {
			(new ErrorCommand(in, out, conn, "You are not logged on")).execute();
		}
	}
}
