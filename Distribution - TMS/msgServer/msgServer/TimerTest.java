// class that is called when a user logs/when a new reminder is created/a reminder is edited
// This class handles the reminders making use of a thread that runs in the background whilst
// the program is running

package msgServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;

import java.awt.Toolkit;

public class TimerTest implements Command {
	private BufferedReader in;
	private BufferedWriter out;
	private MsgSvrConnection conn;

	public TimerTest(BufferedReader in, BufferedWriter out, MsgSvrConnection serverConn) {
		this.in = in;
		this.out = out;
		this.conn = serverConn;
	}

	private volatile static boolean isRunning = true;

	public class AlarmClock {
		public void checkAlarm(final int a, final int b, final int c, final int d, int e, String f) {
			Thread reminderThread = new Thread() {
				public void run() {
					while (isRunning == true) {
						Calendar cal = new GregorianCalendar();
						int hours = cal.get(Calendar.HOUR_OF_DAY);
						int mins = cal.get(Calendar.MINUTE);
						int days = cal.get(Calendar.DAY_OF_MONTH);
						int month = cal.get(Calendar.MONTH);
						int year = cal.get(Calendar.YEAR);
						// if the time and date match that of the reminders
						if (a == hours && b == mins && c == days && d == month && e == year) {
							// plays a beep to notify user of reminder
							Toolkit.getDefaultToolkit().beep();
							// pop-up screen with reminder message
							JOptionPane.showMessageDialog(null, f);
							ArrayList<String> holding = new ArrayList<String>();
							FileReader file = null;
							try {
								file = new FileReader(conn.getCurrentUser() + "_reminders.txt");
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							BufferedReader buff = new BufferedReader(file);
							boolean eof = false;
							while (!eof) {
								String reminderLine = null;
								try {
									reminderLine = buff.readLine();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								if (reminderLine == null)
									eof = true;
								else
									holding.add(reminderLine);
							}
							PrintWriter clearFileContents = null;
							try {
								clearFileContents = new PrintWriter(conn.getCurrentUser() + "_reminders.txt");
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							clearFileContents.print("");
							FileWriter fw = null;
							try {
								fw = new FileWriter(conn.getCurrentUser() + "_reminders.txt", true);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter writeToFile = new PrintWriter(bw);
							// removes the reminder just executed from the file
							holding.remove(0);
							Collections.sort(holding);
							for (String line : holding) {
								writeToFile.write(line + "\r\n");
							}
							writeToFile.close();
							try {
								buff.close();
								file.close();
								fw.close();
								bw.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							clearFileContents.close();
							try {
								TimerTest logInReminders = new TimerTest(in, out, conn);
								logInReminders.execute();
							} catch (IOException e1) {
								try {
									out.write("You have no reminders set.\r\n");
									out.flush();
								} catch (IOException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}
							}
							// calls the timer again so the next reminder is
							// started this is particularly useful if there is
							// more than one reminder at the same time
							try {
								TimerTest logInReminders = new TimerTest(in, out, conn);
								logInReminders.execute();
							} catch (IOException | IndexOutOfBoundsException e) {
								try {
									out.write("You have no reminders set.\r\n");
									out.flush();
								} catch (IOException e1) {
								}
							}
							break;
						}
					}
				}
			};
			reminderThread.setPriority(Thread.MIN_PRIORITY);
			reminderThread.start();
		}
	}

	public static void kill() {
		isRunning = false;
	}

	public void execute() throws IOException {

		if (conn.getCurrentUser() == null) {
			// makes sure the user is logged in before creating a reminder,
			// returns an error message if not
			(new ErrorCommand(in, out, conn, "Error - You must be logged in to set a reminder")).execute();
		} else {
			// kills the current thread so only one reminder can be running at
			// once (prevents the same reminder being called multiple times)
			TimerTest.kill();
			AlarmClock reminderAlert = new AlarmClock();
			ArrayList<String> Reminder = new ArrayList<String>();
			try {
				FileReader file = new FileReader(conn.getCurrentUser() + "_reminders.txt");
				BufferedReader buff = new BufferedReader(file);
				boolean eof = false;
				while (!eof) {
					String reminderLine = buff.readLine();
					if (reminderLine == null)
						eof = true;
					else
						Reminder.add(reminderLine);
				}
				buff.close();
				file.close();
			} catch (IOException e) {
			}

			// gets the next upcoming reminder and sets it as a String
			String Reminder1 = Reminder.get(0);
			Character y1 = Reminder1.charAt(0);
			Character y2 = Reminder1.charAt(1);
			Character y3 = Reminder1.charAt(2);
			Character y4 = Reminder1.charAt(3);
			String yTotal;
			StringBuilder SByear = new StringBuilder();
			SByear.append(y1);
			SByear.append(y2);
			SByear.append(y3);
			SByear.append(y4);
			yTotal = SByear.toString();
			// assigns the year of the reminder to year 
			// - one of the inputs for the reminder
			int year = Integer.parseInt(yTotal);

			// gets month
			Character m1 = Reminder1.charAt(5);
			Character m2 = Reminder1.charAt(6);
			String m2test = m2.toString();
			String mTotal;
			if (m2test.equals(" ")) {
				StringBuilder SBmonth = new StringBuilder();
				SBmonth.append(m1);
				mTotal = SBmonth.toString();
			} else {
				StringBuilder SBmonth = new StringBuilder();
				SBmonth.append(m1);
				SBmonth.append(m2);
				mTotal = SBmonth.toString();
			}
			int month = Integer.parseInt(mTotal);

			// gets day
			String dTotal;
			if (m2test.equals(" ")) {
				Character d1 = Reminder1.charAt(7);
				Character d2 = Reminder1.charAt(8);
				String d2test = d2.toString();
				if (d2test.equals(" ")) {
					StringBuilder SBday = new StringBuilder();
					SBday.append(d1);
					dTotal = SBday.toString();
				} else {
					StringBuilder sb2 = new StringBuilder();
					sb2.append(d1);
					sb2.append(d2);
					dTotal = sb2.toString();
				}
				int day = Integer.parseInt(dTotal);

				// gets hour
				String hTotal;
				int indexOfTime = Reminder1.indexOf(":");
				Character h1 = Reminder1.charAt(indexOfTime - 2);
				Character h2 = Reminder1.charAt(indexOfTime - 1);
				String h1test = h1.toString();
				if (h1test.equals(" ")) {
					StringBuilder SBhours = new StringBuilder();
					SBhours.append(h2);
					hTotal = SBhours.toString();
				} else {
					StringBuilder SBhours = new StringBuilder();
					SBhours.append(h1);
					SBhours.append(h2);
					hTotal = SBhours.toString();
				}
				int hour = Integer.parseInt(hTotal);

				// gets mins
				String minTotal;
				Character min1 = Reminder1.charAt(indexOfTime + 1);
				Character min2 = Reminder1.charAt(indexOfTime + 2);
				String min2Test = min2.toString();
				if (min2Test.equals(" ")) {
					StringBuilder SBmins = new StringBuilder();
					SBmins.append(min1);
					minTotal = SBmins.toString();
				} else {
					StringBuilder SBmins = new StringBuilder();
					SBmins.append(min1);
					SBmins.append(min2);
					minTotal = SBmins.toString();
				}
				int minutes = Integer.parseInt(minTotal);

				// code for message
				int indexOfEquals = Reminder1.indexOf("=");
				String reminderMessage = Reminder1.substring(indexOfEquals + 2);
				// sets isRunning to true so thread can be ran
				isRunning = true;

				reminderAlert.checkAlarm(hour, minutes, day, month - 1, year,
						reminderMessage + "\r\n" + hour + ":" + minutes + " - " + day + "/" + month + "/" + year);

				// gets day
			} else if (m2test.equals(" ") == false) {
				Character d1 = Reminder1.charAt(8);
				Character d2 = Reminder1.charAt(9);
				String d2test = d2.toString();
				if (d2test.equals(" ")) {
					StringBuilder SBday = new StringBuilder();
					SBday.append(d1);
					dTotal = SBday.toString();
				} else {
					StringBuilder SBday = new StringBuilder();
					SBday.append(d1);
					SBday.append(d2);
					dTotal = SBday.toString();
				}
				int day = Integer.parseInt(dTotal);

				// gets hour
				String hTotal;
				int indexOfTime = Reminder1.indexOf(":");
				Character h1 = Reminder1.charAt(indexOfTime - 2);
				Character h2 = Reminder1.charAt(indexOfTime - 1);
				String h1test = h1.toString();
				if (h1test.equals(" ")) {
					StringBuilder SBhours = new StringBuilder();
					SBhours.append(h2);
					hTotal = SBhours.toString();
				} else {
					StringBuilder SBhours = new StringBuilder();
					SBhours.append(h1);
					SBhours.append(h2);
					hTotal = SBhours.toString();
				}
				int hour = Integer.parseInt(hTotal);

				// gets mins
				String minTotal;
				Character min1 = Reminder1.charAt(indexOfTime + 1);
				Character min2 = Reminder1.charAt(indexOfTime + 2);
				String min2Test = min2.toString();
				if (min2Test.equals(" ")) {
					StringBuilder SBmins = new StringBuilder();
					SBmins.append(min1);
					minTotal = SBmins.toString();
				} else {
					StringBuilder SBmins = new StringBuilder();
					SBmins.append(min1);
					SBmins.append(min2);
					minTotal = SBmins.toString();
				}
				int minutes = Integer.parseInt(minTotal);

				// code for message
				int indexOfEquals = Reminder1.indexOf("=");
				String reminderMessage = Reminder1.substring(indexOfEquals + 2);
				// sets isRunning to true so thread can be ran
				isRunning = true;

				reminderAlert.checkAlarm(hour, minutes, day, month - 1, year,
						reminderMessage + "\r\n" + hour + ":" + minutes + " - " + day + "/" + month + "/" + year);
			}
		}
	}
}
