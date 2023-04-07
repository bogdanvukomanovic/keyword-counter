package app;

import directory_crawler.DirectoryCrawlerWorker;
import job_queue.ScanningJob;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Main {

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
