package eu.fancybrackets.template.verticle;

import javax.inject.Inject;

import eu.fancybrackets.template.handler.ArduinoAPIHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class ArduinoAPIVerticle extends AbstractVerticle {
	@Inject
	private Router router;
	
	@Inject
	private ArduinoAPIHandler arduinoAPIHandler;
	
	public static String API_PARAM_CONTAINER_ID = "containerid";
	
	@Override
	public void start() throws Exception {
		super.start();
		this.router.route("/api/*").handler(BodyHandler.create());//allows for easy body parsing in the other handlers
		this.router.route(String.format("/api/:%s",API_PARAM_CONTAINER_ID)).method(HttpMethod.GET).handler(arduinoAPIHandler::handleGet);
		this.router.route(String.format("/api/:%s",API_PARAM_CONTAINER_ID)).method(HttpMethod.POST).handler(arduinoAPIHandler::handlePost);
		//TODO add routes
	}
}
