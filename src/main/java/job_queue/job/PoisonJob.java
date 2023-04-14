package job_queue.job;

public class PoisonJob extends Job {

    public PoisonJob() {
        this.jobType = JobType.POISON;
    }

}
