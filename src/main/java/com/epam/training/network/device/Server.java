// Server.java
package com.epam.training.network.device;

public class Server implements Device {

    private final IP ip;
    private final Router router;

    public Server(String server, Router router) {
        this.ip = new IP(server);
        this.router = router;
    }

    public Server(IP ip, Router router) {
        this.ip = ip;
        this.router = router;
    }

    @Override
    public IP getIP() {
        return ip;
    }

    @Override
    public Packet receive(Packet packet) {
        if (packet.packetType() == PacketType.PING) {
            // Valid response to a PING packet
            System.out.println("Server " + ip + " received PING from " + packet.source());
            return new Packet(ip, packet.source(), PacketType.PONG, "");  // Respond with PONG
        } else {
            // Invalid packet type received by server; throw an exception
            throw new IllegalArgumentException("Failure: Invalid packet type received by server " + ip);
        }
    }

    public Packet ping(IP targetIp) {
        // Self-ping
        if (targetIp.equals(this.ip)) {
            System.out.println("Server " + ip + " pinging itself.");
            return new Packet(ip, targetIp, PacketType.PONG, "");
        }

        // Create and send ping packet through the router
        Packet pingPacket = new Packet(ip, targetIp, PacketType.PING, "PING");
        Packet response = router.receive(pingPacket);

        if (response == null) {
            throw new IllegalArgumentException("Failure: No route to destination " + targetIp + " from server " + ip);
        }

        return response;
    }
}
