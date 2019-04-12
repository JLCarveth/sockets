# Sockets
This is a collection of Client / Server programs made using the 
`java.net` package.   

**Echo Server**:
A simple single-threaded server. The server simply echoes whatever data is sent to it by the client.  

**Version Server**:
A multithreaded server that can handle multiple connections, VersionServer simply relays a software version to any connecting clients.
This can be used to implement an update server.  

**Chat Server**:
Another multithreaded socket implementation. Clients connect to the ChatServer, and can communicate instantly with other clients.

**Planned**:
- Simple Game Server (using gridlib)
- Software Update Server