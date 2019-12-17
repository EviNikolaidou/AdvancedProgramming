package msgServer;
import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream; 
import java.net.Socket;
import java.net.ServerSocket;

/**
 * A class to model the message server itself  
 */
public class MessageServer 
{
  public static final int DEFAULT_PORT = 9801;
  private int port;
  private Properties userInfo;
  private MessageCollection messages;
  private boolean verbose;

  /**
   * Construct a new MessageServer
   * @param int portNumber The port number on which the server will listen.
   * The default port number is 9801
   */
  public MessageServer(int portNumber) throws IOException
  {
    // save the port number
    port = portNumber;
    // load the user details from the password file
    // First create a new properties object 
    userInfo = new Properties();
    // set up a FileInputStream which will be used to read in the user details
    FileInputStream fin = null;
    // Construct a new (empty) MessageCollection
    messages = new MessageCollection();
    try {
      // Open the file input stream from the password file
      // See MsgProtocol.java for the actual filename
      fin = new FileInputStream(MsgProtocol.PASSWORD_FILE);
      // and load the user details
      userInfo.load(fin);
    } catch (IOException e) 
      {
	// we can't open the password file
	throw new IOException("Can't open the password file: " +
			      MsgProtocol.PASSWORD_FILE);
      } finally 
	{
	  // finished, so close that input file
	  if (fin != null) 
	    {
	      try 
		{
		  fin.close();
		} catch (IOException e) {}
	    }
	}
  }

  /**
   * Construct a new MessageServer using the default port of 9801
   */
  public MessageServer() throws IOException
  {
    this(DEFAULT_PORT);
  }

  /**
   * Start the server
   */
  public void startService()
  {
    ServerSocket serverSocket = null;
    Socket clientConnection = null;
    try {
      // print out some information to the user
      userMsg("MessageServer: Starting message service on port " + port);
      // Create the server socket
      serverSocket = new ServerSocket(port);
      while (true) {
        // wait for the next connection
        clientConnection = serverSocket.accept();
        userMsg("MessageServer: Accepted from " + 
		clientConnection.getInetAddress());
        // Create a new thread to handle this connection
        MsgSvrConnection conn = 
	  new MsgSvrConnection(clientConnection, this);
	// if you require some information about what is going on
	// pass true to setVerbose.
	// If you're tired of all those messages, pass false to turn them off
	conn.setVerbose(true);
        // Start the new thread
        conn.start();
        // now loop around to await the next connection
        // no need to wait for the thread to finish
      }
    } catch (IOException e) {
      System.out.println(e);
    } finally {
      // We will close the server now
      userMsg("Message Server closing down");
      try {
        if (clientConnection != null) {
          clientConnection.close();
        }
      } catch (IOException e) {}
      try {
        if (serverSocket != null) {
          serverSocket.close();
        }
      } catch (IOException e) {}
    }

  }


  /**
   * Start the program.<br>
   * Usage: java MessageServer [portNumber].
   * @params String[] args The only argument is an optional port number
   */
  public static void main(String[] args) throws IOException
  {
    int portNumber;
    if (args.length == 1) 
      {
	// get the port number we were given
        portNumber = Integer.parseInt(args[0]);
      } else 
	{
	  // use the default port number
	  portNumber = DEFAULT_PORT;
	}
    // create a new MessageServer using this port number
    MessageServer server = new MessageServer(portNumber);
    // set verbose to true if you want information
    // set it to false to turn off all messages
    server.setVerbose(true);
    // Now start the messaging service
    server.startService();
  }

  /**
   * Query to obtain the message collection from the server.
   * @return MessageCollection The collection of messages that are
   * waiting to be delivered to the users.
   */
  public MessageCollection getMessages()
  {
    return messages;
  }

  /**
   * Query to get the password for a specific user.
   * @param String user The username of the user whose password we want to know
   * @return String the password of this user
   */
  public String getUserPassword(String user)
  {
    return userInfo.getProperty(user);
  }

  /**
   * Query to find out if a username represents a valid user.  If the username
   * is in the password file, return true, else return false
   * @return boolean True if the user is in the password file, false otherwise 
   */
  public boolean isValidUser(String username)
  {
    return (userInfo.getProperty(username) != null);
  }

  /**
   * Get the value of the verbose flag which determines whether or not
   * logging is turned on.
   * @return boolean The current value of the verbose flag
   */
  public boolean getVerbose()
  {
    return verbose;
  }

  /**
   * Set the verbose flag
   * @param boolean verbose The new value for the verbose flag
   */
  public void setVerbose(boolean verbose)
  {
    this.verbose = verbose;
  }

  /**
   * Print out a message to the user dependent on the value of the verbose flag
   */
  private void userMsg(String msg) {
    if (verbose) {
      System.out.println("MessageServer: " + msg);
    }
  }
}
