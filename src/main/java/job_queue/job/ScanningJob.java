package job_queue.job;

import java.util.Map;
import java.util.concurrent.Future;

public interface ScanningJob {

    ScanType getType();

}
