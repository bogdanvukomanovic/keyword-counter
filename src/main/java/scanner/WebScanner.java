package scanner;

import job_queue.JobQueue;
import job_queue.job.WebScanningJob;
import scanner.task.WebScanTask;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class WebScanner {

    ExecutorService threadPool = Executors.newCachedThreadPool();
    private JobQueue jobs;
    private Set<String> cache;


    public WebScanner(JobQueue jobs) {
        this.jobs = jobs;
        this.cache = new HashSet<String>();
    }

    public void scan(WebScanningJob job) {

        if (cache.contains(job.getURL())) {
            return;
        }

        cache.add(job.getURL());

        Future<Map<String, Integer>> result = threadPool.submit(new WebScanTask(job.getURL(), job.getHopCount(), jobs));

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
