package job_queue.job;

import directory_crawler.Corpus;

import java.util.Map;
import java.util.concurrent.Future;

public class FileScanningJob implements ScanningJob {

    private final ScanType scanType = ScanType.FILE;
    private final Corpus corpus;

    public FileScanningJob(Corpus corpus) {
        this.corpus = corpus;
    }

    @Override
    public ScanType getType() {
        return scanType;
    }

    @Override
    public String getQuery() {
        return null;
    }

    @Override
    public Future<Map<String, Integer>> initiate() {
        return null;
    }

}
