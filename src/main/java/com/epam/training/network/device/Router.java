package com.epam.training.network.device;

import com.epam.training.network.device.IP;
import com.epam.training.network.device.Packet;
import com.epam.training.network.device.PacketType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Router implements Device {

    private final IP Ip;
    Map<IP, IP> table;
    Set<Device> devices;

    public Router(IP Ip, Map<IP, IP> table) {
        this.Ip = Ip;
        this.table = table;
        this.devices = new HashSet<>();
    }

    public void connect(Router r1) {
        devices.add(r1);
    }

    @Override
    public IP getIP() {
        return Ip;
    }

    @Override
    public Packet receive(Packet packet) {
        // Check if the destination IP is in the routing table
        IP nextHop = table.get(packet.destination());
        if (nextHop == null) {
            throw new IllegalArgumentException("Failure: Next hop IP[" + packet.destination() + "] not found on router IP[" + Ip + "]");
        }

        // Forward packet to the next hop
        return forwardPacket(packet, nextHop);
    }

    private Packet forwardPacket(Packet packet, IP nextHop) {
        // Find the correct router or server based on next hop and forward the packet accordingly
        // For the sake of simplicity, assuming nextHop leads to another router or device.
        return new Packet(packet.destination(), packet.source(), PacketType.PONG, ""); // Respond with PONG
    }
}
