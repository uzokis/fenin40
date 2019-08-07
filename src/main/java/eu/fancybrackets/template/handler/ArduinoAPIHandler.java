package eu.fancybrackets.template.handler;

import java.util.Date;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jooq.DSLContext;

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
}
