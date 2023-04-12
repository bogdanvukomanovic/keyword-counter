package scanner;

import app.Configuration;
import result_retriever.ResultRetriever;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
public class WebScannerRefreshWorker implements Runnable {

    private volatile boolean working = true;

    private Set<String> cache;

    public WebScannerRefreshWorker(Set<String> cache) {
        this.cache = cache;
    }

    @Override
    public void run() {

        while (working) {

            try {
                Thread.sleep(Configuration.URL_REFRESH_TIME);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            cache.clear();
            ResultRetriever.webSummaryCache.set(null);
            ResultRetriever.webDomainCache = new ConcurrentHashMap<>();

        }

    }

    public void stop() {
        working = false;
    }

}
