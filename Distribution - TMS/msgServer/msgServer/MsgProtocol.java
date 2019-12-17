package msgServer;

public class MsgProtocol 
{
  /*
   * The location of the password file.
   */
  public static final String PASSWORD_FILE = "pwd.txt";

  /* -------------- Commands --------------- */
  /**
   * client requests to login
   * Following lines are
   * username\r\n
   * password\r\n
   */
  public static final int LOGIN = 101;
  /**
   * Client requests logout
   * following line is:
   * username\r\n
   */
  public static final int LOGOUT = 102;
  /**
   * client requests send a message
   * Following lines are:
   * sender name\r\n
   * recipient name\r\n
   * content\r\n
   */
  public static final int SEND = 103;
  /**
   * client requests the number of messages
   * following lines are
   * username\r\n
   */
  public static final int MESSAGES_AVAILABLE = 104;
  /**
   * Client requests to get a single message
   * Following lines are:
   * username\r\n
   */
  public static final int GET_NEXT_MESSAGE = 105;
  /**
   * Client requests to get all messages
   * Following lines are:
   * username\r\n
   * Server reponds by sending all messages for that user
   */
  public static final int GET_ALL_MESSAGES = 106;
  /**
   * Program checks a user isn't currently logged in
   * Following lines are:
   * username\r\n
   * password\r\n
   * User asked if they want full registration
   * If not:
   * Program writes new username and password to 'pwd.txt' file
   * If yes: 
   * phoneNum
   * address
   * DateOfBirth
   * Program writes new username and password to 'pwd.txt' file
   * Program writes username, password, phoneNum, address and DoB to 'UserDetails.txt'
   */
  public static final int REGISTER = 107;
  /** 
   * Program checks that user is logged in
   * Client asked if they wish to update details
   * If yes:
   * Client requests to update details
   * Following lines are:
   * username\r\n
   * password\r\n
   * Client enters new details:
   * password\r\n
   * phoneNum\r\n
   * address\r\n
   * dateOfBirth\r\n
   * Program writes new details to 'UserDetails.txt'
   * Program writes new password to 'pwd.txt'
   */
  public static final int UPDATE_DETAILS = 108;
  /**
   * Program checks user is logged in
   * Client asked when they want the reminder to occur
   * User Inputs:
   * day\r\n
   * month\r\n
   * year\r\n
   * hours\r\n
   * minutes\r\n
   * reminderMessage\r\n
   * Output stored to 'username_reminders.txt'
   */
  public static final int SET_REMINDER = 109;
  /**
   * Program checks user is logged in
   * Client asked if they which to edit, view, delete or cancel a reminder
   * If user chose view:
   * They input 'view'
   * Program lists the reminders for that user from 'username_reminders.txt'
   * If user chose cancel:
   * They input 'cancel'
   * sequence terminates
   * If user chose delete:
   * They input 'delete'
   * User inputs line number of reminder they wish to delete
   * Reminder is deleted
   * 'username_reminders.txt' is updated without the deleted reminder
   * If user chose edit:
   * They input 'edit'
   * User enters line number of reminder they wish to edit
   * User asked if they wish to edit 'date', 'time' or 'message'
   * If they chose 'message':
   * newMessage\r\n
   * 'username_reminders.txt' updated
   * If they chose 'time':
   * newHours\r\n
   * newMinutes\r\n
   * 'username_reminders.txt' updated
   * If they chose 'date':
   * User has a choice of editing 'day', 'month' or 'year'
   * if 'day':
   * newDay\r\n
   * 'username_reminders.txt' updated
   * if 'month':
   * newMonth\r\n
   * 'username_reminders.txt' updated
   * if 'year':
   * newYear\r\n
   * 'username_reminders.txt' updated
   */
  public static final int EDIT_REMINDER = 110;
  /* -------------- Responses --------------- */
  /**
   * Server responds OK
   */
  public static final int OK = 200;
  /**
   * Server responds by sending one or more messages
   * following will be
   * An integer specifying number of messages terminated by \r\n
   * Then repeated for the number of messages are
   * sender terminated by \r\n
   * content terminated by \r\n
   */
  public static final int MESSAGE = 201;
  /**
   * The server sends an error message
   * Requires a one line error message terminated by \r\n
   */
  public static final int ERROR = 500;
}
