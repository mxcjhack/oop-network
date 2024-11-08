# OOP Network exercise

In this exercise your task is to implement a basic networking solution with the given classes.

You can find a picture of the topology that is given in the `Application.java` as a routing table and actors such as routers and servers.

## Routing table

Routing table describes the connections and routes in the whole network system.
Every router has its own routing table.
In this exercise we represent the routing table as a `Map`, keys and values are both `IP` types.

Let's consider that we have two servers and two routers. `Server1 <--> Router1 <--> Router2 <--> Server2`

Routing Table 1 as a Map:

```json
[
  { "Server1" : "Server1" },  
  { "Router2" : "Router2" },
  { "Server2" : "Router2" }
]
```

Line by Line:
1. Server 1 connects to Router1 direclty, hence, key and value are the same
2. Router 2 connects to Router1 direclty, hence, key and value are the same
3. Server 2 can be reachable via Router 2, that is the next hop

* As we see above, when a device is connected to the router directly, the key and the value is the same at that entry.
* If the device (key of the entry) is not connected directly, the value part is the address of the router where 
it needs to go towards to find it.

Obviously, the other routing table (Routing Table 2) looks as follows:

```json
[
  { "Server2" : "Server2" },
  { "Router1" : "Router1" },
  { "Server1" : "Router1" }
]
```

The routing table is static, that means the initial table does not change during the simulation.

## Classes, enums, records and interfaces

The following class diagram describe the connection between classes.

![](https://raw.githubusercontent.com/epam-java-cre/exercise-specification-images/main/oop-network/domain.png)
![](https://raw.githubusercontent.com/epam-java-cre/exercise-specification-images/main/oop-network/device.png)

We have two different devices in the system. We have servers which we can ping, and routers that can receive a packet
and can forward it to the next hop based on the routing table. 

* **Device** is an interface containing two methods, `getIP(): IP` and `receive(Packet): Packet`. 
The latter is responsible for forwarding the packets.
* **Router** is representing an entity which forwards the given packet, implementing all the methods of the interface of `Device`. 
Router has a field called `devices` containing all the connected devices whether it is a router or a server.
This class also has a method, called `connect(Device): void` to set up connection between devices.
* **Server** is an actor that we can use to ping (`ping(IP): Packet`) another one or forward (`receive(Packet): Packet`) a packet to.
As the diagram shows `Server` is inherited from `Device`.
* `IP` and `Packet` types are *records*

> * Don't forget to set up a connection between the server instance and the belonging router.
> * When you ping another server, your server should call the `receive` method of its `router`.
> * Use `PING` and `PONG` appropriately.
> * An instance of a `Router` forwards the packets, but never accepts them.
