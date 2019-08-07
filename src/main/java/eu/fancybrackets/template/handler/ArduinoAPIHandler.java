package eu.fancybrackets.template.handler;

import java.util.Date;
import java.util.logging.Logger;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;





public class ArduinoAPIHandler {
	private final static Logger LOG = Logger.getLogger(ArduinoAPIHandler.class.getName());

//	@Inject
//	private DSLContext jooq;


	public void handleGet(RoutingContext context) {
		try {
			String containerid = context.pathParam("containerid");
			LOG.fine(String.format("Handling request for %s", containerid));
			JsonObject measurement = new JsonObject().put("value", "20").put("timestamp", new Date().toString());
			JsonObject response = new JsonObject().put("measurement", measurement);
			context.response().setChunked(true);
			context.response().write(response.toString()).end();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.throwing("ArduinoAPIHandler", "handleGet", e);
		}
	}

	public void handleGetDataHTML(RoutingContext context) {
		try {
			LOG.fine(String.format("Handling request for water levels"));
			JsonObject waterLevel = new JsonObject().put("bak1", 10).put("bak2", 20).put("bak3", 30);
			context.response().setChunked(true);
			context.response().write(waterLevel.toString()).end();
		}
		catch (Exception e) {
			e.printStackTrace();
			LOG.throwing("ArduinoAPIHandler", "handleGetDataBrowser", e);
		}
	}
	
	public void handlePostStart1(RoutingContext context) {
		System.out.println("start 1");
	}
	
	public void handlePostStop1(RoutingContext context) {
		System.out.println("stop 1");
	}	
	
	public void handlePostStart2(RoutingContext context) {
		System.out.println("start 2");
	}
	
	public void handlePostStop2(RoutingContext context) {
		System.out.println("stop 2");
	}	
	
	public void handlePostStart3(RoutingContext context) {
		System.out.println("start 3");
	}
	
	public void handlePostStop3(RoutingContext context) {
		System.out.println("stop 3");
	}	
	
	public void handletWaterLevels(RoutingContext context) {
		
	}
}
