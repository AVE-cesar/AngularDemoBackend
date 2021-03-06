$output.java($Root, "Application")##

$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##

$output.require("org.springframework.boot.SpringApplication")##
$output.require("org.springframework.boot.autoconfigure.SpringBootApplication")##
$output.require("org.springframework.data.jpa.repository.config.EnableJpaRepositories")##
$output.require("org.springframework.core.env.Environment")##
$output.require("org.springframework.transaction.annotation.EnableTransactionManagement")##

@SpringBootApplication
@EnableJpaRepositories("${configuration.rootPackage}.jpa.repository")
@EnableTransactionManagement
public class Application {
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		Environment env = app.run(args).getEnvironment();

		String host = env.getProperty("server.address");
		String contextPath = env.getProperty("server.contextPath");
		String serverPort = env.getProperty("server.port");

		log.info("\n\nAccess URLs:\n----------------------------------------\n\tLocal: \t\thttp://{}:{}{}\n\n", host, serverPort, contextPath);
	}
}
