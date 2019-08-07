package eu.fancybrackets.template.guice;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class ConfigModule extends AbstractModule {
	public static final String LOCAL_PORT = "LOCAL_PORT";
	private final Vertx vertx;
	private final Context context;
	private final JsonObject config;

	public ConfigModule(Vertx vertx, JsonObject config) {
		this.vertx = vertx;
		this.config = config;
		this.context = vertx.getOrCreateContext();
	}

	@Override
	protected void configure() {
		bind(Vertx.class).toInstance(vertx);
		// TODO your configuration
	}

	@Provides
	@Singleton
	Router provideRouter() {
		Router router = Router.router(vertx);
		router.route("/static/*").handler(StaticHandler.create());
		return router;
	}

	@Provides
	@Singleton
	HttpClient getHttpClient() {
		return vertx.createHttpClient();
	}
	
	@Provides
	@Singleton
	@Named(LOCAL_PORT)
	public Integer getLocalPort() {
		return config.getInteger("PORT");
	}

	@Provides
	@Singleton
	public DataSource provideDataSource() throws URISyntaxException {
		URI dbUri = new URI(config.getString("DATABASE_URL"));

		HikariConfig config = new HikariConfig();
		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = String.format("jdbc:postgresql://%s:%s%s", dbUri.getHost(), dbUri.getPort(), dbUri.getPath());
		config.setJdbcUrl(dbUrl);
		config.addDataSourceProperty("sslmode", "require"); // required for heroku
		config.setUsername(username);
		config.setPassword(password);

		HikariDataSource ds = new HikariDataSource(config);
		return ds;
	}

	@Provides
	@Inject
	public DSLContext provideDSLContext(DataSource ds) {
		return DSL.using(ds, SQLDialect.POSTGRES);
	}
}
