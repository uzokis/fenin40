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
		
		//handle GET request from html file
		this.router.route("/browser/data").method(HttpMethod.GET)
		.handler(arduinoAPIHandler::handleGetDataHTML);
		
		//
		//request from html file:
		//
		
		//handle POST request from html file:
		//	start tank 1 
		this.router.route("/browser/START/tank1").method(HttpMethod.POST)
			.handler(arduinoAPIHandler::handlePostStart1);
		
		//handle POST request from html file:
		//	stop tank 1
		this.router.route("/browser/STOP/tank1").method(HttpMethod.POST)
			.handler(arduinoAPIHandler::handlePostStop1);
		
		//handle POST request from html file:
		//	start tank 2
		this.router.route("/browser/START/tank2").method(HttpMethod.POST)
			.handler(arduinoAPIHandler::handlePostStart2);
		
		//handle POST request from html file:
		//	stop tank 2
		this.router.route("/browser/STOP/tank2").method(HttpMethod.POST)
			.handler(arduinoAPIHandler::handlePostStop2);
		
		//handle POST request from html file:
		//	start tank 3
		this.router.route("/browser/START/tank3").method(HttpMethod.POST)
			.handler(arduinoAPIHandler::handlePostStart3);
	
		//handle POST request from html file:
		//	stop tank 3
		this.router.route("/browser/STOP/tank3").method(HttpMethod.POST)
			.handler(arduinoAPIHandler::handlePostStop3);
		
		// 
		//requests from and to arduino:
		//
		
		//handle POST request from arduino:
		//	water levels of tanks
		this.router.route("/arduino/waterLevels").handler(BodyHandler.create());
		this.router.route("/arduino/waterLevels").method(HttpMethod.POST)
			.handler(arduinoAPIHandler::handletWaterLevels);
		
	}
}
