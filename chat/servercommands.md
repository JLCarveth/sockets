# ChatServer Command List 

A list of all command words the server uses to communicate with the chat client. Not the same as chat commands used by users.
Command | Description | Example
| --- | --- | ---|
SELECTNAME | Indicates a client needs to provide the server with a valid username to display to other clients. | `SELECTNAME` 
NAMEGOOD | Indicates the server has accepted a client's username. Once this command is recieved the client may begin sending abritrary text strings to connected clients. | `NAMEGOOD`
MESSAGE | Indicates a message from either the server or another client. The attached message should be displayed to the user. | `MESSAGE Hello Frank`
WHOTHERE | Informs the client who is connected to the ChatServer | `WHOTHERE Erika,Billy,Carl,Megatron,Sam`