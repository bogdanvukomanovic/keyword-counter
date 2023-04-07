package app;

import directory_crawler.DirectoryCrawlerWorker;


public class Main {

    public static void main(String[] args) {

        Configuration.load();

        DirectoryCrawlerWorker DCWorker = new DirectoryCrawlerWorker();
        Thread DCWorkerThread = new Thread(DCWorker);

        DCWorkerThread.start();

        try {
            DCWorkerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Main finished successfully.");

    }

}
