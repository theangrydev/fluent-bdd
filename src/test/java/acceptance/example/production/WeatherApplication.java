package acceptance.example.production;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class WeatherApplication {
    private final Server server;

    public WeatherApplication(int port, String weatherServiceUrl) {
        server = new Server(port);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(new WeatherServlet(new OpenWeatherMapService(weatherServiceUrl))),"/weather");
    }

    public void start() {
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException("Could not start the server");
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException("Could not stop the server");
        }
    }

    public String baseUrl() {
        return server.getURI().toString();
    }
}
