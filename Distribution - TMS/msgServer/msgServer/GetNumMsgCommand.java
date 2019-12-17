package msgServer;
import java.io.BufferedReader; 
import java.io.BufferedWriter;
import java.io.IOException;

public class GetNumMsgCommand implements Command  
{
  private BufferedReader in;
  private BufferedWriter out;
  private MsgSvrConnection conn;

  public GetNumMsgCommand(BufferedReader in, BufferedWriter out, MsgSvrConnection serverConn)
  {
    this.in = in;
    this.out = out;
    this.conn = serverConn;
  }

  public void execute() throws IOException
  {
      String user = in.readLine();
      if (conn.getCurrentUser() != null && 
          conn.getCurrentUser().equals(user)) {
          Message m = null;
          if (conn.getServer().getMessages().getNumberOfMessages(user) > 0) 
          {
            out.write("" + MsgProtocol.OK + "\r\n");
            out.flush();
          } else 
          {
            (new ErrorCommand(in, out,conn, "No messages")).execute();    
          }
      } else 
      {
        (new ErrorCommand(in, out,conn, "You are not logged on")).execute();
      }
  }
}
