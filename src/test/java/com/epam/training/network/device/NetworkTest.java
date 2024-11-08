package com.epam.training.network.device;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NetworkTest {

    private static final String SERVER_DOMAIN = "1.1";
    private static final String ROUTER_DOMAIN = "234.234";
    private static final String SERVER_1 = SERVER_DOMAIN + ".1.1";
    private static final String SERVER_2 = SERVER_DOMAIN + ".1.2";
    private static final String SERVER_3 = SERVER_DOMAIN + ".3.1";
    private static final String ROUTER_1 = ROUTER_DOMAIN + ".1.1";
    private static final String ROUTER_2 = ROUTER_DOMAIN + ".2.1";
    private static final String ROUTER_3 = ROUTER_DOMAIN + ".3.1";
    private Server underTest;

    @BeforeEach
    void setUp() {
        Map<IP, IP> r1Table = new HashMap<>() {{
            put(new IP(SERVER_1), new IP(SERVER_1));
            put(new IP(SERVER_2), new IP(SERVER_2));
            put(new IP(ROUTER_2), new IP(ROUTER_2));
            put(new IP(ROUTER_3), new IP(ROUTER_2));
            put(new IP(SERVER_3), new IP(ROUTER_2));
        }};

        Map<IP, IP> r2Table = new HashMap<>() {{
            put(new IP(ROUTER_1), new IP(ROUTER_1));
            put(new IP(ROUTER_3), new IP(ROUTER_3));
            put(new IP(SERVER_1), new IP(ROUTER_1));
            put(new IP(SERVER_2), new IP(ROUTER_1));
            put(new IP(SERVER_3), new IP(ROUTER_3));
        }};

        Map<IP, IP> r3Table = new HashMap<>() {{
            put(new IP(ROUTER_2), new IP(ROUTER_2));
            put(new IP(SERVER_3), new IP(SERVER_3));
            put(new IP(ROUTER_1), new IP(ROUTER_2));
            put(new IP(SERVER_1), new IP(ROUTER_2));
            put(new IP(SERVER_2), new IP(ROUTER_2));
        }};

        Router r1 = new Router(new IP(ROUTER_1), r1Table);
        Router r2 = new Router(new IP(ROUTER_2), r2Table);
        Router r3 = new Router(new IP(ROUTER_3), r3Table);

        r1.connect(r2);
        r2.connect(r1);
        r2.connect(r3);
        r3.connect(r2);

        Server server2 = new Server(SERVER_2, r1);
        Server server3 = new Server(SERVER_3, r3);

        underTest = new Server(SERVER_1, r1);
    }

    @DisplayName("Sending ping packet from server to neighbour server runs successfully")
    @Test
    public void testSendPingPacketFromServerToNeighbourServer() {

        Packet expected = new Packet(new IP(SERVER_2), new IP(SERVER_1), PacketType.PONG, "");
        // Act
        Packet actual = underTest.ping(new IP(SERVER_2));

        // Assert
        assertEquals(expected, actual);
    }

    @DisplayName("Sending ping packet from server to another server runs successfully")
    @Test
    public void testSendPingPacketFromServerToAnotherServer() {

        Packet expected = new Packet(new IP(SERVER_3), new IP(SERVER_1), PacketType.PONG, "");
        // Act
        Packet actual = underTest.ping(new IP(SERVER_3));

        // Assert
        assertEquals(expected, actual);
    }

    @DisplayName("Sending ping packet from server to itself runs successfully")
    @Test
    public void testSendPingPacketFromServerToItself() {
        Packet expected = new Packet(new IP(SERVER_1), new IP(SERVER_1), PacketType.PONG, "");
        // Act
        Packet actual = underTest.ping(new IP(SERVER_1));

        // Assert
        assertEquals(expected, actual);
    }

    @DisplayName("Sending ping packet from server to router raises failure")
    @Test
    public void testSendPingPacketFromServerToRouter() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> underTest.ping(new IP(ROUTER_1)));
    }

    @DisplayName("Sending ping packet from server to invalid address raises failure")
    @Test
    public void testSendPingPacketFromServerToNowhere() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> underTest.ping(new IP("120.120.120.120")));
    }

    @DisplayName("Sending pong packet from server to server raises failure")
    @Test
    public void testSendPongPacketFromServerToServer() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> underTest.receive(new Packet(underTest.getIP(), new IP(SERVER_3), PacketType.PONG, "")));
    }

    @DisplayName("Sending invalid packet from server to server raises failure")
    @Test
    public void testSendInvalidPacketFromServerToServer() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> underTest.receive(new Packet(underTest.getIP(), new IP(SERVER_3), null, "")));
    }
}
