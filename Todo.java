import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Todo {

	private static String usage = "\nUsage :-\n" + "$ ./todo add \"todo item\"  # Add a new todo\n"
			+ "$ ./todo ls               # Show remaining todos\n" + "$ ./todo del NUMBER       # Delete a todo\n"
			+ "$ ./todo done NUMBER      # Complete a todo\n" + "$ ./todo help             # Show usage\n"
			+ "$ ./todo report           # Statistics";

	private static Path path = Paths.get("todo.txt");

	public static void main(String args[]) {

		if (args.length == 0) {
			System.out.println(usage);
			System.exit(0);

		} else if (args[0].equals("help")) {
			System.out.println(usage);

		} else if (args[0].equals("add")) {

			if (args.length < 2) {
				System.out.println("Error: Missing todo string. Nothing added!");
			} else {
				String sentence = args[1];

				additemtoTodoList(sentence);
			}

		} else if (args[0].equals("ls")) {
			showtodoList();

		} else if (args[0].equals("del")) {

			if (args.length < 2) {
				System.out.println("Error: Missing NUMBER for deleting todo.");
			} else {
				int delNumber = Integer.parseInt(args[1]);
				deletetodoItem(delNumber - 1);
			}
		} else if (args[0].equals("done")) {

			if (args.length < 2) {
				System.out.println("Error: Missing NUMBER for marking todo as done.");
			} else {
				int donePosition = Integer.parseInt(args[1]);
				markAsCompleted(donePosition - 1);
			}
		} else if (args[0].equals("report")) {
			report();
		}
	}

	private static void deletetodoItem(int position) {

		try {
			List<String> lines = Files.readAllLines(path);

			if (position >= 0 && position < lines.size()) {

				lines.remove(position);

				System.out.println("Deleted todo #" + (position + 1));

				FileWriter writer1;
				try {
					writer1 = new FileWriter("todo.txt");
					for (String str : lines) {
						writer1.write(str + System.lineSeparator());
					}
					writer1.close();

				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Error: todo #" + (position + 1) + " does not exist. Nothing deleted.");
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static void report() {

		try {

			Path donePath = Paths.get("done.txt");

			List<String> todoList = Files.readAllLines(path);
			List<String> doneList = Files.readAllLines(donePath);

			Date todayDate = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String todayString = formatter.format(todayDate);

			System.out.println(
					todayString + " " + "Pending : " + (todoList.size()) + " " + "Completed : " + doneList.size());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void markAsCompleted(int position) {

		try {

			List<String> lines = Files.readAllLines(path);

			if (position >= 0 && position < lines.size()) {

				String doneItem = lines.get(position);

				Date date = Calendar.getInstance().getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String todayDate = formatter.format(date);

				BufferedWriter writer = new BufferedWriter(new FileWriter("done.txt", true));
				writer.write("x " + todayDate + " " + doneItem);
				writer.newLine();
				writer.close();

				System.out.println("Marked todo #" + (position + 1) + " as done.");

				lines.remove(position);

				FileWriter writer1 = new FileWriter("todo.txt");

				for (String str : lines) {
					writer1.write(str + System.lineSeparator());
				}
				writer1.close();

			} else {
				System.out.println("Error: todo #" + (position + 1) + " does not exist.");
			}

		} catch (

		IOException e) {
			e.printStackTrace();
			System.out.println("not found");
		}
	}

	private static void additemtoTodoList(String addTodo) {

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("todo.txt", true));

			writer.write(addTodo);
			writer.newLine();
			writer.close();

			System.out.println("Added todo: " + "\"" + addTodo + "\"");

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private static void showtodoList() {

		try {
			File f = new File("todo.txt");
			List<String> lines = Files.readAllLines(path);

			if (lines.size() != 0 && f.exists()) {
				for (int i = lines.size() - 1; i >= 0; i--) {
					System.out.println("[" + String.valueOf(i + 1) + "] " + lines.get(i));
				}

			} else {
				System.out.println("There are no pending todos!");
			}

		} catch (IOException e) {
			System.out.println("There are no pending todos!");

			// e.printStackTrace();
		}
	}

}
