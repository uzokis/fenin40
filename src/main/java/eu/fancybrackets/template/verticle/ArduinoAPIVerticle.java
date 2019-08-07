package eu.fancybrackets.template.verticle;

import javax.inject.Inject;

import eu.fancybrackets.template.handler.ArduinoAPIHandler;
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
		
		this.router.route("/api/:containerid").method(HttpMethod.GET)
		.handler(arduinoAPIHandler::handleGet);
		
		this.router.route("/browser/data").method(HttpMethod.GET)
		.handler(arduinoAPIHandler::handleGetDataHTML);
		
		this.router.route("/browser/START/tank1").method(HttpMethod.POST)
			.handler(arduinoAPIHandler::handlePostStart1);
		
		this.router.route("/browser/STOP/tank1").method(HttpMethod.POST)
			.handler(arduinoAPIHandler::handlePostStop1);
		
		this.router.route("/browser/START/tank2").method(HttpMethod.POST)
			.handler(arduinoAPIHandler::handlePostStart2);
	
		this.router.route("/browser/STOP/tank2").method(HttpMethod.POST)
			.handler(arduinoAPIHandler::handlePostStop2);
		
		this.router.route("/browser/START/tank3").method(HttpMethod.POST)
			.handler(arduinoAPIHandler::handlePostStart3);
	
		this.router.route("/browser/STOP/tank3").method(HttpMethod.POST)
			.handler(arduinoAPIHandler::handlePostStop3);
		
	}
}
