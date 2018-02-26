package kim.eren.infonalquest;

import kim.eren.infonalquest.controller.IndexController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class InfonalQuestApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(InfonalQuestApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(IndexController.class);
	}
}
