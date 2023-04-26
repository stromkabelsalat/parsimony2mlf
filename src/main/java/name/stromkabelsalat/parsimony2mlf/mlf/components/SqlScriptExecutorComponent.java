package name.stromkabelsalat.parsimony2mlf.mlf.components;

import java.nio.charset.StandardCharsets;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

@Component
public class SqlScriptExecutorComponent {
	private final ApplicationContext applicationContext;
	private final DataSource dataSource;

	public SqlScriptExecutorComponent(final ApplicationContext applicationContext, final JdbcTemplate jdbcTemplate) {
		this.applicationContext = applicationContext;
		this.dataSource = jdbcTemplate.getDataSource();
	}

	public void executeSqlScript(final String location) throws DataAccessException {
		final Resource resource = this.applicationContext.getResource(location);
		final ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false,
				StandardCharsets.UTF_8.toString(), resource);

		if (this.dataSource != null) {
			resourceDatabasePopulator.execute(this.dataSource);
		}
	}
}
