package scanner;

import job_queue.JobQueue;
import job_queue.job.WebScanningJob;
import org.jsoup.Jsoup;
import result_retriever.ResultRetriever;
import result_retriever.result.Result;
import result_retriever.result.WebResult;
import scanner.task.WebScanTask;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;

public class WebScanner {

    ExecutorService threadPool = Executors.newCachedThreadPool();
    private JobQueue jobs;
    private Map<String, Result> results;
    private Set<String> cache;


    public WebScanner(JobQueue jobs, Map<String, Result> results) {
        this.jobs = jobs;
        this.results = results;
        this.cache = new HashSet<String>();
    }

    public void scan(WebScanningJob job) {

        if (cache.contains(job.getURL())) {
            return;
        }

        try {

            String domain;

            /* Risky operations */
            Jsoup.connect(job.getURL()).get();
            domain = new URI(job.getURL()).getHost();

            cache.add(job.getURL());

            Future<Map<String, Integer>> result = threadPool.submit(new WebScanTask(job.getURL(), job.getHopCount(), jobs));

            results.put(job.getURL(), new WebResult(result, domain));

            /* Each new web page, makes webSummaryCache dirty */
            ResultRetriever.webSummaryCache.set(null);
            /* Also, the corresponding domain is no longer up to date */
            ResultRetriever.webDomainCache.put(domain, Optional.empty());


            System.out.println("\t\t\t\t\t\t\t" + domain);


        } catch (IOException e) {
            System.out.println("IOException");
        } catch (URISyntaxException e) {
            System.out.println("URISyntaxException");
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException");
        }

//        cache.add(job.getURL());
//
//        Future<Map<String, Integer>> result = threadPool.submit(new WebScanTask(job.getURL(), job.getHopCount(), jobs));
//
//        String domain;
//        try {
//            domain = new URI(job.getURL()).getHost();
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//
//        results.put(job.getURL(), new WebResult(result, domain));
//        System.out.println("\t\t\t\t\t\t\t" + domain);

//        try {
//
//            Map<String, Integer> countedKeywords = result.get();
//            System.out.println(countedKeywords);
//
//            /* Two options: */
//            /* TODO: Send countedKeywords to Result retriever */
//            /* TODO: FileScanTask should send result to Result retriever */
//
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }

    }

}
