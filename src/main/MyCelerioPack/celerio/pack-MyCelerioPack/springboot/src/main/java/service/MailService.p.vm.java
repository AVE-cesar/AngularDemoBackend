$output.java("${configuration.rootPackage}.service","MailService")##

$output.require("org.apache.commons.lang.CharEncoding")##
$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.springframework.context.MessageSource")##
$output.require("org.springframework.mail.javamail.JavaMailSenderImpl")##
$output.require("org.springframework.mail.javamail.MimeMessageHelper")##
$output.require("org.springframework.scheduling.annotation.Async")##
$output.require("org.springframework.stereotype.Service")##
$output.require("org.thymeleaf.context.Context")##
$output.require("org.thymeleaf.spring4.SpringTemplateEngine")##

$output.require("org.springframework.beans.factory.annotation.Autowired")##

$output.require("com.jaxio.demo.config.ApplicationProperties")##

$output.require("javax.mail.internet.MimeMessage")##
$output.require("java.util.Locale")##

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(applicationProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }
    
    @Async
    public void sendWelcomeEmail(String to, Locale locale, Integer id) {
        log.debug("Sending activation e-mail to '{}'", to);

        String content = generateEmailContent(locale, id);
        String subject = messageSource.getMessage("email.welcome.title", null, locale);
        
        sendEmail(to, subject, content, false, true);
    }
    
    protected String generateEmailContent(Locale locale, Integer id) {
    		// add variables to template
        Context context = new Context(locale);
        context.setVariable("url", "http://localhost:8080/#!/confirmRegistration/"+id);
        
    		String content = templateEngine.process("welcomeEmail", context);
    	
    		return content;
    }
}
