package app;

import directory_crawler.DirectoryCrawlerWorker;
import job_queue.ScanningJob;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Main {

//    public enum Command {
//
//        ADD_DIRECTORY("ad"),
//        ADD_WEB("aw");
//
//        private String command;
//
//        Command(String command) {
//            this.command = command;
//        }
//
//        public String getCommand() {
//            return command;
//        }
//
//    }

    private class Command {

        final static String ADD_DIRECTORY = "ad";

    }

    private void CLI() {

        Scanner sc = new Scanner(System.in);

        while (true) {

            String line = sc.nextLine();

            switch (line) {

                case Command.ADD_DIRECTORY:
                    System.out.println("Command: Add directory");
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + line);

            }

        }

    }
    public static void main(String[] args) {

        Configuration.load();

        List<String> directories = new ArrayList<String>();
        BlockingQueue<ScanningJob> jobs = new LinkedBlockingQueue<ScanningJob>(); /* TODO: Maybe change to ArrayBlockingQueue<>? */

        DirectoryCrawlerWorker DCWorker = new DirectoryCrawlerWorker(directories, jobs);
        Thread DCWorkerThread = new Thread(DCWorker);

        DCWorkerThread.start();

        try {
            DCWorkerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Main finished successfully.");

    }

}
