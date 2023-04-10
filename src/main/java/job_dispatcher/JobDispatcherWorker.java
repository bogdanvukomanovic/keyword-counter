package job_dispatcher;

import job_queue.JobQueue;
import job_queue.job.FileScanningJob;
import job_queue.job.ScanningJob;
import job_queue.job.WebScanningJob;
import scanner.FileScanner;
import scanner.WebScanner;

public class JobDispatcherWorker implements Runnable {

    private JobQueue jobs;
    private FileScanner fileScanner;
    private WebScanner webScanner;

    public JobDispatcherWorker(JobQueue jobs, FileScanner fileScanner, WebScanner webScanner) {
        this.jobs = jobs;
        this.fileScanner = fileScanner;
        this.webScanner = webScanner;
    }

    @Override
    public void run() {

        /* TODO: Cancellation with Poison pill mechanism */
        /* Continue in WEB and FILE cases - Break in POISON case */
        while (true) {

            /* Blocks if queue is empty */
            ScanningJob job = jobs.dequeue();

            switch (job.getType()) {

                case FILE:
                    System.out.println("Job dispatcher: FILE SCANNING JOB");
                    fileScanner.scan((FileScanningJob) job);
                    continue;
                case WEB:
                    System.out.println("Job dispatcher: WEB SCANNING JOB");
                    webScanner.scan((WebScanningJob) job);
                    continue;
                default:
                    throw new IllegalStateException("Unexpected value: " + job.getType());

            }

        }

    }

}
