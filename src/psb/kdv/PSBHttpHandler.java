package psb.kdv;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.IOException;
import java.io.OutputStream;

public class PSBHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

    }

    private String handleGetRequest(HttpExchange exchange){
        return exchange
                .getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];
    }

    private void handleResponse(HttpExchange exchange, String reqParamValue) throws IOException{
        OutputStream outputStream = exchange.getResponseBody();
        StringBuilder builder = new StringBuilder();

        builder.append("<html><body><h1>Hello from my little server")
                .append(reqParamValue)
                .append("</h1></body></html>");

        exchange.sendResponseHeaders(200, builder.toString().length());
        outputStream.write(builder.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
