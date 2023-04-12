package app;

import job_queue.job.WebScanningJob;
import result_retriever.response.Response;

import java.util.Scanner;

public class CLI {

    private static String command;
    private static String argument;
    private static String type;
    private static String target;

    private class Command {

        final static String ADD_DIRECTORY = "ad";
        final static String ADD_WEB = "aw";
        final static String GET = "get";
        final static String QUERY = "query";
        final static String SUMMARY = "summary";
        final static String CLEAR_FILE_SUMMARY = "cfs";
        final static String CLEAR_WEB_SUMMARY = "cws";
        final static String STOP = "stop";

    }

    private class Type {

        final static String FILE = "file";
        final static String WEB = "web";

    }

    private static boolean parse(String line) {

        String[] xs = line.trim().split("\\s");

        if (xs.length == 1) {

            command = xs[0];

            if (command.equals(Command.STOP) || command.equals(Command.CLEAR_FILE_SUMMARY) || command.equals(Command.CLEAR_WEB_SUMMARY)) {
                return true;
            } else {
                return false;
            }

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


    static void loop() {

        Scanner sc = new Scanner(System.in);

        Response response;

        while (true) {

            if (!parse(sc.nextLine())) {
                System.out.println("Error: Invalid command format.");
            }

            if (target.equals(Command.SUMMARY)) {

                switch (command) {

                    case Command.GET:
                        response = Controller.resultRetriever.getFileSummary();
                        System.out.println(response);
                        continue;

                    case Command.SUMMARY:
                        continue;
                }

            }

            switch (command) {

                case Command.ADD_DIRECTORY:
                    System.out.println(">> Command: Add directory");
                    /* TODO: Handle if directory exists */
                    Controller.directories.add(argument);
                    continue;

                case Command.ADD_WEB:
                    System.out.println(">> Command: Add web");
                    /* TODO: Handle if web path is valid */
                    Controller.jobs.enqueue(new WebScanningJob(argument, Configuration.HOP_COUNT));
                    continue;

                case Command.GET:

                    response = Controller.resultRetriever.getResult(type, target);
                    System.out.println(response);
                    continue;

                case Command.QUERY:

                    response = Controller.resultRetriever.queryResult(type, target);
                    System.out.println(response);
                    continue;

                case Command.CLEAR_FILE_SUMMARY:
                    Controller.resultRetriever.clearFileSummary();
                    continue;

                case Command.CLEAR_WEB_SUMMARY:
                    continue;

                case Command.STOP:
                    System.out.println(">> Command: Stop");
                    Controller.DCWorker.stop();    /* TODO: DCWorkerThread.join() ? */
                    break;

                default:
                    throw new IllegalStateException("?"); /* TODO: Resolve */

            }
            break;
        }

    }


}
