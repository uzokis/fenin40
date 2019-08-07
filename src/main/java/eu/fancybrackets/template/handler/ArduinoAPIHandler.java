package eu.fancybrackets.template.handler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.postgresql.jdbc.TimestampUtils;

import eu.fancybrackets.template.jooq.generated.tables.pojos.Measurement;
import eu.fancybrackets.template.jooq.generated.tables.records.MeasurementRecord;
import eu.fancybrackets.template.verticle.ArduinoAPIVerticle;
import eu.fancybrackets.template.verticle.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import static eu.fancybrackets.template.jooq.generated.tables.Measurement.*;

public class ArduinoAPIHandler {
	private final static Logger LOG = Logger.getLogger(ArduinoAPIHandler.class.getName());

	@Inject
	private Vertx vertx;

	@Inject
	private DataSource ds;

	public void handleGet(RoutingContext context) {
		vertx.executeBlocking(future -> {
			try (Connection conn = ds.getConnection()) {
				Measurement m = DSL.using(conn, SQLDialect.POSTGRES).selectFrom(MEASUREMENT)
						.where(MEASUREMENT.CONTAINER_ID.eq(context.pathParam(ArduinoAPIVerticle.API_PARAM_CONTAINER_ID)))
						.orderBy(MEASUREMENT.MEASURE_TIME.desc()).fetchAny().into(Measurement.class);

				JsonObject measurement = new JsonObject().put("value", m.getValue()).put("timestamp", DateFormat.getDateTimeInstance().format(m.getMeasureTime()));
				JsonObject response = new JsonObject().put("measurement", measurement);

				future.complete(response);
			} catch (SQLException e) {
				future.fail(e);
			}
		}, future -> {
			if (future.succeeded()) {
				context.response().setChunked(true);
				context.response().write(future.result().toString()).end();
			} else {
				LOG.log(Level.SEVERE, "Unexpected exception", future.cause());
				context.response().setStatusCode(500).end();
			}
		});
//		
//		
//		
//		try {
//			String containerid = context.pathParam(ArduinoAPIVerticle.API_PARAM_CONTAINER_ID);
//			LOG.fine(String.format("Handling request for %s", containerid));
//			JsonObject measurement = new JsonObject().put("value", "20").put("timestamp", new Date().toString());
//			JsonObject response = new JsonObject().put("measurement", measurement);
//			context.response().setChunked(true);
//			context.response().write(response.toString()).end();
//		} catch (Exception e) {
//			LOG.log(Level.SEVERE, "Unexpected exception", e);
//		}
	}

	public void handlePost(RoutingContext context) {
		vertx.executeBlocking(future -> {
			try (Connection conn = ds.getConnection()) {
				DSL.using(conn).transaction(configuration -> {
					LOG.info(String.format("**** Got %s", context.getBodyAsJson()));

					Measurement m = new Measurement();
					m.setContainerId(context.pathParam(ArduinoAPIVerticle.API_PARAM_CONTAINER_ID));
					m.setValue(context.getBodyAsJson().getString("value"));
					m.setMeasureTime(new Timestamp(new Date().getTime()));

					MeasurementRecord mr = DSL.using(configuration).newRecord(MEASUREMENT, m);
					mr.store();
					future.complete();
				});
			} catch (SQLException e) {
				future.fail(e);
			}
		}, future -> {
			if (future.succeeded()) {
				context.response().setStatusCode(201).end();
			} else {
				context.response().setStatusCode(500).end();
			}
		});
	}
}
