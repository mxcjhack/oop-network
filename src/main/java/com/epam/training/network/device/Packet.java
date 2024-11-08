package com.epam.training.network.device;

public record Packet(IP source, IP destination, PacketType packetType, String message) {
}
