package job_queue.job;

import directory_crawler.Corpus;


public class FileJob extends Job {

    private final Corpus corpus;

    public FileJob(Corpus corpus) {
        this.jobType = JobType.FILE;
        this.corpus = corpus;
    }

    public Corpus getCorpus() {
        return corpus;
    }

}
