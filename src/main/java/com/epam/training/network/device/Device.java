package com.epam.training.network.device;

public interface Device {
    IP getIP();
    Packet receive(Packet packet);
}
