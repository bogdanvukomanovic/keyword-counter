package job_queue;

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
        return null;
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
