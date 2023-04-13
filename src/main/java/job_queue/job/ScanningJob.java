package job_queue.job;


public abstract class ScanningJob {

    protected ScanType scanType;

    public ScanType getType() {
        return scanType;
    }

}
