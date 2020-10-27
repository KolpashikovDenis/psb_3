package psb.kdv;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;

public class PSBHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String reqParamValue = null;
        if("GET".equals(httpExchange.getRequestMethod())){
            reqParamValue = handleGetRequest(httpExchange);
        } else if("POST".equals(httpExchange.getRequestMethod())){
            reqParamValue = handlePostRequest(httpExchange);
        }

        handleResponse(httpExchange, reqParamValue);
    }

    private String handleGetRequest(HttpExchange exchange){
        return exchange
                .getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];
    }

    private String handlePostRequest(HttpExchange exchange) throws IOException{
        InputStream input = exchange.getRequestBody();
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line;
        while((line = br.readLine()) != null){
            stringBuilder.append(line);
        }

        JSONParser parser = new JSONParser();


        return stringBuilder.toString();
    }

    private void handleResponse(HttpExchange exchange, String reqParamValue) throws IOException{
        OutputStream outputStream = exchange.getResponseBody();
        StringBuilder builder = new StringBuilder();

        builder.append(reqParamValue);

        exchange.sendResponseHeaders(200, builder.toString().length());
        outputStream.write(builder.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }


}
