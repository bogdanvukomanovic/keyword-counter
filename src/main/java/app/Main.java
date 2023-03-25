package app;

import directory_crawler.DirectoryCrawlerWorker;

public class Main {

    public static void main(String[] args) {

        DirectoryCrawlerWorker DCWorker = new DirectoryCrawlerWorker();
        System.out.println(DCWorker.crawl());

    }

}
