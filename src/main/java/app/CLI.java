package app;

import job_queue.job.PoisonJob;
import job_queue.job.WebJob;
import org.jsoup.Jsoup;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CLI {

    private static final String logo =
                    " ___  __     ___       __    ________   ________                ________   ________    _________   ________     \n"                                            +
                    "|\\  \\|\\  \\  |\\  \\     |\\  \\ |\\   __  \\ |\\   ___ \\              |\\   ____\\ |\\   ___  \\ |\\___   ___\\|\\   __  \\    \n"                        +
                    "\\ \\  \\/  /__\\ \\  \\    \\ \\  \\\\ \\  \\|\\  \\\\ \\  \\_|\\ \\   _________ \\ \\  \\___| \\ \\  \\\\ \\  \\\\|___ \\  \\_|\\ \\  \\|\\  \\   \n"        +
                    " \\ \\   ___  \\\\ \\  \\  __\\ \\  \\\\ \\   _  _\\\\ \\  \\ \\\\ \\ |\\_________\\\\ \\  \\     \\ \\  \\\\ \\  \\    \\ \\  \\  \\ \\   _  _\\  \n"         +
                    "  \\ \\  \\\\ \\  \\\\ \\  \\|\\__\\_\\  \\\\ \\  \\\\  \\ \\ \\  \\_\\\\ \\\\|_________| \\ \\  \\____ \\ \\  \\\\ \\  \\    \\ \\  \\  \\ \\  \\\\  \\ \n"   +
                    "   \\ \\__\\\\ \\__\\\\ \\____________\\\\ \\__\\\\ _\\ \\ \\_______\\             \\ \\_______\\\\ \\__\\\\ \\__\\    \\ \\__\\  \\ \\__\\\\ _\\ \n"          +
                    "    \\|__| \\|__| \\|____________| \\|__|\\|__| \\|_______|              \\|_______| \\|__| \\|__|     \\|__|   \\|__|\\|_ |\n";

    private static String command = "";
    private static String argument = "";
    private static String type = "";
    private static String target = "";

    private class Command {

        final static String ADD_DIRECTORY = "ad";
        final static String ADD_WEB = "aw";
        final static String GET = "get";
        final static String QUERY = "query";
        final static String SUMMARY = "summary";
        final static String CLEAR_FILE_SUMMARY = "cfs";
        final static String CLEAR_WEB_SUMMARY = "cws";
        final static String STOP = "stop";

        static Set<String> COMMANDS_WITH_PARAMS = new HashSet<>() {
            {
                add(ADD_DIRECTORY.toString()); add(ADD_WEB.toString());
                add(GET.toString()); add(QUERY.toString());
            }
        };

        static Set<String> COMMANDS_WITHOUT_PARAMS = new HashSet<>() {
            {
                add(STOP.toString());
                add(CLEAR_FILE_SUMMARY.toString()); add(CLEAR_WEB_SUMMARY.toString());
            }
        };

    }

    private class Type {

        final static String FILE = "file";
        final static String WEB = "web";

    }

    private static void welcome() {

        System.out.println(logo);

        System.out.println("List of commands: ");
        System.out.println("\t- ad <directory>          - Add directory");
        System.out.println("\t- aw <url>                - Add web");
        System.out.println("\t- get <type>|<target>     - Blocking, get corpus/domain result");
        System.out.println("\t- get <type>|summary      - Blocking, get file/web summary result");
        System.out.println("\t- query <type>|<target>   - Non-blocking, get corpus/domain result");
        System.out.println("\t- query <type>|summary    - Non-Blocking, get file/web summary result");
        System.out.println("\t- cws                     - Clear web summary");
        System.out.println("\t- cfs                     - Clear file summary");
        System.out.println("\t- stop                    - Quit");

        System.out.println("---------------------------------------");

    }

    private static boolean parse(String line) {

        String[] xs = line.trim().split("\\s");

        if (xs.length == 1) {

            command = xs[0];

            if (Command.COMMANDS_WITHOUT_PARAMS.contains(command)) {
                return true;
            } else {
                return false;
            }

        }

        if (!Command.COMMANDS_WITH_PARAMS.contains(xs[0])) {
            return false;
        }


        if (xs.length != 2) {
            return false;
        }

        command = xs[0].trim();
        argument = xs[1].trim();

        if (command.equals(Command.GET) || command.equals(Command.QUERY)) {

            if (argument.startsWith("file|") || argument.startsWith("web|")) {

                xs = argument.trim().split("\\|");

                if (xs.length != 2) {
                    return false;
                }

                type = xs[0];
                target = xs[1];

            } else {
                return false;
            }

        }

        return true;
    }

    private static boolean isDirectoryValid(String directory) {

        if (!Files.exists(Path.of(Configuration.DATA_ROOT + directory))) {
            System.out.println(">> Error: Data folder '" + directory + "' doesn't exist.");
            return false;
        } else {
            return true;
        }

    }

    private static boolean isURLValid(String URL) {

        try {
            Jsoup.connect(URL).get();
        } catch (Exception e) {
            System.out.println(">> Error: Invalid URL. " + e.getMessage());
            return false;
        }

        return true;
    }


    static void loop() {

        welcome();

        Scanner sc = new Scanner(System.in);

        while (true) {

            if (!parse(sc.nextLine())) {
                System.out.println(">> Error: Invalid command format.");
                continue;
            }

            if (target.equals(Command.SUMMARY)) {

                switch (command) {

                    case Command.GET:
                        System.out.println(Controller.resultRetriever.getSummary(type));
                        continue;

                    case Command.QUERY:
                        System.out.println(Controller.resultRetriever.querySummary(type));
                        continue;
                }

            }

            switch (command) {

                case Command.ADD_DIRECTORY:

                    if (isDirectoryValid(argument)) {
                        Controller.directories.add(argument);
                    }

                    continue;

                case Command.ADD_WEB:

                    if (isURLValid(argument)) {
                        Controller.jobs.enqueue(new WebJob(argument, Configuration.HOP_COUNT));
                    }

                    continue;

                case Command.GET:

                    System.out.println(Controller.resultRetriever.getResult(type, target));
                    continue;

                case Command.QUERY:

                    System.out.println(Controller.resultRetriever.queryResult(type, target));
                    continue;

                case Command.CLEAR_FILE_SUMMARY:
                    Controller.resultRetriever.clearFileSummary();
                    continue;

                case Command.CLEAR_WEB_SUMMARY:
                    Controller.resultRetriever.clearWebSummary();
                    continue;

                case Command.STOP:
                    Controller.DCWorker.stop();
                    Controller.jobs.enqueue(new PoisonJob());
                    Controller.resultRetriever.stop();
                    break;

                default:
                    throw new IllegalStateException(">> Error: Unknown command " + command);

            }
            break;
        }

    }


}
