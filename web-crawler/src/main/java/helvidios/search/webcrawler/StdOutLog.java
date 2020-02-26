package helvidios.search.webcrawler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StdOutLog implements Log {

    private String timestamp(){
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

    @Override
    public void info(String msg) {
        System.out.println(String.format("[INFO]: %s %s", timestamp(), msg));

    }

    @Override
    public void err(String err) {
        System.out.println(String.format("[ERROR]: %s %s", timestamp(), err));
    }

}