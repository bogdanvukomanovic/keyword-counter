package job_dispatcher;

import job_queue.JobQueue;
import job_queue.job.FileScanningJob;
import job_queue.job.ScanningJob;
import scanner.FileScanner;

public class JobDispatcherWorker implements Runnable {

    private JobQueue jobs;
    private FileScanner fileScanner;

    public JobDispatcherWorker(JobQueue jobs, FileScanner fileScanner) {
        this.jobs = jobs;
        this.fileScanner = fileScanner;
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
                    continue;
                default:
                    throw new IllegalStateException("Unexpected value: " + job.getType());

            }

        }

    }

}
