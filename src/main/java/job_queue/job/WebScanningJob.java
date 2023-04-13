package job_queue.job;

public class WebScanningJob extends ScanningJob {

    private final String URL;
    private final int hopCount;

    public WebScanningJob(String URL, int hopCount) {
        this.scanType = ScanType.WEB;
        this.URL = URL;
        this.hopCount = hopCount;
    }

    public String getURL() {
        return URL;
    }

    public int getHopCount() {
        return hopCount;
    }

}
