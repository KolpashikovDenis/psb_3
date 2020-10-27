package psb.kdv;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class PSBHttpServer {
    static HttpServer myserver;

    public static void main(String[] args) {
        String fullName, jarName;
	// write your code here
        if (args.length != 1){
            System.out.println("Usage:\n\tjava -jar PSBHttpServer.jar <port>");
        }

        int port = Integer.parseInt(args[0]);

        ThreadPoolExecutor pePool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        try{
            myserver = HttpServer.create(new InetSocketAddress("localhost", port), 0);
            myserver.createContext("/test", new PSBHttpHandler());
            myserver.setExecutor(pePool);
            myserver.start();

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
