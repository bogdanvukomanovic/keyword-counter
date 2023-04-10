package job_queue.job;

import directory_crawler.Corpus;


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

    public Corpus getCorpus() {
        return corpus;
    }

}
