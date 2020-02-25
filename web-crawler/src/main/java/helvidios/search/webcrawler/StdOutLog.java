package helvidios.search.webcrawler;

public class StdOutLog implements Log{

    @Override
    public void info(String msg) {
        System.out.println(String.format("[INFO]: %s", msg));

    }

    @Override
    public void err(String err) {
        System.out.println(String.format("[ERROR]: %s", err));
    }

}