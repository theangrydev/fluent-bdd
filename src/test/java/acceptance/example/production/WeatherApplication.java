/*
 * Copyright 2016 Liam Williams <liam.williams@zoho.com>.
 *
 * This file is part of fluent-bdd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package acceptance.example.production;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class WeatherApplication {
    private final Server server;

    public WeatherApplication(int port, String weatherServiceUrl) {
        server = new Server(port);
        server.setHandler(handlers(weatherServiceUrl));
    }

    private HandlerList handlers(String weatherServiceUrl) {
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {requestLogHandler(), servletHandler(weatherServiceUrl)});
        return handlers;
    }

    private ServletHandler servletHandler(String weatherServiceUrl) {
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(new ServletHolder(new WeatherServlet(new OpenWeatherMapService(weatherServiceUrl))),"/weather");
        return servletHandler;
    }

    private RequestLogHandler requestLogHandler() {
        NCSARequestLog requestLog = new NCSARequestLog("access.log");
        requestLog.setAppend(true);
        requestLog.setExtended(false);
        requestLog.setLogTimeZone("GMT");
        requestLog.setLogLatency(true);
        requestLog.setRetainDays(90);

        RequestLogHandler requestLogHandler = new RequestLogHandler();
        requestLogHandler.setRequestLog(requestLog);
        return requestLogHandler;
    }

    public void start() {
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException("Could not start the server", e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException("Could not stop the server", e);
        }
    }

    public String baseUrl() {
        return server.getURI().toString();
    }
}
