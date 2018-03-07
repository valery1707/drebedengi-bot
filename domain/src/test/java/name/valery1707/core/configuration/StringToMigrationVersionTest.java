package name.valery1707.core.configuration;

import org.flywaydb.core.api.MigrationVersion;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringToMigrationVersionTest {

	@Test
	public void testConvert() throws Exception {
		StringToMigrationVersion converter = new StringToMigrationVersion();
		assertThat(converter.convert("2016.08.17_11.00"))
				.isGreaterThan(MigrationVersion.fromVersion("2016.08.17_10.00"))
				.isLessThan(MigrationVersion.fromVersion("2016.08.17_12.00"));
	}
}