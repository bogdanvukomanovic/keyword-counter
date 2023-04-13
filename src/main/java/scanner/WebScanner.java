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
    private WebScannerRefreshWorker webScannerRefreshWorker;


    public WebScanner(JobQueue jobs, Map<String, Result> results) {
        this.jobs = jobs;
        this.results = results;
        this.cache = new HashSet<String>();

        this.webScannerRefreshWorker = new WebScannerRefreshWorker(this.cache);

        Thread webScanRefresherThread = new Thread(webScannerRefreshWorker);
        webScanRefresherThread.start();
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

            System.out.println(">> Starting file scan for: web|" + job.getURL() + " (Domain: " + domain + ")");
            Future<Map<String, Integer>> result = threadPool.submit(new WebScanTask(job.getURL(), job.getHopCount(), jobs));

            results.put(job.getURL(), new WebResult(result, domain));

            /* TODO: Fields webSummaryCache and webDomainCache should not be static, maybe pass the ResultRetriever reference to WebScanner? */
            /* Each new web page, makes webSummaryCache dirty */
            ResultRetriever.webSummaryCache.set(null);
            /* Also, the corresponding domain is no longer up to date */
            ResultRetriever.webDomainCache.put(domain, Optional.empty());


        } catch (Exception e) {
            System.out.println(">> Error: Did not create new job because of non-supported/invalid/unreachable URL. (" + job.getURL() + ")");
        }

    }

    public void stop() {
        threadPool.shutdown();
        System.out.println(">> FINISHED: Web Scanner");
        webScannerRefreshWorker.stop();
    }

}
