package eu.fancybrackets.template.verticle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import eu.fancybrackets.template.guice.ConfigModule;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.ResponseTimeHandler;

public class MainVerticle extends AbstractVerticle {
	Injector injector = null;
	private final static Logger LOG = Logger.getLogger(MainVerticle.class.getName());
	
	public MainVerticle() {
		super();
	}

	@Override
	public void start() throws Exception {
		super.start();
		
		ConfigStoreOptions file = new ConfigStoreOptions().setType("file").setFormat("properties")
				.setConfig(new JsonObject().put("path", ".env"));
		ConfigRetrieverOptions options = new ConfigRetrieverOptions().setIncludeDefaultStores(true).addStore(file);
		ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

		vertx.executeBlocking(future-> {
			retriever.getConfig(ar -> {
				if (ar.failed()) {
					future.fail("Could not retrieve configuration!");
				} else {
					future.complete(ar.result());
				}
			});
		}, res -> {
			injector = Guice.createInjector(new ConfigModule(vertx, (JsonObject)res.result()));

			Router router = injector.getInstance(Router.class);
			router.route().handler(ResponseTimeHandler.create());

			vertx.deployVerticle(injector.getInstance(ArduinoAPIVerticle.class));
			
			Integer localPort = injector.getInstance(Key.get(Integer.class, Names.named(ConfigModule.LOCAL_PORT)));

			//TODO should pbly go via config retriever
			vertx.createHttpServer().requestHandler(router).listen(localPort);

			//disabled until db is fixed
			printDBVersion(injector.getInstance(DataSource.class));
		});
	}

	protected void printDBVersion(DataSource ds) {
		vertx.executeBlocking(future -> {
			try (Connection conn = ds.getConnection()) {
				Record record = DSL.using(conn, SQLDialect.POSTGRES).fetchOne("select version();");
				future.complete(record.get(0));
			} catch (SQLException e) {
				future.fail(e);
				LOG.log(Level.SEVERE, "Unexpected exception", e);
				e.printStackTrace();
			}
		}, res -> {
			LOG.info(String.format("Connected to %s", res.result()));
		});
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		injector.getInstance(DSLContext.class).close();
	}

}
