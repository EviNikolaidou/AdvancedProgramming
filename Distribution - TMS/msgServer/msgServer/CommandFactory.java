package msgServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Factory to read the command identifier and return a Command class that can be
 * used to process the rest of the command
 */
public class CommandFactory {
	/**
	 * Read the command identifier and return a Command class
	 * 
	 * @return Command The class that will process this particular command
	 */
	public static msgServer.Command getCommand(BufferedReader in, BufferedWriter out, MsgSvrConnection serverConn)
			throws IOException {
		String cmdString = null;
		try {
			// Read the command identifier (101, 102, 103, 104, 105 or 106)
			cmdString = in.readLine();
			// convert the string to an integer
			int command = Integer.parseInt(cmdString);
			// print out some logging information
			serverConn.userMsg("Read command " + command);
			// Now decide which command identifier we have just read...
			switch (command) {
			case MsgProtocol.LOGIN: // 101
				return new LoginCommand(in, out, serverConn);
			case MsgProtocol.LOGOUT: // 102
				return new LogoutCommand(in, out, serverConn);
			case MsgProtocol.SEND: // 103
				return new SendCommand(in, out, serverConn);
			case MsgProtocol.MESSAGES_AVAILABLE: // 104
				return new GetNumMsgCommand(in, out, serverConn);
			case MsgProtocol.GET_NEXT_MESSAGE: // 105
				return new GetMsgCommand(in, out, serverConn);
			case MsgProtocol.GET_ALL_MESSAGES: // 106
				return new GetAllMsgsCommand(in, out, serverConn);
			case MsgProtocol.REGISTER: // 107
				return new RegistrationCommand(in, out, serverConn);
			case MsgProtocol.UPDATE_DETAILS: //108
				return new RegistrationUpdate(in, out, serverConn);
			case MsgProtocol.SET_REMINDER: // 109
				return new ReminderCommand(in, out, serverConn);
			case MsgProtocol.EDIT_REMINDER: // 110
				return new EditReminderCommand(in, out, serverConn);
			/*
			 * Add more case statements below this comment to process the other
			 * commands
			 */

			/*
			 * Don't add anything below this line
			 */
			default:
				// If the command is not listed above, we don't have such a
				// command
				return new ErrorCommand(in, out, serverConn, "No such command: " + command);
			}
		} catch (NumberFormatException e) {
			// The string sent as command identifier wasn't an integer!
			return new ErrorCommand(in, out, serverConn, "Incorrect Command Identifier: " + cmdString);
		}
	}
}
