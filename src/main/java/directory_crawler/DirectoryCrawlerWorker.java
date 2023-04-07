package directory_crawler;

import app.Configuration;
import job_queue.FileScanningJob;
import job_queue.ScanningJob;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

public class DirectoryCrawlerWorker implements Runnable {

    private List<String> directories;
    private BlockingQueue<ScanningJob> jobs;
    private HashMap<Path, Corpus> corpora = new HashMap<Path, Corpus>();


    public DirectoryCrawlerWorker(List<String> directories, BlockingQueue<ScanningJob> jobs) {
        this.directories = directories;
        this.jobs = jobs;
        this.directories.add("data_0"); this.directories.add("data_1"); this.directories.add("data_2"); /* TODO: TBC */
    }

    private List<Corpus> findCorpora() {

        List<Corpus> corpora = new ArrayList<>();

        for (String directory : directories) {

            try (Stream<Path> stream = Files.walk(Paths.get(Configuration.DATA_ROOT + directory))) {

                corpora.addAll(stream.filter(x -> Files.isDirectory(x) && Paths.get(x.toString()).getFileName().toString().startsWith(Configuration.FILE_CORPUS_PREFIX))
                                     .map(x -> new Corpus(x.getFileName().toString(), x, extractAllTexts(x)))
                                     .toList());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return corpora;
    }

    public List<Text> extractAllTexts(Path path) {

        List<Text> texts = new ArrayList<>();

        for (File file : Objects.requireNonNull(new File(path.toString()).listFiles())) {
            if (!file.isDirectory()) {
                texts.add(new Text(file.getName()));
            }
        }

        return texts;
    }

    private boolean areTextsModified(Corpus corpus) {

        boolean x = false;

        for (Text text : corpus.getTexts()) {

            File file = new File(corpus.getPath() + "\\" + text.getName());

            /* New file added or any of the recorded files modified */
            if (Objects.isNull(text.getLastModified()) || file.lastModified() != text.getLastModified()) {
                x = true;
                text.setLastModified(file.lastModified());
            }

        }

        return x;
    }

    private void updateTextsModifiedValue(Corpus corpus) {

        for (Text text : corpus.getTexts()) {

            File file = new File(corpus.getPath() + "\\" + text.getName());
            text.setLastModified(file.lastModified());
        }

    }

    /* TODO: Maybe add level of abstraction for Job queue? */
    private void enqueue(FileScanningJob job) {

        try {
            jobs.put(job);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {

        List<Corpus> candidates;

        /* TODO: Add cancellation and sleep after each iteration */
        while (true) {

            candidates = findCorpora();

            for (Corpus corpus : candidates) {

                /* Brand-new corpus */
                if (!corpora.containsKey(corpus.getPath())) {

                    System.out.println("Created job for corpus: " + corpus.getName());
                    enqueue(new FileScanningJob(corpus));

                    updateTextsModifiedValue(corpus);

                    corpora.put(corpus.getPath(), corpus);

                    continue;
                }

                /* Old corpus - check if any texts are modified or added in the meantime */
                if (areTextsModified(corpora.get(corpus.getPath()))) {

                    System.out.println("Created job for corpus: " + corpus.getName());
                    enqueue(new FileScanningJob(corpus));

                }

            }

        }

    }

}