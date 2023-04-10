package scanner.task;

import app.Configuration;
import directory_crawler.Text;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileScanTask extends RecursiveTask<Map<String, Integer>> {

    List<Text> texts;

    public FileScanTask(List<Text> texts) {
        this.texts = texts;
    }

//    private List<Text> getBatch() {
//
//        List<Text> batch = new ArrayList<>();
//
//        int accumulator = 0;
//
//        for (Text text : texts) {
//
//            File file = new File(text.getPath());
//            accumulator += file.length();
//
//            batch.add(text);
//
//            if (accumulator >= Configuration.FILE_SCANNING_SIZE_LIMIT) {
//                break;
//            }
//
//        }
//
//        texts = texts.subList(batch.size(), texts.size());
//
//        return batch;
//    }

    private int totalTextSize() {

        int sum = 0;

        for (Text text : texts) {
            File file = new File(text.getPath());
            sum += file.length();
        }

        return sum;
    }

    private Map<String, Integer> countWords(Map<String, Integer> map) {

        Map<String, Integer> result = new HashMap<>();

        for (Text text : texts) {

            try {

                List<String> words = new ArrayList<>();
                Scanner input = new Scanner(new File(text.getPath()));

                while (input.hasNext()) {
                    words.add(input.next());
                }

                /* TODO: Make Set of Keywords */
                // Map<String, Integer> count = (Map<String, Integer>) stream.filter(x -> Configuration.KEYWORDS.contain(x)).collect(Collectors.toMap(w -> w, w -> 1, Integer::sum));
                Map<String, Integer> count = words.stream().collect(Collectors.toMap(w -> w, w -> 1, Integer::sum));

                result = Stream.concat(result.entrySet().stream(), count.entrySet().stream())
                               .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return result;
    }

    @Override
    protected Map<String, Integer> compute() {

        Map<String, Integer> result = new HashMap<String, Integer>(); /* TODO: ConcurrentHashMap<>? */

        if (totalTextSize() < Configuration.FILE_SCANNING_SIZE_LIMIT || texts.size() == 1) {
            result = countWords(result);
            System.out.println(result);

        } else {

            int mid = texts.size() / 2;

            FileScanTask left = new FileScanTask(texts.subList(0, mid));
            FileScanTask right = new FileScanTask(texts.subList(mid, texts.size()));

            left.fork();

            Map<String, Integer> rightResult = right.compute();
            Map<String, Integer> leftResult = left.join();

            result = Stream.concat(result.entrySet().stream(), rightResult.entrySet().stream())
                           .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));

            result = Stream.concat(result.entrySet().stream(), leftResult.entrySet().stream())
                           .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));

        }

        return result;
    }

}
