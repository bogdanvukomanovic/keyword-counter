package job_queue;

import job_queue.job.ScanningJob;

import java.util.concurrent.BlockingQueue;

public class JobQueue {

    private BlockingQueue<ScanningJob> jobs;

    public JobQueue(BlockingQueue<ScanningJob> jobs) {
        this.jobs = jobs;
    }

    public void enqueue(ScanningJob job) {

        try {
            jobs.put(job);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public ScanningJob dequeue() {

        try {
            return jobs.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
