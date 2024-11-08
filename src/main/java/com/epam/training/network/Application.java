package com.epam.training.network;

import com.epam.training.network.device.IP;
import com.epam.training.network.device.Packet;
import com.epam.training.network.device.Router;
import com.epam.training.network.device.Server;

import java.util.HashMap;
import java.util.Map;

public class Application {
    private static final String SERVER_DOMAIN = "10.10";
    private static final String ROUTER_DOMAIN = "45.65";

    private static final String SERVER_1 = SERVER_DOMAIN + ".1.1";
    private static final String SERVER_2 = SERVER_DOMAIN + ".2.1";
    private static final String SERVER_3 = SERVER_DOMAIN + ".3.1";
    private static final String SERVER_4 = SERVER_DOMAIN + ".3.2";
    private static final String ROUTER_1 = ROUTER_DOMAIN + ".1.1";
    private static final String ROUTER_2 = ROUTER_DOMAIN + ".2.1";
    private static final String ROUTER_3 = ROUTER_DOMAIN + ".3.1";

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }

    private void run() {
        Map<IP, IP> r1Table = new HashMap<>() {{
            put(new IP(SERVER_1), new IP(SERVER_1));
            put(new IP(SERVER_2), new IP(ROUTER_2));
            put(new IP(SERVER_3), new IP(ROUTER_3));
            put(new IP(SERVER_4), new IP(ROUTER_3));
            put(new IP(ROUTER_2), new IP(ROUTER_2));
            put(new IP(ROUTER_3), new IP(ROUTER_3));
        }};

        Map<IP, IP> r2Table = new HashMap<>() {{
            put(new IP(SERVER_2), new IP(SERVER_2));
            put(new IP(SERVER_1), new IP(ROUTER_1));
            put(new IP(SERVER_3), new IP(ROUTER_1));
            put(new IP(SERVER_4), new IP(ROUTER_1));
            put(new IP(ROUTER_3), new IP(ROUTER_1));
            put(new IP(ROUTER_1), new IP(ROUTER_1));
        }};

        Map<IP, IP> r3Table = new HashMap<>() {{
            put(new IP(SERVER_1), new IP(ROUTER_1));
            put(new IP(SERVER_2), new IP(ROUTER_1));
            put(new IP(SERVER_3), new IP(SERVER_3));
            put(new IP(SERVER_4), new IP(SERVER_4));
            put(new IP(ROUTER_1), new IP(ROUTER_1));
            put(new IP(ROUTER_2), new IP(ROUTER_1));
        }};

        Router r1 = new Router(new IP(ROUTER_1), r1Table);
        Router r2 = new Router(new IP(ROUTER_2), r2Table);
        Router r3 = new Router(new IP(ROUTER_3), r3Table);

        Server server1 = new Server(SERVER_1, r1);
        Server server2 = new Server(SERVER_2, r2);
        Server server3 = new Server(SERVER_3, r3);
        Server server4 = new Server(SERVER_4, r3);

        r2.connect(r1);
        r1.connect(r2);
        r1.connect(r3);
        r3.connect(r1);

        Packet pong = server1.ping(new IP(SERVER_4));
        System.out.println(server1.getIP() + "   " + SERVER_4);
        System.out.println(pong);
    }
}
