package scanner;

import job_queue.job.FileJob;
import result_retriever.ResultRetriever;
import result_retriever.result.FileResult;
import result_retriever.result.Result;
import scanner.task.FileScanTask;

import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class FileScanner {

    private ForkJoinPool forkJoinPool = new ForkJoinPool();

    private Map<String, Result> results;

    public FileScanner(Map<String, Result> results) {
        this.results = results;
    }

    public void scan(FileJob job) {

        System.out.println(">> Starting file scan for: file|" + job.getCorpus().getName());
        Future<Map<String, Integer>> result = forkJoinPool.submit(new FileScanTask(job.getCorpus().getTexts()));

        results.put(job.getCorpus().getName(), new FileResult(result, job.getCorpus().getName()));
        ResultRetriever.fileSummaryCache.set(null); /* TODO: FileScanner should have reference to ResultRetriever instead of just results from ResultRetriever? */

    }

    public void stop() {
        forkJoinPool.shutdown();
        System.out.println(">> FINISHED: File Scanner");
    }

}
