$output.java($Root, "Application")##

$output.require("org.springframework.boot.SpringApplication")##
$output.require("org.springframework.boot.autoconfigure.SpringBootApplication")##
$output.require("org.springframework.data.jpa.repository.config.EnableJpaRepositories")##

@SpringBootApplication
@EnableJpaRepositories("${configuration.rootPackage}.jpa.repository")
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
