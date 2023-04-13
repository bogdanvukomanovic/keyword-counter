package job_queue.job;


public abstract class Job {

    protected JobType jobType;

    public JobType getType() {
        return jobType;
    }

}
