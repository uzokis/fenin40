package eu.fancybrackets.template.handler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.json.JSONObject;
import eu.fancybrackets.template.jooq.generated.tables.pojos.Measurement;
import eu.fancybrackets.template.jooq.generated.tables.records.MeasurementRecord;
import eu.fancybrackets.template.verticle.ArduinoAPIVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import static eu.fancybrackets.template.jooq.generated.tables.Measurement.*;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;


public class ArduinoAPIHandler {
	private final static Logger LOG = Logger.getLogger(ArduinoAPIHandler.class.getName());

	@Inject
	private Vertx vertx;

	@Inject
	private DataSource ds;

	/* 
	 *  example handler by Eli
	 */
	public void handleGet(RoutingContext context) {
		try {
			String containerid = context.pathParam(ArduinoAPIVerticle.API_PARAM_CONTAINER_ID);
			LOG.fine(String.format("Handling request for %s", containerid));
			JsonObject measurement = new JsonObject().put("value", "20").put("timestamp", new Date().toString());
			JsonObject response = new JsonObject().put("measurement", measurement);
			context.response().setChunked(true);
			context.response().write(response.toString()).end();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Unexpected exception", e);
		}
		vertx.executeBlocking(future -> {
			try (Connection conn = ds.getConnection()) {
				Measurement m = DSL.using(conn, SQLDialect.POSTGRES).selectFrom(MEASUREMENT)
						.where(MEASUREMENT.CONTAINER_ID.eq(context.pathParam( ArduinoAPIVerticle.API_PARAM_CONTAINER_ID)))
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

	}
	
	
	/*
	 * Example handler by Eli
	 */
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
	
	
	/*
	 * Handle the GET request from the HTML file. 
	 * Replies with the last known data of the water levels of the 3 tanks.
	 */
	public void handleGetDataHTML(RoutingContext context) {
		
		vertx.executeBlocking(future -> {
			try (Connection conn = ds.getConnection()) {
				
				Measurement m1 = DSL.using(conn, SQLDialect.POSTGRES).selectFrom(MEASUREMENT)
						.where(MEASUREMENT.CONTAINER_ID.eq("1"))
						.orderBy(MEASUREMENT.MEASURE_TIME.desc()).fetchAny().into(Measurement.class);
				Measurement m2 = DSL.using(conn, SQLDialect.POSTGRES).selectFrom(MEASUREMENT)
						.where(MEASUREMENT.CONTAINER_ID.eq("2"))
						.orderBy(MEASUREMENT.MEASURE_TIME.desc()).fetchAny().into(Measurement.class);
				Measurement m3 = DSL.using(conn, SQLDialect.POSTGRES).selectFrom(MEASUREMENT)
						.where(MEASUREMENT.CONTAINER_ID.eq("3"))
						.orderBy(MEASUREMENT.MEASURE_TIME.desc()).fetchAny().into(Measurement.class);

				JsonObject response = new JsonObject().put("bak1", m1.getValue())
														.put("bak2" , m2.getValue())
														.put("bak3", m3.getValue());

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
	}
	
	
	/*
	 * Handle POST request from the HTML file.
	 * Starts tank 1 by sending a POST request to the Arduino.
	 */
	public void handlePostStart1(RoutingContext context) {
		System.out.println("start 1");
		startTank(1);
	}
	
	
	/*
	 * Handle POST request from te HTML file.
	 * Stops tank 1 by sending a POST request to the Arduino.
	 */
	public void handlePostStop1(RoutingContext context) {
		System.out.println("stop 1");
		stopTank(1);
	}	
	
	
	/*
	 * Handle POST request from the HTML file.
	 * Stops tank 2 by sending a POST request to the Arduino.
	 */
	public void handlePostStart2(RoutingContext context) {
		System.out.println("start 2");
		startTank(2);
	}
	
	
	/*
	 * Handle POST request from the HTML file.
	 * Stops tank 2 by sending a POST request to the Arduino.
	 */
	public void handlePostStop2(RoutingContext context) {
		System.out.println("stop 2");
		stopTank(2);
	}	
	
	
	/*
	 * Handle POST request from the HTML file.
	 * Starts tank 3 by sending a POST request to the Arduino.
	 */
	public void handlePostStart3(RoutingContext context) {
		System.out.println("start 3");
		startTank(3);
	}
	
	/*
	 * Handle POST request from the HTML file.
	 * Stops tank 3 by sending a POST request to the Arduino.
	 */
	public void handlePostStop3(RoutingContext context) {
		System.out.println("stop 3");
		stopTank(3);
	}	
	
	/*
	 * Handle POST request from the Arduino, containing the water levels of the 3 tanks.
	 * Creates a new instance of the class Measurements for each tank that was given with the
	 * POST request.
	 */
	public void handletWaterLevels(RoutingContext context) {
		
		vertx.executeBlocking(future -> {
			try (Connection conn = ds.getConnection()) {
				DSL.using(conn).transaction(configuration -> {
					LOG.info(String.format("**** Got %s", context.getBodyAsJson()));
					
					//get number of tanks that was given with the POST request
					JsonObject jsonBody = context.getBodyAsJson();
					Map jsonMap = jsonBody.mapTo(Map.class);
					JSONObject JSONBody = new JSONObject(jsonMap);
					Set<String> keys = JSONBody.keySet();
					
					System.out.println("data: " + jsonBody);
					
					//get time. Do this before the for-loop so we can be sure that
					//measurements from the same POST request have the exact same
					//time stamp - attribute
					Timestamp time = new Timestamp(new Date().getTime());
					
					//iterate over all the given keys ( options: tank 1, tank 2 , tank 3)
					// 	create instance of the class Measurement for each given tank
					// 	add container id, value and time stamp to each instance
					for (String i : keys) {
						System.out.println("komt in forloop");
						
						Measurement m = new Measurement();
						String containerId = Character.toString(i.charAt(3));
						m.setContainerId(containerId);
						String key = "bak" + containerId;
						m.setValue(context.getBodyAsJson().getString(key));
						m.setMeasureTime(time);
						MeasurementRecord mr = DSL.using(configuration).newRecord(MEASUREMENT, m);
						mr.store();
						future.complete();
					}

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
	
	/*
	 * request to make 
	 */
	private HttpRequest<Buffer> request;
	
	/*
	 * Stop specified tank by sending a POST request to the Arduino.
	 * 
	 * @argument tankNb : integer specifying which tank to stop (tank 1, 2 or 3).
	 * 
	 * Post request contains a Json object of which the key is always "STOP" and the 
	 * value is "eeen" if tankNb equals 1, "twee" if tankNb equals 2, and "drie" if 
	 * the tankNb equals 3.
	 */
	private void stopTank(int tankNb) {
		request = WebClient.create(vertx)
				.post(48964, "192.168.43.4", "/onoff");
		
		String tank;
		if (tankNb == 1) {
			tank = "eeen";
		} else if (tankNb == 2) {
			tank = "twee";
		} else if (tankNb ==  3) {
			tank = "drie";
		} else {
			tank = "invalid number";
		}
		
		JsonObject resp = new JsonObject().put("STOP", tank);

		request.sendJson(resp, ar -> {
			if (ar.succeeded()) {
			    AsyncResult<HttpResponse<Buffer>> response = ar;
			    System.out.println(response);
			} else {
				System.out.println("mislukt: " + ar.cause().getMessage());
			}
			});
	}
	
	/*
	 * Start specified tank by sending a POST request to the Arduino.
	 * 
	 * @argument tankNb : integer specifying which tank to start (tank 1, 2 or 3).
	 * 
	 * Post request contains a Json object of which the key is always "STAR" and the 
	 * value is "eeen" if tankNb equals 1, "twee" if tankNb equals 2, and "drie" if 
	 * the tankNb equals 3.
	 */
	private void startTank(int tankNb) {
		request = WebClient.create(vertx)
				.post(48964, "192.168.43.4", "/onoff");
		String tank;
		if (tankNb == 1) {
			tank = "eeen";
		} else if (tankNb == 2) {
			tank = "twee";
		} else if (tankNb ==  3) {
			tank = "drie";
		} else {
			tank = "invalid number";
		}
		
		JsonObject resp = new JsonObject().put("STAR", tank);
		System.out.println(resp);
		
		request.sendJson(resp, ar -> {
			if (ar.succeeded()) {
			    // Ok
			}});
	}
}
