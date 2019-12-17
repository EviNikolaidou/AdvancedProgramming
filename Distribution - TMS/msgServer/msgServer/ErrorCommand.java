package msgServer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException; 

/**
 * Command class to be used when there is some kind of error
 */
public class ErrorCommand implements Command 
{
  private BufferedWriter out;
  private MsgSvrConnection conn;
  private String message;

  /**
   * Construct a new ErrorCommand object.  Errors are also commands so that
   * they can be executed just like any other command.  An ErrorCommand will
   * send a 500 response to the client.
   * @param BufferedReader in A reader connected to the InputStream
   * @param BufferedWriter out A writer connected to the OutputStream
   * @param MsgSvrConnection serverConn The class modelling the connection
   * @param String message The message describing the error
   */
  public ErrorCommand(BufferedReader in, BufferedWriter out, 
		      MsgSvrConnection serverConn, String message)
  {
    this.out = out;
    this.conn = serverConn;
    this.message = message;  
  }

  /**
   * Execute the command, which will send a 500 response and an error message
   */
  public void execute() throws IOException
  {
    try 
      {
	// send the response ID
	out.write("500\r\n"); 
	// Send the message
	out.write(message + "\r\n"); 
	// flush the output
	out.flush();
      } catch (IOException e) { throw new IOException(e.getMessage());}
  }
}
