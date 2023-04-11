package app;

public class Main {

    public static void main(String[] args) {

        Configuration.load();
        Controller.initialize();
        CLI.loop();

        System.out.println("Main finished successfully.");

    }

}
