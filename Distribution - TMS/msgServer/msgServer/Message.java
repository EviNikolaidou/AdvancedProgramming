package msgServer;
import java.util.GregorianCalendar;

/**
 * Class to model an individual message
 */
public class Message 
{ 
  private String sender;


  private String recipient;
  private String content;
  private GregorianCalendar date;

  /**
   * Construct a new object of type Message.  The current date and time
   * is stored in the message.
   * @param String recipient The username of the recipient of the message
   * @param String sender The username of the sender of the message
   * @param String content The content of the message
   */
  public Message(String recipient, String sender, String content)
  {
    this.recipient = recipient;
    this.sender = sender;
    this.content = content;
    this.date = new GregorianCalendar();
  }

  /**
   * Query to obtain the content of this message.
   * @return String The content of this message
   */
  public String getContent()
  {
    return content;
  }

  /**
   * Query to obtain the username of the intended recipient of this message
   * @return String The username of the recipient of this message
   */
  public String getRecipient()
  {
    return recipient;
  }
 
  /**
   * Query to obtain the username of the sender of this message
   * @return String The username of the sender of the message
   */
  public String getSender()
  {
    return sender;
  }

  /**
   * Query to obtain the date and time this message was sent
   * @return String The date and time that the message was sent
   */
  public String getDate()
  {
    return date.getTime().toString();
  }
}
