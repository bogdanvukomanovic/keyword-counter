package result_retriever.task;

import directory_crawler.Corpus;
import job_queue.job.FileScanningJob;
import job_queue.job.ScanningJob;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class FileSummaryTask implements Callable<Map<String, Map<String, Integer>>> {

    private Map<String, Future<Map<String, Integer>>> results;

    public FileSummaryTask(Map<String, Future<Map<String, Integer>>> results) {
        this.results = results;
    }

    @Override
    public Map<String, Map<String, Integer>> call() throws Exception {

//        results.entrySet().stream().filter(x -> );

        return null;
    }


    public static void main(String[] args) {

        Map<String, Integer> result_1 = new HashMap<>();
        Map<String, Integer> result_2 = new HashMap<>();

        result_1.put("A", 1);
        result_1.put("A", 2);
        result_2.put("B", 69);

        Map<String, Map<String, Integer>> results = new HashMap<>();

        results.put("AA", result_1);
        results.put("BB", result_2);


        results.entrySet().stream().filter(x -> x.getKey() == "AA").
                forEach(System.out::println);

    }


}




