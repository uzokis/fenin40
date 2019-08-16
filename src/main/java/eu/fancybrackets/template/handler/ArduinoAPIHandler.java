package eu.fancybrackets.template.handler;

import static eu.fancybrackets.template.jooq.generated.tables.Measurement.MEASUREMENT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.json.JSONObject;

import eu.fancybrackets.template.jooq.generated.tables.pojos.Measurement;
import eu.fancybrackets.template.jooq.generated.tables.records.MeasurementRecord;
import eu.fancybrackets.template.verticle.ArduinoAPIVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;


public class ArduinoAPIHandler {
	private final static Logger LOG = Logger.getLogger(ArduinoAPIHandler.class.getName());

	@Inject
	private Vertx vertx;

	@Inject
	private DataSource ds;
	
	
	private DataProcessor tank1 = new DataProcessor(1);
	private DataProcessor tank2 = new DataProcessor(2);
	private DataProcessor tank3 = new DataProcessor(3);


	
	/*
	private static int entryCount;
		
	public static void calculateEntryCount() {
		vertx.executeBlocking(future -> {
			try (Connection conn = ds.getConnection()) {
				int ec = DSL.using(conn, SQLDialect.POSTGRES).fetchCount(MEASUREMENT);
				entryCount = ec;
				future.complete();
			} catch (SQLException e) {
				future.fail(e);
			}
		}, future -> {
			if (future.succeeded()) {
				System.out.println("entry count calculated successfully");
			} else {
				LOG.log(Level.SEVERE, "Unexpected exception", future.cause());
				System.out.println("failed calculating entry count");
			}
		});
	}
	*/
	public void handleStartStopTank1(RoutingContext context) {
		vertx.executeBlocking(future -> {
			try (Connection conn = ds.getConnection()) {
				String response;
				if (tank1.intIsRunning() == 1) {
					response = "STAReeen";
				} else {
					response = "STOPeeen";
				}
				
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
	
	public void handleStartStopTank2(RoutingContext context) {
		vertx.executeBlocking(future -> {
			try (Connection conn = ds.getConnection()) {
				String response;
				if (tank2.intIsRunning() == 1) {
					response = "STARtwee";
				} else {
					response = "STOPtwee";
				}
				
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
	
	int i = 0;
	
	public void handleStartStopTank3(RoutingContext context) {
		vertx.executeBlocking(future -> {
			try (Connection conn = ds.getConnection()) {
				i++;
				String response;
				if (tank3.intIsRunning() == 1) {
					response = "STARdrie" + i;
				} else {
					response = "STOPdrie" + i;
				}
				
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

	public void handleHTMLFile(RoutingContext context) {
		vertx.executeBlocking(future -> {
				HttpServerResponse response = context.response();
				response
	            .putHeader("content-type", "text/html")
	            .sendFile("/src/main/resources/Distance.html");
				
				future.complete();
		
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
	
	/**
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
	
	
	/**
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
	
	
	/**
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
														.put("bak3", m3.getValue())
														.put("running1", tank1.intIsRunning())
														.put("running2", tank2.intIsRunning())
														.put("running3", tank3.intIsRunning());

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
	
	
	/**
	 * Handle POST request from the HTML file.
	 * Starts tank 1 by answering the next GET request from the Arduino with
	 * "STAReeen".
	 */
	public void handlePostStart1(RoutingContext context) {
		System.out.println("start 1");
		startTank(1, context);
	}
	
	/**
	 * 
	 * @param context
	 */
	public void handlePostStop1(RoutingContext context) {
		System.out.println("stop 1");
		stopTank(1, context);
	}
	
	/**
	 * 
	 * @param context
	 */
	public void handlePostStart2(RoutingContext context) {
		System.out.println("start 2");
		startTank(2, context);
	}
	
	/**
	 * 
	 * @param context
	 */
	public void handlePostStop2(RoutingContext context) {
		System.out.println("stop 2");
		stopTank(2, context);
	}
	
	/**
	 * 
	 * @param context
	 */
	public void handlePostStart3(RoutingContext context) {
		System.out.println("start 3");
		startTank(3, context);
	}
	
	/**
	 * 
	 * @param context
	 */
	public void handlePostStop3(RoutingContext context) {
		System.out.println("stop 3");
		stopTank(3, context);
	}
	
	/**
	 * Handle POST request from the Arduino, containing the water levels of the 3 tanks.
	 * Creates a new instance of the class Measurements for each tank that was given with the
	 * POST request.
	 */
	public void handletWaterLevels(RoutingContext context) {
		System.out.println("hij doet toch nog iets");
		
		vertx.executeBlocking(future -> { System.out.println("lkjsf");
			try (Connection conn = ds.getConnection()) {
				System.out.println("waypoint 1");
				DSL.using(conn).transaction(configuration -> {
					System.out.println("komt in functie");
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
					
					//delete the 2000 oldest entries if the entry count is more than 7000 entries.
					int entryCount = DSL.using(conn).fetchCount(MEASUREMENT);
					System.out.println("entry count: " + entryCount);
					if (entryCount > 7000) {
						int id = DSL.using(conn, SQLDialect.POSTGRES).selectFrom(MEASUREMENT).orderBy(MEASUREMENT.MEASURE_TIME.desc()).fetchAny().get(MEASUREMENT.ID);
						String condition = "MEASUREMENT.ID < " + Integer.toString(id) + " - 5000";
						DSL.using(conn, SQLDialect.POSTGRES).deleteFrom(MEASUREMENT).where(condition).execute();
					}
					
					
					//iterate over all the given keys ( options: tank 1, tank 2 , tank 3)
					// 	create instance of the class Measurement for each given tank
					// 	add container id, value and time stamp to each instance
					for (String i : keys) {
						
						String containerId = Character.toString(i.charAt(3));
						DataProcessor tank = selectTank(containerId);
						String key = "bak" + containerId;
						String value = context.getBodyAsJson().getString(key);
						float smoothedValue = tank.SmoothenData(Float.parseFloat(value));
						
						if (smoothedValue == -1) {
							tank.setIdle();
						} else {
							Measurement m = new Measurement();
							m.setContainerId(containerId);
							m.setValue(Float.toString(smoothedValue));
							m.setMeasureTime(time);
							MeasurementRecord mr = DSL.using(configuration).newRecord(MEASUREMENT, m);
							int result = mr.store();
						}
						//System.out.println("stored: " + result);
						System.out.println("tank" + containerId + ": " + smoothedValue); 
						
					}
					
					future.complete();

				});
			} catch (Exception e) {
				System.out.println("sqlException: " + e);
				future.fail(e);
			}
		}, future -> {
			if (future.succeeded()) {
				System.out.println("succeeded");
				context.response().setStatusCode(201).end();
			} else {
				System.out.println("failed: " + future.cause());
				context.response().setStatusCode(500).end();
			}
		});
		System.out.println("komt op einde functie");
	}
	
	/**
	 * request to make 
	 */
	private HttpRequest<Buffer> request;
	
	/**
	 * Stop specified tank by sending a POST request to the Arduino.
	 * 
	 * @argument tankNb : integer specifying which tank to stop (tank 1, 2 or 3).
	 * 
	 * Post request contains a Json object of which the key is always "STOP" and the 
	 * value is "eeen" if tankNb equals 1, "twee" if tankNb equals 2, and "drie" if 
	 * the tankNb equals 3.
	 */
	private void stopTank(int tankNb, RoutingContext context) {
		DataProcessor tankToStop = selectTank(Integer.toString(tankNb));
		tankToStop.setIdle();
	}
	
	/**
	 * Start specified tank by sending a POST request to the Arduino.
	 * 
	 * @argument tankNb : integer specifying which tank to start (tank 1, 2 or 3).
	 * 
	 * Post request contains a Json object of which the key is always "STAR" and the 
	 * value is "eeen" if tankNb equals 1, "twee" if tankNb equals 2, and "drie" if 
	 * the tankNb equals 3.
	 */
	private void startTank(int tankNb, RoutingContext context) {
		DataProcessor tankToStart = selectTank(Integer.toString(tankNb));
		tankToStart.setRunning();
	}
	
	
	private DataProcessor selectTank(String tankNb) {
		if (tankNb.equalsIgnoreCase("1")) {
			return tank1;
		}
		else if (tankNb.equalsIgnoreCase("2")) {
			return tank2;
		}
		else {
			return tank3;
		}
	}
	
}
