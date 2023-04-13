package job_queue.job;

public class PoisonJob extends ScanningJob {

    public PoisonJob() {
        this.scanType = ScanType.POISON;
    }



}
