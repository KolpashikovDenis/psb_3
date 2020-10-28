package psb.kdv;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.*;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;

public class PSBHttpHandler implements HttpHandler {
    String[] opers = {"sum", "max", "min", "average", "median", "prime"};
    String resInt = "{\r\n\t\"result\":[%d]\r\n}";
    String resFloat = "{\r\n\t\"result\":[%f]\r\n}";
    String resError = "{\r\n\t\"result\":[%s]\r\n}";

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

        JSONObject root = new JSONObject(stringBuilder.toString());
        JSONArray arr = root.getJSONArray("numbers");
        Integer[] nums = new Integer[arr.length()];
        for(int i = 0; i < arr.length(); i++){
            nums[i] = Integer.parseInt(arr.get(i).toString());
        }

        // операция - sum, max, min, average, median, prime
        String oper = root.get("operation").toString();
        stringBuilder.setLength(0);

        if (oper.contains(opers[0])){
            int sum = 0;
            for(int n: nums){
                sum+=n;
            }
            stringBuilder.append(String.format(resInt, sum));
        } else if (oper.contains(opers[1])){
            Arrays.sort(nums, Collections.reverseOrder());
            stringBuilder.append(String.format(resInt, nums[0]));
        } else if (oper.contains(opers[2])){
            Arrays.sort(nums);
            stringBuilder.append(String.format(resInt, nums[0]));
        } else if (oper.contains(opers[3])){
            double f = 0.0;
            int sum = 0;
            for(int n: nums){
                sum += n;
            }
            f = (double)sum/nums.length;
            stringBuilder.append(String.format(resFloat, f));
        } else if (oper.contains(opers[4])){
            Arrays.sort(nums);
            // определим чётность количества значений массива
            if((nums.length % 2) == 1){

                stringBuilder.append(String.format(resInt, nums[(nums.length+1)/2-1]));
            } else {
                int i = nums.length/2;
                double avg = (double)(nums[i-1]+nums[i])/2;
                stringBuilder.append(String.format(resFloat, avg));
            }
        } else if (oper.contains(opers[5])){

        } else {
        }


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
