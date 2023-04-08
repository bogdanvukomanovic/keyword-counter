package app;

import directory_crawler.DirectoryCrawlerWorker;

import job_dispatcher.JobDispatcherWorker;
import job_queue.JobQueue;
import job_queue.job.ScanningJob;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
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
        final static String STOP = "stop";

    }

//    private void CLI() {
//
//        Scanner sc = new Scanner(System.in);
//
//        while (true) {
//
//            String line = sc.nextLine();
//
//            switch (line) {
//
//                case Command.ADD_DIRECTORY:
//                    System.out.println("Command: Add directory");
//                    break;
//
//                default:
//                    throw new IllegalStateException("Unexpected value: " + line);
//
//            }
//
//        }
//
//    }

    public static void main(String[] args) {

        Configuration.load();

        List<String> directories = new CopyOnWriteArrayList<>();
        JobQueue jobs = new JobQueue(new LinkedBlockingQueue<ScanningJob>()); /* TODO: Maybe change to ArrayBlockingQueue<>? */


        DirectoryCrawlerWorker DCWorker = new DirectoryCrawlerWorker(directories, jobs);
        Thread DCWorkerThread = new Thread(DCWorker);
        DCWorkerThread.start();

        JobDispatcherWorker JDWorker = new JobDispatcherWorker(jobs);
        Thread JDWorkerThread = new Thread(JDWorker);

        JDWorkerThread.setDaemon(true); /* TODO: TBD */
        JDWorkerThread.start();


        Scanner sc = new Scanner(System.in);

        while (true) {

            String line = sc.nextLine();

            /* TODO: Bad parsing... To be changed... */
            String[] split = line.trim().split("\\s");

            String command = split[0];
            String argument = split[1];


            switch (command) {

                case Command.ADD_DIRECTORY:
                    System.out.println(">> " + "Command: Add directory");
                    directories.add(argument);
                    continue;

                case Command.STOP:
                    System.out.println(">> Command: Stop");
                    DCWorker.stop();    /* TODO: DCWorkerThread.join() ? */
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + line);

            }
            break;
        }

        System.out.println("Main finished successfully.");

    }

}
