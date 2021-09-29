package duke.userinterface;
import java.util.Scanner;
import duke.FormatLines;
import duke.command.Command;
import duke.task.TaskManager;

public class UserInterface {
    private static Scanner in = new Scanner(System.in);
    private static String userInput;

    public static void talkToUser() {
        boolean validUserInput;
        do {
            getUserInput();
            validUserInput = processUserInput();
            if (!validUserInput) {
                return;
            }
            System.out.println(FormatLines.divider);

        } while (true);
    }

    private static void getUserInput() {
        System.out.println();
        userInput = in.nextLine();
        userInput=userInput.strip();
        System.out.println(FormatLines.divider);
    }

    private static boolean processUserInput() {
        try {
            Command userCommand = retrieveUserCommand();
            switch (userCommand) {
            case DONE:
                TaskManager.markDone(userInput);
                return true;
            case LIST:
                TaskManager.printTasks();
                return true;
            case TODO:
                TaskManager.addTodo(userInput);
                return true;
            case EVENT:
                TaskManager.addEvent(userInput);
                return true;
            case DEADLINE:
                TaskManager.addDeadline(userInput);
                return true;
            case DELETE:
                TaskManager.deleteTask(userInput);
                return true;
            case FIND:
                TaskManager.findTaskWithSubstring(userInput);
                return true;
            case BYE:
                return false;
            default:
                return true;
            }
        } catch (NullPointerException e){
            return true;
        }
    }

    private static Command retrieveUserCommand() {
        String commandString;
        int indexOfSpace = userInput.indexOf(' ');
        if (indexOfSpace == -1) {
            commandString = userInput.substring(0);
        } else {
            commandString = userInput.substring(0, indexOfSpace);
        }
        try {
            Command userCommand = Command.valueOf(commandString.toUpperCase());
            return userCommand;
        } catch (IllegalArgumentException e) {
            System.out.println("OOPS!!! I'm sorry but I don't know what that means");
            return null;
        }
    }


}
