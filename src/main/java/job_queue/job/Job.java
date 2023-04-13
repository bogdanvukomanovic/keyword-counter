package job_queue.job;


public abstract class Job {

    protected ScanType scanType;

    public ScanType getType() {
        return scanType;
    }

}
