package job_queue.job;

public class PoisonJob implements ScanningJob {

    private final ScanType scanType = ScanType.POISON;

    @Override
    public ScanType getType() {
        return scanType;
    }

}
