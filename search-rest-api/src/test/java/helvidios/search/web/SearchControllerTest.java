package helvidios.search.web;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import helvidios.search.web.model.SearchHit;
import helvidios.search.web.model.TermPage;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SearchControllerTest {

    @LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
    
    @Test
    public void getVocabulary(){
        TermPage page = restTemplate.getForObject(
            String.format("http://localhost:%d/api/vocabulary?prefix=stop&limit=6&page=1", port), 
            TermPage.class);
        assertThat(page.getnPages()).isEqualTo(2);
        assertThat(page.getTerms()).isEqualTo(Arrays.asList(
            "stop",
            "stopanimationtimer",
            "stopautoscrolling",
            "stopcellediting",
            "stopclass",
            "stopediting"
        ));

        page = restTemplate.getForObject(
            String.format("http://localhost:%d/api/vocabulary?prefix=stop&limit=6&page=2", port), 
            TermPage.class);
        assertThat(page.getnPages()).isEqualTo(2);
        assertThat(page.getTerms()).isEqualTo(Arrays.asList(
            "stopeditingincompleteediting",
            "stopping",
            "stoppropagation",
            "stopreceivingtasks",
            "stoprecording",
            "stopthread"
        ));
    }

    @Test
    public void search(){
        SearchHit[] hits = restTemplate.getForObject(
            String.format("http://localhost:%d/api/search?query=map put&limit=3", port), 
            SearchHit[].class);
        assertThat(hits.length).isEqualTo(3);
        assertThat(hits[0]).isEqualTo(new SearchHit(-1965704338, "https://docs.oracle.com/javase/8/docs/api/java/util/Map.html", "Map (Java Platform SE 8 )", 7.432675089531022));
        assertThat(hits[1]).isEqualTo(new SearchHit(-30051156, "https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html", "TreeMap (Java Platform SE 8 )", 7.213925016886638));
        assertThat(hits[2]).isEqualTo(new SearchHit(460305784, "https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentSkipListMap.html", "ConcurrentSkipListMap (Java Platform SE 8 )", 7.0617820051664015));
    }
}