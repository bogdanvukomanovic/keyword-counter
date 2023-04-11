package app;

import directory_crawler.DirectoryCrawlerWorker;

import job_dispatcher.JobDispatcherWorker;
import job_queue.JobQueue;
import job_queue.job.ScanningJob;
import job_queue.job.WebScanningJob;
import result_retriever.ResultRetriever;
import scanner.FileScanner;
import scanner.WebScanner;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;


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
        final static String ADD_WEB = "aw";
        final static String GET = "get";
        final static String QUERY = "query";
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

        /* Shared memory structures */
        List<String> directories = new CopyOnWriteArrayList<>();
        JobQueue jobs = new JobQueue(new LinkedBlockingQueue<ScanningJob>()); /* TODO: Maybe change to ArrayBlockingQueue<>? */
        Map<String, Future<Map<String, Integer>>> results = new ConcurrentHashMap<>();

        /* Threads */
        DirectoryCrawlerWorker DCWorker = new DirectoryCrawlerWorker(directories, jobs);
        Thread DCWorkerThread = new Thread(DCWorker);
        DCWorkerThread.start();

        FileScanner fileScanner = new FileScanner(results);
        WebScanner webScanner = new WebScanner(jobs, results);
        ResultRetriever resultRetriever = new ResultRetriever(results);

        JobDispatcherWorker JDWorker = new JobDispatcherWorker(jobs, fileScanner, webScanner);
        Thread JDWorkerThread = new Thread(JDWorker);

        JDWorkerThread.setDaemon(true); /* TODO: TBD */
        JDWorkerThread.start();



        /* Command Line Interface */
        Scanner sc = new Scanner(System.in);

        while (true) {

            String line = sc.nextLine();

            /* TODO: Bad parsing... To be changed... */
            String[] split = line.trim().split("\\s");

            String command = split[0];
            String argument = split[1];


            switch (command) {

                case Command.ADD_DIRECTORY:
                    System.out.println(">> Command: Add directory");
                    directories.add(argument);
                    continue;

                case Command.ADD_WEB:
                    System.out.println(">> Command: Add web");
                    jobs.enqueue(new WebScanningJob(argument, Configuration.HOP_COUNT));
                    continue;

                case Command.GET:
                    continue;

                case Command.QUERY:
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
