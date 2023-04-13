package job_queue;

import job_queue.job.Job;

import java.util.concurrent.BlockingQueue;

public class JobQueue {

    private BlockingQueue<Job> jobs;

    public JobQueue(BlockingQueue<Job> jobs) {
        this.jobs = jobs;
    }

    public void enqueue(Job job) {

        try {
            jobs.put(job);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public Job dequeue() {

        try {
            return jobs.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
