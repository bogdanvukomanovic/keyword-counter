package scanner.task;

import app.Configuration;
import job_queue.JobQueue;
import job_queue.job.WebJob;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class WebScanTask implements Callable<Map<String, Integer>> {

    private String URL;
    private int hopCount;
    private JobQueue jobs;

    public WebScanTask(String URL, int hopCount, JobQueue jobs) {
        this.URL = URL;
        this.hopCount = hopCount;
        this.jobs = jobs;
    }

    private Map<String, Integer> countWords(List<String> words) {
        return words.stream().filter(x -> Configuration.KEYWORDS.contains(x)).collect(Collectors.toMap(w -> w, w -> 1, Integer::sum));
    }

    @Override
    public Map<String, Integer> call() {

        Map<String, Integer> result;

        try {

            Document document = Jsoup.connect(URL).get();
            List<String> words = Arrays.stream(document.body().text().split("\\s")).toList().stream().map(x -> x.replaceAll("[^a-zA-Z]", "").toLowerCase()).toList();

            result = countWords(words);

            if (hopCount > 1) {

                Elements links = document.select("a[href]");

                for (Element link : links) {
                    jobs.enqueue(new WebJob(link.attr("abs:href"), hopCount - 1));
                }

            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return result;
    }

}
