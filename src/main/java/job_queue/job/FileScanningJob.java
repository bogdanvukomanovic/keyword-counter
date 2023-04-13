package job_queue.job;

import directory_crawler.Corpus;


public class FileScanningJob extends ScanningJob {

    private final Corpus corpus;

    public FileScanningJob(Corpus corpus) {
        this.scanType = ScanType.FILE;
        this.corpus = corpus;
    }

    public Corpus getCorpus() {
        return corpus;
    }

}
