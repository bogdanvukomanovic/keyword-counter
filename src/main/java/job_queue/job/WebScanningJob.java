package job_queue.job;

public class WebScanningJob implements ScanningJob {

    private final ScanType scanType = ScanType.WEB;
    private final String URL;
    private final int hopCount;

    public WebScanningJob(String URL, int hopCount) {
        this.URL = URL;
        this.hopCount = hopCount;
    }

    @Override
    public ScanType getType() {
        return scanType;
    }

    public String getURL() {
        return URL;
    }

    public int getHopCount() {
        return hopCount;
    }

}
