package helvidios.search.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import helvidios.search.web.controller.SearchController;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AppStartingTest {
    
    @Autowired
    SearchController searchController;

    @Test
    public void contextLoads(){
        assertThat(searchController).isNotNull();
    }

}