package job_queue.job;

import java.util.Map;
import java.util.concurrent.Future;

public class WebScanningJob implements ScanningJob {

    private final ScanType scanType = ScanType.FILE;
    private final String URL;

    public WebScanningJob(String URL) {
        this.URL = URL;
    }

    @Override
    public ScanType getType() {
        return scanType;
    }

}
