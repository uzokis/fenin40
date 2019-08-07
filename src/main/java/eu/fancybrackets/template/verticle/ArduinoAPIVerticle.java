package eu.fancybrackets.template.verticle;

import javax.inject.Inject;

import eu.fancybrackets.template.handler.ArduinoAPIHandler;
import eu.fancybrackets.template.handler.RonSwansonHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;

public class ArduinoAPIVerticle extends AbstractVerticle {
	@Inject
	private Router router;
	
	@Inject
	private ArduinoAPIHandler arduinoAPIHandler;
	
	@Override
	public void start() throws Exception {
		super.start();
		this.router.route("/api/:containerid").method(HttpMethod.GET).handler(arduinoAPIHandler::handleGet);
		//TODO add routes
	}
}
