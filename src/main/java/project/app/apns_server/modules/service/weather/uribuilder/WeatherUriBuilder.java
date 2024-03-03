package project.app.apns_server.modules.service.weather.uribuilder;

import java.net.URI;

public abstract class WeatherUriBuilder {

    public abstract URI buildUriByLocation(double lat, double lon);
}
