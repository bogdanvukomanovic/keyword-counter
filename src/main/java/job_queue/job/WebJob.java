package job_queue.job;

public class WebJob extends Job {

    private final String URL;
    private final int hopCount;

    public WebJob(String URL, int hopCount) {
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
