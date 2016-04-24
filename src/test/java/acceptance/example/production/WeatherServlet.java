package acceptance.example.production;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

public class WeatherServlet extends HttpServlet {

    private final OpenWeatherMapService openWeatherMapService;

    public WeatherServlet(OpenWeatherMapService openWeatherMapService) {
        this.openWeatherMapService = openWeatherMapService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String city = req.getParameter("city");
        String weatherDescription = openWeatherMapService.fetchWeatherFor(city);
        resp.getWriter().write(format("There is %s in %s", weatherDescription, city));
    }
}
