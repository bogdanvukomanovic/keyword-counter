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

        while (true) {

            /* Blocks if queue is empty */
            ScanningJob job = jobs.dequeue();

            switch (job.getType()) {

                case FILE:
                    fileScanner.scan((FileScanningJob) job);
                    continue;

                case WEB:
                    webScanner.scan((WebScanningJob) job);
                    continue;

                case POISON:
                    fileScanner.stop();
                    webScanner.stop();
                    break;

                default:
                    throw new IllegalStateException(">> Unexpected value: " + job.getType());

            }

            System.out.println(">> FINISHED: Job Dispatcher");
            break;
        }

    }

}
