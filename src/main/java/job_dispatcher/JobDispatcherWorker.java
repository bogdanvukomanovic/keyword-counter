package job_dispatcher;

import job_queue.JobQueue;
import job_queue.job.FileJob;
import job_queue.job.Job;
import job_queue.job.WebJob;
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
            Job job = jobs.dequeue();

            switch (job.getType()) {

                case FILE:
                    fileScanner.scan((FileJob) job);
                    continue;

                case WEB:
                    webScanner.scan((WebJob) job);
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
