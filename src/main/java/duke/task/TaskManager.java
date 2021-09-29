package duke.task;
import duke.exception.InvalidInputException;

import java.io.IOException;
import java.util.ArrayList;
import duke.FileIOManager;

public class TaskManager {
    private static ArrayList<Todo> tasks = new ArrayList<Todo>();

    public static void addTodo(String userInput) {
        try {
            if (userInput.length()<5) {
                throw new InvalidInputException("OOPS!!! Description of todo cannot be empty :(");
            }
            Todo newTask = new Todo(userInput);
            tasks.add(newTask);
            printTaskAddedConfirmation(newTask);
            updateFile();
        } catch (InvalidInputException e){
            System.out.println(e.toString().substring(23));
        }
    }

    public static void addDeadline(String userInput) {
        try {
            if (userInput.length()<9){
                throw new InvalidInputException("OOPS!!! Description of deadline cannot be empty :(");
            }
            if (!userInput.contains("/by ")){
                throw new InvalidInputException("OOPS!! Please input date of deadline");
            }
            int startIndexOfTask = userInput.indexOf(' ')+1;
            int endIndexOfTask = userInput.indexOf('/') - 1;

            int startIndexOfDeadline = userInput.indexOf('/') + 4;
            String taskName = userInput.substring(startIndexOfTask, endIndexOfTask);
            String deadline = userInput.substring(startIndexOfDeadline);
            Deadline newTask = new Deadline(taskName, deadline);
            tasks.add(newTask);
            printTaskAddedConfirmation(newTask);
            updateFile();
        } catch (InvalidInputException e){
            System.out.println(e.toString().substring(23));
        }
    }

    public static void addEvent(String userInput) {
        try {
            if (userInput.length()<6) {
                throw new InvalidInputException("OOPS!!! Description of event cannot be empty :(");
            }
            if (!userInput.contains("/at ")){
                throw new InvalidInputException("OOPS!! Please input time of event");
            }
            int startIndexOfTask = userInput.indexOf(' ')+1;
            int endIndexOfTask = userInput.indexOf('/') - 1;
            int startIndexOfDate = userInput.indexOf('/') + 4;
            String taskName = userInput.substring(startIndexOfTask, endIndexOfTask);
            String deadline = userInput.substring(startIndexOfDate);
            Event newTask = new Event(taskName, deadline);
            tasks.add(newTask);
            printTaskAddedConfirmation(newTask);
            updateFile();
        } catch (InvalidInputException e){
            System.out.println(e.toString().substring(23));
        }

    }

    public static void printTasks() {
        if (tasks.size() == 0) {
            System.out.println("\tNo tasks added");
            return;
        }
        int count = 1;
        for (Todo task : tasks) {
            System.out.println("\tHere are the tasks in your list");
            System.out.println("\t"+count+ "." + task);
            count++;
        }
    }

    public static void markDone(String userInput) {
        int indexOfTaskNumber = userInput.indexOf(' ') + 1;
        int taskNumber = Integer.parseInt(userInput.substring(indexOfTaskNumber));
        if (taskNumber > tasks.size() || taskNumber <= 0) {
            System.out.println("\tNo such task");
            return;
        }
        Todo task = tasks.get(taskNumber - 1);
        task.setIsDone();
        System.out.println("\tNice! I've marked this task as done:");
        System.out.println("\t\t" + task);
        updateFile();
    }

    public static void deleteTask(String userInput){
        int indexOfTaskNumber = userInput.indexOf(' ') + 1;
        int taskNumber = Integer.parseInt(userInput.substring(indexOfTaskNumber));
        if (taskNumber > tasks.size() || taskNumber <= 0) {
            System.out.println("\tNo such task");
            return;
        }
        Todo task = tasks.get(taskNumber - 1);
        tasks.remove(taskNumber-1);
        System.out.println("\tNoted. I've removed this task:");
        System.out.println("\t\t"+task.toString());
        System.out.println("\t Now you have "+tasks.size()+" tasks in the list.");
        updateFile();
    }

    public static void findTaskWithSubstring(String userInput){
        int startIndexOfTargetSubstring = userInput.indexOf(' ') + 1;
        String targetSubString = userInput.substring(startIndexOfTargetSubstring);
        ArrayList<Todo> listOfTasksWithSubstring = new ArrayList<Todo>();
        for (Todo task:tasks){
            if (task.getName().contains(targetSubString)){
                listOfTasksWithSubstring.add(task);
            }
        }
        printTasksWithSubstring(listOfTasksWithSubstring);
    }

    private static void printTasksWithSubstring(ArrayList<Todo> listOfTasks){
        if (listOfTasks.size() == 0) {
            System.out.println("\tNo tasks with target substring");
            return;
        }
        System.out.println("\tHere are the matching tasks in your list:");
        int count = 1;
        for (Todo task : listOfTasks) {
            System.out.println("\t"+count+ "." + task);
            count++;
        }
    }

    private static void printTaskAddedConfirmation(Todo task) {
        System.out.println("\tGot it. I've added this task:");
        System.out.println("\t\t" + task);
        System.out.format("\tNow you have %d tasks in the list.\n", tasks.size());
    }

    private static void updateFile(){
        try {
            FileIOManager.write(tasks);
        } catch (IOException e){
            System.out.println(e);
        }
    }
    public static void convertTaskStringToTasks(ArrayList<String> tasksString){
        if (tasksString==null){
            return;
        }
        if (tasksString.isEmpty()){
            return;
        }
        int startIndexOfTask;
        int endIndexOfTask;
        int startIndexOfDate;
        int endIndexOfDate;
        String date;
        String taskName;
        for (String taskString: tasksString){
            char type = taskString.charAt(3);
            switch(type) {
            case 'T':
                startIndexOfTask = 9;
                taskName = taskString.substring(startIndexOfTask);
                Todo newTodo = new Todo(taskName);
                tasks.add(newTodo);
                if (taskString.charAt(6)=='X'){
                    newTodo.setIsDone();
                }
                continue;
            case 'D':
                startIndexOfTask = 9;
                endIndexOfTask = taskString.indexOf('(')-1;
                startIndexOfDate = endIndexOfTask+6;
                endIndexOfDate = taskString.indexOf(')');
                taskName = taskString.substring(startIndexOfTask, endIndexOfTask);
                date = taskString.substring(startIndexOfDate,endIndexOfDate);
                Deadline newDeadline = new Deadline(taskName, date);
                tasks.add(newDeadline);
                if (taskString.charAt(6)=='X'){
                    newDeadline.setIsDone();
                }
                continue;
            case'E':
                startIndexOfTask = 9;
                endIndexOfTask = taskString.indexOf('(')-1;
                startIndexOfDate = endIndexOfTask+6;
                endIndexOfDate = taskString.indexOf(')');
                taskName = taskString.substring(startIndexOfTask, endIndexOfTask);
                date = taskString.substring(startIndexOfDate,endIndexOfDate);
                Event newEvent = new Event(taskName, date);
                tasks.add(newEvent);
                if (taskString.charAt(6)=='X'){
                    newEvent.setIsDone();
                }
                continue;
            }

        }
    }
}
