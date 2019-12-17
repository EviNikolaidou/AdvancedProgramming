**Module: Advanced Programming**

**Group Members: **
###### *Evi Nikolaidou,Callum Jack Melrose, Benjamin Stephen Long, Muhammad Awais*

Assesment Brief
------------



## Assessment Title: Developing a Text Messaging Server in Java,A Client/Server Internet Utility Tool


**Assessment Summary:**

This is a group project. The maximum number of students in each group should not exceed four members; groups can be formed of less than
four but with a minimum of two members in a group. Use the CMP5308 -Advanced Programming Coursework (Groups) Group choice system to
enrol yourself and join a specific group. For this assignment you will design, implement and demonstrate a (Text massaging client/ server) chat Application in Java.


## 1.Problem Description

Your group task is to implement a text-messaging server that can be used for exchanging text messaging. A text message contains a single line of ASCII text, sent to a single user. The two
components of this system are; a message server that manages the messages and a telnet client that can be used to connect to the server and to send and receive messages. 
A partial implementation of the server will be made available to the students and in it its, in its current state, it allows users to Log In, Log Out, send a message, check for waiting
messages and get the next message from the server. The server can be compiled and run, you can connect to it using telnet commands 101, 102, 103, 104 and 105 (see below) which are
fully implemented. The code is available and can be downloaded from Moodle. In its implemented state, the server requires a password file. The current location of the
password file must be configured in the file MsgProtocol.java. The default location is "h:\\pwd.txt" – this needs to be changed with the current location on your local machine.

## 2.1. The Current Text Messaging Protocol

Here is the definition for the commands that a client may send to the message server:

	{QUERY} ::= {LOGIN} | {SEND} | {OTHER}
	{LOGIN} ::= "101" {CR} {U} {CR} {PWD} {CR}
	{SEND} ::= "103" {CR} {U} {CR} {U} {CR} {M} {CR}
	{OTHER} ::= {ID} {CR} {U} {CR}
	{U} ::= username
	{PWD} ::= password
	{ID} ::= "102" | "104" | "105" | "106"
	{M} ::= [<CHAR> | <CHAR> {M}]
	{CR} := <CRLF>

Note that <CHAR> is any ASCII character excluding control characters and <CRLF> is carriage return followed by line feed which is "\r\n" in java. Here is a key to the client
command IDs:

|   ID|Translation   |Example   |
| ------------ | ------------ | ------------ |
|101|Login Command   | "101 \r\n tony \r\n"  |
|102| Logout | "102 \r\n tony \r\n"   |
|103|Send Command   |"103 \r\n tony \r\n A messege to fred    |
|104|Do I have waiting messages?  |"104 \r\n toy \r\n"   |
|105|Get next messages   |"105 \r\n tony \r\n"   |
|106|Get all messages   |"106 \r\n tony \r\n"   |

## 2.2. Current Available Server responses
	{RESPONSE} ::= {OK} | {MSG} | {ERROR}
	{OK} ::= "200" {CR}
	{MSG} ::= "201" {CR} <n> {CR} {MSGDATA}
	{ERROR} ::= "500" {CR} {M} {CR}
	{D} ::= Date
	{MSGDATA} ::= {U} {CR} {D} {CR} {M} {CR} [{MSGDATA}]

Note < n > is an integer with a value greater than zero
Here is a key to the server response IDs:

| ID  |Translation   |
| ------------ | ------------ |
|200   | Request successfully executed   |
|201   |n messages are being send, each message consists of the senders username,the date the message was sent and the message content   |
|500   |An error response and the accompanying message should explain the error   |

Examples of Java strings for valid responses are:

"200 \r\n"

"201 \r\n 1 \r\n fred \r\n Thu Feb 27 14:04:32 GMT 2003 \r\n A Message \r\n"
or,

if there is more than one message:

"201 \r\n 2 \r\n fred \r\n Thu Feb 27 14:06:32 GMT 2003 \r\n Hello from Fred \r\n"

"fred \r\n Thu Feb 27 14:07:47 GMT 2003 \r\n Another Message \r\n"

"500 \r\n Incorrect Password \r\n"

Commands 101, 102, 103, 104 will all be followed by either an {OK} response or
an {ERROR} response.

Note that for request 104, an {OK} response is a confirmation that there are messages
waiting and an {ERROR} response means

No messages are waiting.

Commands 105 and 106 will be followed by eithera {MSG} response or an {ERROR}
response.

## 2.3. The server

Main server classes are:

**MsgProtocol**
The MsgProtocol class defines constants for all of the client and server command identifiers.
Use these constants in your code.

**LoginCommand and LogoutCommand**
The LoginCommand and LogoutCommand classes process the login and logout
commands. You should look at these classes to see how to implement a command.

**CommandFactory**
This class exists to read the command identifier sent by the client and return a command class
that can process the rest of the command. For example, if the command identifier is 101, a LoginCommand class will be returned. Currently command classes are only implemented for logging in and logging out.

**Handling messages – MessageCollection**
The classes you need for storing messages on the server has been fully implemented. These are called Message and MessageCollection. The Message class models the individual messages, has sender, date and content. The date is added to a message automatically.

The send command construct a new Message object whenever a new message is sent and that message is then added to the MessageCollection. The MessageCollection class provides a way of holding all the messages that have been sent but not yet read.

Those messages can be accessed using the recipient name as a key. The class has all the
methods you will need for adding a new message to the collection, retrieving messages for a
particular user and finding out how many messages a particular user currently has waiting in
the collection. Note that getting a message from the collection also removes it from the
collection.

**Server connection - classes**
The MsgSvrConnection class handles an individual connection between the server and a
client. It has methods to set the current user and get the current user. Another method returns the MessageServer object because that object provides access to the message collection. The MessageServer class is the main server class. It knows about the MessageCollection and each MsgSvrConnection.

# 3. Command classes to be Completed
Your group task is to implement the networking code for the remaining classes below. This should include defining the protocols that handles these commands that your server must process. These commands are:

1. Get all messages (106) – Partially implemented

2. Devise and implement a protocol and command(s) that allows the user to register for the text-messaging server. Basic registration would include username and password. Full registration can include Date-of-birth, telephone information, address details – Not
implemented

3. Devise and implement a protocol and command(s) that allows the user to update registration details – Not implemented

4. Devise and implement a protocol and command(s) that allows the user to set reminder for particular event. Users can be notified of reminders by (text message, sound alert or a
popup window) – Not implemented

5. Devise and implement a protocol that allows the user to access and update reminders.
Users can be notified of reminders by (text message, sound alert or a popup window) –
Not implemented

6. Extend the implementation of the text-messaging server to enable JDBC connection for at least two command classes.

