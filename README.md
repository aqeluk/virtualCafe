# Virtual Café System - Java Client-Server Architecture

The Virtual Café System is a client-server application that simulates the process of ordering tea or coffee from a virtual barista in a café. The client application simulates a customer who can order tea or coffee, ask for the order status, or leave the café. The server application simulates the role of the virtual barista who processes the orders, prepares the tea and coffee, and delivers the order back to the customer.

## Compilation and Execution

To compile and run the server application on a terminal, use the following command:

    javac -cp "." Barista.java
    java -cp "." Barista

To compile and run the client application on a terminal, use the following command:

    javac -cp "." Customer.java
    java -cp "." Customer

On Windows, use a semicolon (;) instead of a colon (:) for the classpath.

## Customer Client Role

The client can order tea or coffee, check their order status, or leave the café by typing commands in the terminal. They can order multiple items using commands like "order 1 tea" or "order 2 teas and 3 coffees". The barista will send replies to the terminal.

## Barista Server Role

The server oversees the waiting, brewing, and tray areas in the café. It listens for incoming connections from clients, asks for their name, and accepts their orders. If the server receives an unknown command, it sends an error message to the client. When an order is complete, the server notifies the client by name and removes the relevant items from the tray area. The server logs the state in the terminal and in a JSON file. Clients can leave gracefully using SIGTERM signals. The server can also transfer items between orders belonging to different clients.
