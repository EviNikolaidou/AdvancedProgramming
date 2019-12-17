package msgServer;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class SendCommand implements Command 
{
  private BufferedReader in; 


  private BufferedWriter out; 
  private MsgSvrConnection conn;

  public SendCommand(BufferedReader in, BufferedWriter out, MsgSvrConnection conn) 
  {
    this.in = in;
    this.out = out;
    this.conn = conn;
  }
  
  public void execute() throws IOException 
  {
      String sender = in.readLine();
      String recipient = in.readLine();
      String content = in.readLine();
      if (conn.getCurrentUser() != null && 
          conn.getCurrentUser().equals(sender)) {
        if (sender != null && recipient != null & content != null) 
        {
            Message m = new Message(recipient, sender, content);
            if (conn.getServer().isValidUser(recipient)) {
              conn.getServer().getMessages().addMessage(m);
	      out.write("200\r\n");
	      out.flush();
	      return;
            } else 
            {
              (new ErrorCommand(in, out,conn, "No such recipient")).execute();                                
            }
        } else {
	  (new ErrorCommand(in, out,conn, "Error trying to send message")).execute();                  
	}
      } else 
      {
        (new ErrorCommand(in, out,conn, "You are not logged in")).execute();                          
      }
  }
}
