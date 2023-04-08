package job_dispatcher;

import job_queue.JobQueue;
import job_queue.job.ScanningJob;

public class JobDispatcherWorker implements Runnable {

    private JobQueue jobs;

    public JobDispatcherWorker(JobQueue jobs) {
        this.jobs = jobs;
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
