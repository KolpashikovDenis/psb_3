package psb.kdv;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.net.URISyntaxException;

public class PSBHttpServer {

    public static void main(String[] args) {
        String fullName, jarName;
	// write your code here
        if (args.length != 1){
            System.out.println("Usage:\n\tjava -jar ");
        }
        try {
            fullName = new File(PSBHttpServer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
            jarName = fullName.substring(fullName.lastIndexOf(System.getProperty("line.separator"))+1);
            System.out.println(jarName);
        }catch(URISyntaxException e){
            e.printStackTrace();
        }
    }
}
