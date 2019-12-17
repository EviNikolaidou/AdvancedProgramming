package msgServer;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;  
import java.io.OutputStreamWriter;
import java.io.IOException;

/**
 * Class to model a connection between the server and client.
 * This class extends the Thread class so multiple connections can
 * be processed simultaneously
 */
public class MsgSvrConnection extends Thread 
{
  private Socket socket;


  private MessageServer server;
  private BufferedReader reader;
  private BufferedWriter writer;
  private String currentUser;
  private boolean verbose;

  /**
   * Construct a new object of type MsgSvrConnection
   */
  public MsgSvrConnection(Socket socket, MessageServer server)
  {
    // socket represents the connection to the client
    this.socket = socket;
    // we don't have a reader or writer yet so initialise them to null
    reader = null;
    writer = null;
    // Keep a reference to the server class because that class
    // maintains the message collection
    this.server = server;
    // no client has yet logged on, initialise currentUser to null
    currentUser = null;
    // we don't want any debugging messages
    verbose = false;
  }

  public void run()
  {
    try {
      // Construct reader to read the client's commands from the InputStream
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      // writer to send responses back to the client down the OutputStream
      writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      // a command class to process the client's command
      Command command = null;
      do {
	// get the appropriate Command class from the CommandFactory
        command = CommandFactory.getCommand(reader, writer, this);
	// Print logging information
	userMsg("CommandFactory returned a " + 
		((Object)command).getClass().getName());
	// and execute the Command we got
        command.execute();
	// we exit if the client has correctly logged out
      } while (! (command instanceof LogoutCommand) || currentUser != null);
    } catch (IOException e) 
      {
	// an IOException occurred
	System.out.println("MsgSvrConnection: " + e);
      } finally 
	{
	  // Shut down this connection
	  try 
	    {
	      userMsg("MsgSvrConnection: Closing this connection");
	      socket.close();
	    } catch (IOException e) {}
	}
  }

  /**
   * Query to get the main MessageServer class.
   * @return MessageServer The class that models the server iteself
   */
  public MessageServer getServer()
  {
    return server;
  }

  /**
   * The verbose flag is used to decide if the connection should print
   * out messages telling what is going on
   * @return boolean Return true if logging is turned on, false otherwise
   */
  public boolean getVerbose()
  {
    return verbose;
  }

  /**
   * Set the verbose flag to true or false.  A false value will turn off the
   * print statements and true will turn them on
   * @param boolean verbose The new value for the verbose flag.
   */
  public void setVerbose(boolean verbose)
  {
    this.verbose = verbose;
  }

  /**
   * Get the username of the user who is currently logged on
   * @return String The username of the current user
   */
  public String getCurrentUser()
  {
    return currentUser;
  }

  /**
   * Set the username of the current user.  This should only be called
   * by the LoginCommand when a new user logs in.  Other command classes
   * use the getCurrentUser query to find out who is currently logged in
   * @param String username The username of the user who has logged in
   */
  public void setCurrentUser(String username) 
  {
    userMsg("setting user to: " + username);
    this.currentUser = username;
  }

  /**
   * Print out messages, dependent on the value of the verbose flag
   * @param String msg The message to print out
   */
  void userMsg(String msg) {
    if (verbose) {
      System.out.println(msg);
    }
  }
}
