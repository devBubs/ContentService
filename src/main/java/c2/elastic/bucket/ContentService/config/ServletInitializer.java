package c2.elastic.bucket.ContentService.config;

import c2.elastic.bucket.ContentService.ContentServiceApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ContentServiceApplication.class);
	}

}
