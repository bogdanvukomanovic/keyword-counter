package app;

import directory_crawler.DirectoryCrawlerWorker;
import job_dispatcher.JobDispatcherWorker;
import job_queue.JobQueue;
import job_queue.job.ScanningJob;
import result_retriever.ResultRetriever;
import result_retriever.result.Result;
import scanner.FileScanner;
import scanner.WebScanner;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller {

    /* Shared memory structures */
    static List<String> directories;
    static JobQueue jobs;
    static Map<String, Result> results;

    /* Thread pools */
    static FileScanner fileScanner;
    static WebScanner webScanner;
    static ResultRetriever resultRetriever;

    /* Workers */
    static DirectoryCrawlerWorker DCWorker;
    static JobDispatcherWorker JDWorker;

    /* Threads */
    static Thread DCWorkerThread;
    static Thread JDWorkerThread;

    static void initialize() {

        /* Shared memory structures */
        directories = new CopyOnWriteArrayList<>();
        jobs = new JobQueue(new LinkedBlockingQueue<ScanningJob>()); /* TODO: Maybe change to ArrayBlockingQueue<>? */
        results = new ConcurrentHashMap<>();

        /* Thread pools */
        fileScanner = new FileScanner(results);
        webScanner = new WebScanner(jobs, results);
        resultRetriever = new ResultRetriever(results);

        /* Workers */
        DCWorker = new DirectoryCrawlerWorker(directories, jobs);
        JDWorker = new JobDispatcherWorker(jobs, fileScanner, webScanner);

        /* Threads */
        DCWorkerThread = new Thread(DCWorker);
        JDWorkerThread = new Thread(JDWorker);

        /* Start threads */
        DCWorkerThread.start();
        JDWorkerThread.setDaemon(true); /* TODO: TBD */
        JDWorkerThread.start();

    }


}
