package msgServer;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Class to model a collection of messages. The username of the recipient is the
 * key that is used to store each message. Therefore it is easy to retrieve
 * messages destined for a particular user.
 */
public class MessageCollection {
	private Hashtable<String, Vector<Message>> messages;

	/**
	 * Construct a new empty MessageCollection
	 */
	public MessageCollection() {
		messages = new Hashtable<String, Vector<Message>>();
	}

	/**
	 * Command to add a new message to the collection
	 * 
	 * @param Message
	 *            message is the message to be added
	 */
	synchronized void addMessage(Message message) {
		if (messages.containsKey(message.getRecipient())) {
			Vector<Message> msgList = (Vector<Message>) messages.get(message.getRecipient());
			msgList.add(message);
		} else {
			Vector<Message> msgList = new Vector<Message>();
			msgList.add(message);
			messages.put(message.getRecipient(), msgList);
		}
	}

	/**
	 * Command to retrieve the oldest message waiting for a specific user. The
	 * message is returned and also deleted from the collection.
	 * 
	 * @param String
	 *            user is the user who the message is addressed to
	 * @return Message The oldest message addressed to that user
	 */
	synchronized public Message getNextMessage(String user) {
		Vector msgList = (Vector) messages.get(user);
		if (msgList != null) {
			Message message = (Message) msgList.firstElement();
			msgList.removeElementAt(0);
			if (msgList.size() == 0) {
				messages.remove(user);
			}
			return message;
		} else {
			return null;
		}
	}

	/**
	 * Query to retrieve the number of messages waiting for a specific user.
	 * 
	 * @param String
	 *            user is the user whose messages we are asking about 8 @return
	 *            int The number of messages waiting for this user
	 */
	synchronized public int getNumberOfMessages(String user) {
		Vector msgList = (Vector) messages.get(user);
		if (msgList != null) {
			return msgList.size();
		} else {
			return 0;
		}
	}

	/**
	 * Command to retrieve all the messages waiting for a specific user. The
	 * messages are deleted from the collection and returned in an array of
	 * Messages.
	 * 
	 * @param String
	 *            user is the user who the messages are addressed to
	 * @return Message[] An array of messages addressed to the user
	 */
	synchronized public Message[] getAllMessages(String user) {
		Vector

		msgList = (Vector) messages.get(user);
		if (msgList != null) {
			messages.remove(user);
			return ((Message[]) msgList.toArray(new Message[msgList.size()]));
		}
		return null;
	}
}
