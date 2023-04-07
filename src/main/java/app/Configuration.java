package app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    public static String KEYWORDS;
    public static String FILE_CORPUS_PREFIX;
    public static int DIR_CRAWLER_SLEEP_TIME;
    public static int FILE_SCANNING_SIZE_LIMIT;
    public static int HOP_COUNT;
    public static int URL_REFRESH_TIME;
    public static String DATA_ROOT;


    public static void load() {

        Properties properties = new Properties();

        try {

            FileInputStream in = new FileInputStream("./src/resources/application.properties");
            properties.load(in);

            KEYWORDS = properties.getProperty("KEYWORDS");
            FILE_CORPUS_PREFIX = properties.getProperty("FILE_CORPUS_PREFIX");
            DIR_CRAWLER_SLEEP_TIME = Integer.parseInt(properties.getProperty("DIR_CRAWLER_SLEEP_TIME"));
            FILE_SCANNING_SIZE_LIMIT = Integer.parseInt(properties.getProperty("FILE_SCANNING_SIZE_LIMIT"));
            HOP_COUNT = Integer.parseInt(properties.getProperty("HOP_COUNT"));
            URL_REFRESH_TIME = Integer.parseInt(properties.getProperty("URL_REFRESH_TIME"));
            DATA_ROOT = properties.getProperty("DATA_ROOT");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
