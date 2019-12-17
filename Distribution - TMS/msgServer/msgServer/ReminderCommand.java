package msgServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class ReminderCommand implements Command {
	private BufferedReader in;
	private BufferedWriter out;
	private MsgSvrConnection conn;

	public ReminderCommand(BufferedReader in, BufferedWriter out, MsgSvrConnection serverConn) {
		this.in = in;
		this.out = out;
		this.conn = serverConn;
	}

	public void execute() throws IOException {
		if (conn.getCurrentUser() == null) {
			// makes sure the user is logged in before creating a reminder,
			// returns an error message if not
			(new ErrorCommand(in, out, conn, "Error - You must be logged in to set a reminder")).execute();
		} else {

			out.write("What date do you want the reminder to occur?\r\n");
			out.write("Please enter the day (1-31): ");
			// asks the user to input a day between 1-31
			out.flush();
			int day = Integer.parseInt(in.readLine());
			// reads the user input day and stores it as an integer
			if (day > 0 && day <= 31) {
				// makes sure that the day entered is valid
				out.write("Please enter the month (1-12): ");
				out.flush();
				// asks the user to input a month
				int month = Integer.parseInt(in.readLine());
				// reads the user input month and stores it as an integer
				if ((month == 1 && day <= 31) || (month == 2 && day <= 29) || (month == 3 && day <= 31)
						|| (month == 4 && day <= 30) || (month == 5 && day <= 31) || (month == 6 && day <= 30)
						|| (month == 7 && day <= 31) || (month == 8 && day <= 31) || (month == 9 && day <= 30)
						|| (month == 10 && day <= 31) || (month == 11 && day <= 30) || (month == 12 && day <= 31)) {
					// checks both that the month entered is valid and that the
					// number of days matches the month entered
					out.write("Please enter the year (yyyy): ");
					// asks the user to input a year
					out.flush();
					int year = Integer.parseInt(in.readLine());
					// reads the user input year and stores it as an integer
					Boolean isLeapYear = false;
					if ((year % 4 == 0) && year % 100 != 0) {
						// if statement that works out whether the year entered
						// is a leap year or not
						isLeapYear = true;
					} else if (year % 400 == 0) {
						isLeapYear = true;
					} else {
						isLeapYear = false;
					}
					if ((year >= 2017 && month != 2)
							|| ((year >= 2017 && month == 2) && (day < 30 && isLeapYear == true))
							|| ((year >= 2017 && month == 2) && (day < 29 && isLeapYear == false))) {
						/*
						 * Checks that the year entered is valid If the year
						 * entered is 'Feb' and a leap year then any day entered
						 * below 30 is valid If the year entered is 'Feb' and is
						 * not a leap year then any day below 29 is valid
						 */
						out.write("You entered a valid date \r\n");
						out.write("What time would you like the reminder?\r\n");
						out.write("Please enter the hour: (0-23)\r\n");
						out.flush();
						int hour = Integer.parseInt(in.readLine());
						if (hour >= 0 && hour <= 23) {
							out.write("Please enter the minutes: (0-59)\r\n");
							out.flush();
							int minute = Integer.parseInt(in.readLine());
							if (minute >= 0 && hour <= 59) {
								out.write("What would you like the reminder to say? \r\n");
								out.flush();
								String reminderMessage = in.readLine();
								try (FileWriter fw = new FileWriter(conn.getCurrentUser() + "_reminders.txt", true);
										/*
										 * creates a .txt file in the format
										 * username_reminders with the boolean
										 * value true allowing data to be
										 * appended to the file
										 */
										BufferedWriter bw = new BufferedWriter(fw);
										PrintWriter output = new PrintWriter(bw)) {
									output.println(
											year + " " + month + " " + day + " " + hour + ":" + minute + " = " + reminderMessage);
								} catch (IOException e) {
									(new ErrorCommand(in, out, conn, "")).execute();
								}
								// creates an array used to store the
								// user_reminders.txt
								// file
								ArrayList<String> sortListByDate = new ArrayList<String>();
								FileReader file = new FileReader(conn.getCurrentUser() + "_reminders.txt");
								BufferedReader buff = new BufferedReader(file);
								boolean eof = false;
								while (!eof) {
									String reminderLine = buff.readLine();
									if (reminderLine == null)
										eof = true;
									else
										sortListByDate.add(reminderLine);
								}
								PrintWriter clearFileContents = new PrintWriter(
										conn.getCurrentUser() + "_reminders.txt");
								clearFileContents.print("");
								FileWriter fw = new FileWriter(conn.getCurrentUser() + "_reminders.txt", true);
								BufferedWriter bw = new BufferedWriter(fw);
								PrintWriter writeToFile = new PrintWriter(bw);
								/*
								 * sorts the array holding the reminders so that
								 * the soonest date is at the top
								 */
								Collections.sort(sortListByDate);
								// writes contents of array to file
								for (String line : sortListByDate) {
									writeToFile.write(line + "\r\n");
								}
								// closes streams
								writeToFile.close();
								buff.close();
								file.close();
								fw.close();
								bw.close();
								clearFileContents.close();
								try {
									TimerTest logInReminders = new TimerTest(in, out, conn);
									logInReminders.execute();
								} catch (IOException e) {
									out.write("You have no reminders set.\r\n");
									out.flush();
								}
							} else {
								// if the minutes entered were invalid this
								// error message occurs
								(new ErrorCommand(in, out, conn, "Error - Invalid response for minutes")).execute();
							}
						} else {
							// if the hour entered was invalid this error
							// message occurs
							(new ErrorCommand(in, out, conn, "Error - Invalid response for hours.")).execute();
						}
					} else if (year < 2017) {
						// if the year was before 2017 then a reminder can't be
						// set and an error message is returned
						(new ErrorCommand(in, out, conn, "Error - " + year + " is in the past.")).execute();
					} else if (isLeapYear == false && day == 29 && month == 2) {
						/*
						 * if the year was valid but the 29th of Feb was entered
						 * and it wasn't a leap year then an error message is
						 * returned stating that it's not a leap year
						 */
						(new ErrorCommand(in, out, conn, "Error - " + year + " is not a leap year.")).execute();
					} else {
						/*
						 * if the year entered does not apply to previous
						 * conditions and was invalid a generic error statement
						 * is returned
						 */
						(new ErrorCommand(in, out, conn, "Error - " + year + " is not a valid year.")).execute();
					}
				} else if (month >= 1 && month <= 12) {
					/*
					 * if month entered was valid but the day was invalid (e.g.
					 * the 31st of April) then an error message is returned
					 */
					(new ErrorCommand(in, out, conn, "Error - the chosen month does not have " + day + " days in it."))
							.execute();
				} else {
					/*
					 * if the month entered was not a recognised month an error
					 * message is returned
					 */
					(new ErrorCommand(in, out, conn,
							"Error - month entered was not recognized as a valid month. Remember to enter a number between 1 and 12."))
									.execute();
				}
			} else {
				/*
				 * returns an error message if day entered is outside of the
				 * limits 1 - 31
				 */
				(new ErrorCommand(in, out, conn, "You entered a day outside of the limits (1-31).")).execute();
			}
		}
	}
}
