package msgServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;

public class RegistrationUpdate implements Command {
	private BufferedReader in;
	private BufferedWriter out;
	private MsgSvrConnection conn;

	public RegistrationUpdate(BufferedReader in, BufferedWriter out, MsgSvrConnection serverConn) {
		this.in = in;
		this.out = out;
		this.conn = serverConn;
	}

	public void execute() throws IOException {
		out.write("Please enter your username:\r\n");
		out.flush();
		String username = in.readLine();
		out.write("Please enter your password:\r\n");
		out.flush();
		String password = in.readLine();
		conn.userMsg("LoginCommand: Got user: " + username + " (" + password + ")");
		conn.userMsg("LoginCommand: Server thinks passwd is " + conn.getServer().getUserPassword(username));
		if (password != null && username != null &&
		// and the password is correct...
				password.equals(conn.getServer().getUserPassword(username))) {
			conn.setCurrentUser(username);
			out.write("200\r\n");
			out.flush();
			out.write("Do you want to update your registration details? (y/n):\r\n");
			out.flush();
			String yesOrNo = in.readLine();
			while (true) {
				if (yesOrNo.equals("y")) {
					out.write("Please enter your new password: ");
					out.flush();
					password = in.readLine();
					out.write("Please enter your new phone number: ");
					out.flush();
					String phoneNum = in.readLine();
					out.write("Please enter your new address: ");
					out.flush();
					String addressDetails = in.readLine();

					out.write("You will be asked your Date of Birth in three sections\r\n");
					out.write("Please enter the day (1-31): ");
					// asks the user to input a day between 1-31
					out.flush();
					int day = Integer.parseInt(in.readLine());
					if (day > 0 && day <= 31) {
						// makes sure that the day entered is valid
						out.write("Please enter the month ('Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',"
								+ " 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' - warning: case sensitive): ");
						out.flush();
						// asks the user to input a month
						String month = in.readLine();
						// reads the user input month and stores it as a
						// String
						if ((month.equals("Jan") && day <= 31) || (month.equals("Feb") && day <= 29)
								|| (month.equals("Mar") && day <= 31) || (month.equals("Apr") && day <= 30)
								|| (month.equals("May") && day <= 31) || (month.equals("Jun") && day <= 30)
								|| (month.equals("Jul") && day <= 31) || (month.equals("Aug") && day <= 31)
								|| (month.equals("Sep") && day <= 30) || (month.equals("Oct") && day <= 31)
								|| (month.equals("Nov") && day <= 30) || (month.equals("Dec") && day <= 31)) {
							// checks both that the month entered is
							// valid and that the
							// number of days matches the month entered
							out.write("Please enter the year (yyyy): ");
							// asks the user to input a year
							out.flush();
							int year = Integer.parseInt(in.readLine());
							// reads the user input year and stores it
							// as an integer
							Boolean isLeapYear = false;
							if ((year % 4 == 0) && year % 100 != 0) {
								// if statement that works out whether
								// the year entered
								// is a leap year or not
								isLeapYear = true;
							} else if (year % 400 == 0) {
								isLeapYear = true;
							} else {
								isLeapYear = false;
							}
							if (year > 2017) {
								/*
								 * if the year was after 2017 then it is not a
								 * valid year to be born in as you can't be born
								 * in the future
								 */
								(new ErrorCommand(in, out, conn, "Error - " + year + " is in the future.")).execute();
							} else if (year < 1900) {
								// catches if a user enters a year too
								// old for them to be born in
								(new ErrorCommand(in, out, conn,
										"Error - nobody is alive who was born in the year " + year)).execute();
							} else if (month.equals("Feb") == false
									|| ((month.equals("Feb") && isLeapYear == true) && day < 30)
									|| ((month.equals("Feb") && isLeapYear == false) && day < 29)) {
								/*
								 * Checks that the year entered is valid If the
								 * year entered is 'Feb' and a leap year then
								 * any day entered below 30 is valid If the year
								 * entered is 'Feb' and is not a leap year then
								 * any day below 29 is valid
								 */
								
								/*
								 * The code below writes the username and
								 * password to the "pwd.txt" file
								 */
								try (FileWriter fw = new FileWriter("pwd.txt", true);
										BufferedWriter bw = new BufferedWriter(fw);
										PrintWriter output = new PrintWriter(bw)) {
									output.println(username + "=" + password);
								} catch (IOException e) {
									(new ErrorCommand(in, out, conn, "")).execute();
								}
								/*
								 * The code below writes the username, password,
								 * phone number, date of birth and address
								 * details to the "UserDetails.txt" file
								 */
								try (FileWriter fw = new FileWriter("UserDetails.txt", true);
										BufferedWriter bw = new BufferedWriter(fw);
										PrintWriter output = new PrintWriter(bw)) {
									output.println(username + "," + password);
									output.println(phoneNum + "," + day + "/" + month + "/" + year);
									output.println(addressDetails + "\r\n");
								} catch (IOException e) {
									(new ErrorCommand(in, out, conn, "")).execute();
								}
								out.write("200\r\n");
								out.write("Please re-open the program to log in using the new details\r\n");
								out.flush();
								break;
							} else if (isLeapYear == false && month.equals("Feb") && day == 29) {
								/*
								 * if the year was valid but the 29th of Feb was
								 * entered and it wasn't a leap year then an
								 * error message is returned stating that it's
								 * not a leap year
								 */
								(new ErrorCommand(in, out, conn, "Error - " + year + " is not a leap year.")).execute();
							} else {
								/*
								 * if the year entered does not apply to
								 * previous conditions and was invalid a generic
								 * error statement is returned
								 */
								(new ErrorCommand(in, out, conn, "Error - " + year + " is not a valid year."))
										.execute();
							}

						} else if (month.equals("Jan") || month.equals("Feb") || month.equals("Mar")
								|| month.equals("Apr") || month.equals("May") || month.equals("Jun")
								|| month.equals("Jul") || month.equals("Aug") || month.equals("Sep")
								|| month.equals("Oct") || month.equals("Nov") || month.equals("Dec")) {
							/*
							 * if month entered was valid but the day was
							 * invalid (e.g. the 31st of April) then an error
							 * message is returned
							 */
							(new ErrorCommand(in, out, conn,
									"Error - " + month + " does not have " + day + " days in it.")).execute();
						} else {
							/*
							 * if the month entered was not a recognised month
							 * an error message is returned
							 */
							(new ErrorCommand(in, out, conn,
									"Error - " + month
											+ " not recognized as a valid month. Remember to enter the first three "
											+ "letters of the month with the first one being a capital letter."))
													.execute();
						}
					} else {
						/*
						 * returns an error message if day entered is outside of
						 * the limits 1 - 31
						 */
						(new ErrorCommand(in, out, conn, "You entered a day outside of the limits (1-31).")).execute();
					}
				} else if (yesOrNo.equals("n")) {
					break;
				} else {
					out.write("Invalid response entered. Please enter either 'y' or 'n'. \r\n");
					out.flush();
					yesOrNo = in.readLine();
				}
			}
		} else {
			(new ErrorCommand(in, out, conn, "Incorrect Password")).execute();
		}
	}
}