package helvidios.search.web;

import java.io.IOException;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import helvidios.search.index.storage.IndexRepository;
import helvidios.search.index.storage.MongoDbIndexRepository;
import helvidios.search.linguistics.ApacheNlpLemmatizer;
import helvidios.search.linguistics.Lemmatizer;
import helvidios.search.searcher.*;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.MongoDbDocumentRepository;
import helvidios.search.tokenizer.HtmlTokenizer;
import helvidios.search.tokenizer.Tokenizer;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class SearchRestApiApplication {

	private static final Logger logger = LogManager.getLogger(SearchRestApiApplication.class);

	@Value("${db.name}")
	private String dbName;

	public static void main(String[] args) {
		SpringApplication.run(SearchRestApiApplication.class, args);
	}

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
			.apis(RequestHandlerSelectors.basePackage("helvidios.search.web")).build();
	}

	@Bean
	Searcher getSearcher() throws IOException {
		Tokenizer tokenizer = new HtmlTokenizer();
		Lemmatizer lemmatizer = new ApacheNlpLemmatizer();
		DocumentRepository docRepo = new MongoDbDocumentRepository.Builder().setDatabase(dbName).build();
		IndexRepository indexRepo = new MongoDbIndexRepository.Builder().setDatabase(dbName).build();
		return new IndexSearcher(indexRepo, docRepo, tokenizer, lemmatizer, logger);
	}

}
