package scanner;

import job_queue.job.FileScanningJob;
import scanner.task.FileScanTask;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class FileScanner {

    private ForkJoinPool forkJoinPool = new ForkJoinPool();

    public void scan(FileScanningJob job) {

        Future<Map<String, Integer>> result = forkJoinPool.submit(new FileScanTask(job.getCorpus().getTexts()));

        try {

            Map<String, Integer> countedKeywords = result.get();
            System.out.println(countedKeywords);

            /* Two options: */
            /* TODO: Send countedKeywords to Result retriever */
            /* TODO: FileScanTask should send result to Result retriever */

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

}
