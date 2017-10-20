$output.javaTest("${configuration.rootPackage}.service", "MailServiceTest")##

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.containsString;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

	@Autowired
    private MailService mailService;
	
	@Test
	public void test() {
		assertNotNull(mailService);
		
		int i = 123456789;
		
		// Hamcrest assertion
		assertThat(mailService.generateEmailContent(Locale.FRENCH, i), containsString(""+i));
	}
}
