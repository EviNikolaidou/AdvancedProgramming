package msgServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class EditReminderCommand implements Command {
	private BufferedReader in;
	private BufferedWriter out;
	private MsgSvrConnection conn;

	public EditReminderCommand(BufferedReader in, BufferedWriter out, MsgSvrConnection serverConn) {
		this.in = in;
		this.out = out;
		this.conn = serverConn;
	}

	public void execute() throws IOException {
		if (conn.getCurrentUser() == null) {
			// makes sure the user is logged in before editing a reminder,
			// returns an error message if not
			(new ErrorCommand(in, out, conn, "Error - You must be logged in to edit a reminder")).execute();
		} else {
			out.write("Would you like to view, edit or delete a reminder ('view', 'edit', 'delete' or 'cancel')?\r\n");
			out.flush();
			String reminderChoice = in.readLine();
			if (reminderChoice.equals("view") || reminderChoice.equals("edit") || reminderChoice.equals("delete")) {
				try {
					out.write("\r\nReminders:\r\n");
					FileReader file = new FileReader(conn.getCurrentUser() + "_reminders.txt");
					BufferedReader buff = new BufferedReader(file);
					boolean eof = false;
					int i = 1;
					while (!eof) {
						String reminderLine = buff.readLine();
						if (reminderLine == null)
							eof = true;
						else
							// writes out the number of the reminder line and the reminder itself
							out.write(i + ". " + reminderLine + "\r\n");
						i++;
					}
					out.write("\r\n");
					out.flush();
					buff.close();
					file.close();
				} catch (IOException e) {
					// writes error message if no reminder file is found for the
					// user
					out.write("Error - " + conn.getCurrentUser()
							+ "_reminders.txt file not found. You must create a reminder before you can edit one.\r\n");
					out.flush();
				}
				if (reminderChoice.equals("edit")) {
					out.write(
							"Enter the number for the reminder you want to edit (e.g. for the 1st reminder you would enter 1):\r\n");
					out.flush();
					int editLine = Integer.parseInt(in.readLine());
					out.write("Would you like to edit the date or message? ('date', 'time' or 'message'):\r\n");
					out.flush();
					String editWhich = in.readLine();
					if (editWhich.equals("date")) {
						out.write("Would you like to edit the day, month or year? ('day', 'month' or 'year'):\r\n");
						out.flush();
						String editWhichDate = in.readLine();
						if (editWhichDate.equals("day")) {
							out.write("Enter a day (1-31):\r\n");
							out.flush();
							int newDay = Integer.parseInt(in.readLine());
							if (newDay >= 1 && newDay <= 31) {
								// declares an array that the reminders will be
								// held in
								ArrayList<String> holding = new ArrayList<String>();
								FileReader file = new FileReader(conn.getCurrentUser() + "_reminders.txt");
								BufferedReader buff = new BufferedReader(file);
								boolean eof = false;
								while (!eof) {
									String reminderLine = buff.readLine();
									if (reminderLine == null)
										eof = true;
									else
										// adds each line of the reminders file
										// to an array
										holding.add(reminderLine);
								}
								PrintWriter clearFileContents = new PrintWriter(
										conn.getCurrentUser() + "_reminders.txt");
								// clears the file
								clearFileContents.print("");
								FileWriter fw = new FileWriter(conn.getCurrentUser() + "_reminders.txt", true);
								BufferedWriter bw = new BufferedWriter(fw);
								PrintWriter writeToFile = new PrintWriter(bw);
								try {
									String lineToBeEdited = holding.get(editLine - 1);
									Character d1 = lineToBeEdited.charAt(7);
									String d1test = d1.toString();
									if (d1test.equals(" ")) {
										holding.add(
												lineToBeEdited.substring(0, 8) + newDay + lineToBeEdited.substring(9));
									} else {
										holding.add(
												lineToBeEdited.substring(0, 7) + newDay + lineToBeEdited.substring(9));
									}
									holding.remove(editLine - 1);
									Collections.sort(holding);
									// re-writes reminders with edited day
									out.write("New file contains:\r\n");
									int i = 1;
									for (String line : holding) {
										writeToFile.write(line + "\r\n");
										out.write(i + ". " + line + "\r\n");
										i++;
									}
									writeToFile.close();
									out.write("\r\n");
									out.flush();
									buff.close();
									file.close();
									fw.close();
									bw.close();
									clearFileContents.close();
								} catch (IndexOutOfBoundsException e) {
									out.write("There is nothing left to delete\r\n");
									out.flush();
								}
								// calls the reminder (in case order has changed
								// by alteration of day)
								try {
									TimerTest logInReminders = new TimerTest(in, out, conn);
									logInReminders.execute();
								} catch (IOException | IndexOutOfBoundsException e) {
									out.write("You have no reminders set.\r\n");
									out.flush();
								}
							} else {
								(new ErrorCommand(in, out, conn, "Error - day entered was not between 1 and 31."))
										.execute();
							}
						} else if (editWhichDate.equals("month")) {
							out.write("Enter a month (1-12):\r\n");
							out.flush();
							int newMonth = Integer.parseInt(in.readLine());
							if (newMonth >= 1 && newMonth <= 12) {
								// declares an array that the reminders will be
								// held in
								ArrayList<String> holding = new ArrayList<String>();
								FileReader file = new FileReader(conn.getCurrentUser() + "_reminders.txt");
								BufferedReader buff = new BufferedReader(file);
								boolean eof = false;
								while (!eof) {
									String reminderLine = buff.readLine();
									if (reminderLine == null)
										eof = true;
									else
										// adds each line of the reminders file
										// to an array
										holding.add(reminderLine);
								}
								PrintWriter clearFileContents = new PrintWriter(
										conn.getCurrentUser() + "_reminders.txt");
								// clears the file
								clearFileContents.print("");
								FileWriter fw = new FileWriter(conn.getCurrentUser() + "_reminders.txt", true);
								BufferedWriter bw = new BufferedWriter(fw);
								PrintWriter writeToFile = new PrintWriter(bw);
								try {
									String lineToBeEdited = holding.get(editLine - 1);
									Character m2 = lineToBeEdited.charAt(6);
									String m2test = m2.toString();
									if (m2test.equals(" ")) {
										holding.add(lineToBeEdited.substring(0, 5) + newMonth
												+ lineToBeEdited.substring(6));
									} else {
										holding.add(lineToBeEdited.substring(0, 5) + newMonth
												+ lineToBeEdited.substring(7));
									}
									holding.remove(editLine - 1);
									Collections.sort(holding);
									out.write("New file contains:\r\n");
									int i = 1;
									for (String line : holding) {
										writeToFile.write(line + "\r\n");
										out.write(i + ". " + line + "\r\n");
										i++;
									}
									writeToFile.close();
									out.write("\r\n");
									out.flush();
									buff.close();
									file.close();
									fw.close();
									bw.close();
									clearFileContents.close();
								} catch (IndexOutOfBoundsException e) {
									out.write("Error - No reminders available.\r\n");
									out.flush();
								}
								try {
									// calls the reminder (in case order has
									// changed by alteration of month)
									TimerTest logInReminders = new TimerTest(in, out, conn);
									logInReminders.execute();
								} catch (IOException | IndexOutOfBoundsException e) {
									out.write("You have no reminders set.\r\n");
									out.flush();
								}
							} else {
								(new ErrorCommand(in, out, conn, "Error - Month entered was not between 1 and 12."))
										.execute();
							}
						} else if (editWhichDate.equals("year")) {
							out.write("Enter a year:\r\n");
							out.flush();
							int newYear = Integer.parseInt(in.readLine());
							if (newYear >= 2017) {
								// declares an array that the reminders will be
								// held in
								ArrayList<String> holding = new ArrayList<String>();
								FileReader file = new FileReader(conn.getCurrentUser() + "_reminders.txt");
								BufferedReader buff = new BufferedReader(file);
								boolean eof = false;
								while (!eof) {
									String reminderLine = buff.readLine();
									if (reminderLine == null)
										eof = true;
									else
										// adds each line of the reminders file
										// to an array
										holding.add(reminderLine);
								}
								PrintWriter clearFileContents = new PrintWriter(
										conn.getCurrentUser() + "_reminders.txt");
								// clears the file
								clearFileContents.print("");
								FileWriter fw = new FileWriter(conn.getCurrentUser() + "_reminders.txt", true);
								BufferedWriter bw = new BufferedWriter(fw);
								PrintWriter writeToFile = new PrintWriter(bw);
								try {
									String lineToBeEdited = holding.get(editLine - 1);
									holding.add(newYear + lineToBeEdited.substring(4));
									holding.remove(editLine - 1);
									Collections.sort(holding);
									out.write("New file contains:\r\n");
									int i = 1;
									for (String line : holding) {
										writeToFile.write(line + "\r\n");
										out.write(i + ". " + line + "\r\n");
										i++;
									}
									writeToFile.close();
									out.write("\r\n");
									out.flush();
									buff.close();
									file.close();
									fw.close();
									bw.close();
									clearFileContents.close();
								} catch (IndexOutOfBoundsException e) {
									out.write("There is nothing left to delete\r\n");
									out.flush();
								}
								// calls the reminder (in case order has changed
								// by alteration of year)
								try {
									TimerTest logInReminders = new TimerTest(in, out, conn);
									logInReminders.execute();
								} catch (IOException | IndexOutOfBoundsException e) {
									out.write("You have no reminders set.\r\n");
									out.flush();
								}
							} else {
								(new ErrorCommand(in, out, conn, "Error - Year entered is in the past.")).execute();
							}
						} else {
							(new ErrorCommand(in, out, conn, "Error - Response not recognised.")).execute();
						}
					} else if (editWhich.equals("message")) {
						out.write("What would you like the new message to say?\r\n");
						out.flush();
						String newMessage = in.readLine();
						// declares an array that the reminders will be held in
						ArrayList<String> holding = new ArrayList<String>();
						FileReader file = new FileReader(conn.getCurrentUser() + "_reminders.txt");
						BufferedReader buff = new BufferedReader(file);
						boolean eof = false;
						while (!eof) {
							String reminderLine = buff.readLine();
							if (reminderLine == null)
								eof = true;
							else
								// adds each line of the reminders file to an
								// array
								holding.add(reminderLine);
						}
						PrintWriter clearFileContents = new PrintWriter(conn.getCurrentUser() + "_reminders.txt");
						// clears the file
						clearFileContents.print("");
						FileWriter fw = new FileWriter(conn.getCurrentUser() + "_reminders.txt", true);
						BufferedWriter bw = new BufferedWriter(fw);
						PrintWriter writeToFile = new PrintWriter(bw);
						try {
							String lineToBeEdited = holding.get(editLine - 1);

							int whereEqualsIs = lineToBeEdited.indexOf("=");
							holding.add(lineToBeEdited.substring(0, whereEqualsIs + 2) + newMessage);
							holding.remove(editLine - 1);
							Collections.sort(holding);
							out.write("New file contains:\r\n");
							int i = 1;
							for (String line : holding) {
								writeToFile.write(line + "\r\n");
								out.write(i + ". " + line + "\r\n");
								i++;
							}
							writeToFile.close();
							out.write("\r\n");
							out.flush();
							buff.close();
							file.close();
							fw.close();
							bw.close();
							clearFileContents.close();
						} catch (IndexOutOfBoundsException e) {
							out.write("There is nothing left to delete\r\n");
							out.flush();
						}
						try {
							TimerTest logInReminders = new TimerTest(in, out, conn);
							logInReminders.execute();
						} catch (IOException | IndexOutOfBoundsException e) {
							out.write("You have no reminders set.\r\n");
							out.flush();
						}
					} else if (editWhich.equals("time")) {
						out.write("New time:\r\n");
						out.write("Please enter the hour (0-23):\r\n");
						out.flush();
						int newHour = Integer.parseInt(in.readLine());
						out.write("Please enter the minutes (0-59):\r\n");
						out.flush();
						int newMins = Integer.parseInt(in.readLine());
						if ((newHour >= 0 && newHour <= 23) && (newMins >= 0 && newMins <= 59)) {
							// declares an array that the reminders will be held
							// in
							ArrayList<String> holding = new ArrayList<String>();
							FileReader file = new FileReader(conn.getCurrentUser() + "_reminders.txt");
							BufferedReader buff = new BufferedReader(file);
							boolean eof = false;
							while (!eof) {
								String reminderLine = buff.readLine();
								if (reminderLine == null)
									eof = true;
								else
									// adds each line of the reminders file to
									// an array
									holding.add(reminderLine);
							}
							PrintWriter clearFileContents = new PrintWriter(conn.getCurrentUser() + "_reminders.txt");
							// clears the file
							clearFileContents.print("");
							FileWriter fw = new FileWriter(conn.getCurrentUser() + "_reminders.txt", true);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter writeToFile = new PrintWriter(bw);
							try {
								String lineToBeEdited = holding.get(editLine - 1);
								int whereColonIs = lineToBeEdited.indexOf(":");
								int whereEqualsIs = lineToBeEdited.indexOf("=");
								Character h1 = lineToBeEdited.charAt(whereColonIs - 2);
								String h1test = h1.toString();
								if (h1test.equals(" ")) {
									holding.add(lineToBeEdited.substring(0, whereColonIs - 1) + newHour + ":" + newMins
											+ lineToBeEdited.substring(whereEqualsIs - 1));
								} else {
									holding.add(lineToBeEdited.substring(0, whereColonIs - 2) + newHour + ":" + newMins
											+ lineToBeEdited.substring(whereEqualsIs - 1));
								}
								holding.remove(editLine - 1);
								Collections.sort(holding);
								out.write("New file contains:\r\n");
								int i = 1;
								for (String line : holding) {
									writeToFile.write(line + "\r\n");
									out.write(i + ". " + line + "\r\n");
									i++;
								}
								writeToFile.close();
								out.write("\r\n");
								out.flush();
								buff.close();
								file.close();
								fw.close();
								bw.close();
								clearFileContents.close();
							} catch (IndexOutOfBoundsException e) {
								out.write("There is nothing left to delete\r\n");
								out.flush();
							}
							// calls the reminder (in case order has changed by
							// alteration of time)
							try {
								TimerTest logInReminders = new TimerTest(in, out, conn);
								logInReminders.execute();
							} catch (IOException | IndexOutOfBoundsException e) {
								out.write("You have no reminders set.\r\n");
								out.flush();
							}
						} else {
							(new ErrorCommand(in, out, conn,
									"Error - You must enter between 0 and 23 for the hours and between 0 and 59 for the minutes."))
											.execute();
						}
					} else {
						(new ErrorCommand(in, out, conn, "Error - Response not recognised.")).execute();
					}
				} else if (reminderChoice.equals("delete")) {
					out.write(
							"Enter the number for the reminder you want to delete (e.g. for the 1st reminder you would enter 1).\r\n");
					out.flush();
					int deleteLine = Integer.parseInt(in.readLine());
					// declares an array that the reminders will be held in
					ArrayList<String> holding = new ArrayList<String>();
					FileReader file = new FileReader(conn.getCurrentUser() + "_reminders.txt");
					BufferedReader buff = new BufferedReader(file);
					boolean eof = false;
					while (!eof) {
						String reminderLine = buff.readLine();
						if (reminderLine == null)
							eof = true;
						else
							// adds each line of the reminders file to an array
							holding.add(reminderLine);
					}
					PrintWriter clearFileContents = new PrintWriter(conn.getCurrentUser() + "_reminders.txt");
					// clears the file
					clearFileContents.print("");
					FileWriter fw = new FileWriter(conn.getCurrentUser() + "_reminders.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter writeToFile = new PrintWriter(bw);
					try {
						holding.remove(deleteLine - 1);
						Collections.sort(holding);
						out.write("New file contains:\r\n");
						int i = 1;
						for (String line : holding) {
							writeToFile.write(line + "\r\n");
							out.write(i + ". " + line + "\r\n");
							i++;
						}
						writeToFile.close();
						out.write("\r\n");
						out.flush();
						buff.close();
						file.close();
						fw.close();
						bw.close();
						clearFileContents.close();
					} catch (IndexOutOfBoundsException e) {
						out.write("There is nothing left to delete\r\n");
						out.flush();
					}
					try {
						TimerTest logInReminders = new TimerTest(in, out, conn);
						logInReminders.execute();
					} catch (IOException | IndexOutOfBoundsException e) {
						out.write("You have no reminders set.\r\n");
						out.flush();
					}
				} else if (reminderChoice.equals("View")) {
					out.write("200\r\n");
					out.flush();
				}
				try {
					TimerTest logInReminders = new TimerTest(in, out, conn);
					logInReminders.execute();
				} catch (IOException | IndexOutOfBoundsException e) {
					out.write("You have no reminders set.\r\n");
					out.flush();
				}
			} else if (reminderChoice.equals("cancel")) {
				// Allows the user to leave the EditReminderCommand without
				// getting an error message
				out.write("200\r\n");
				out.flush();
			} else {
				(new ErrorCommand(in, out, conn, "Error - Response not recognised.")).execute();
			}
		}
	}
}