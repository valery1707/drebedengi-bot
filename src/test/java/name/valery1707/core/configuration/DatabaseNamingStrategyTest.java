package name.valery1707.core.configuration;

import org.hibernate.boot.model.naming.Identifier;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseNamingStrategyTest {

	@Test
	public void testCamelCaseToUnderscores() throws Exception {
		assertEquals("parentFuid", "parent_fuid", toUnderscores("parentFuid"));
		assertEquals("md5", "md5", toUnderscores("md5"));
	}

	private String toUnderscores(String text) {
		return DatabaseNamingStrategy.camelCaseToUnderscores(new Identifier(text, false)).getText();
	}
}