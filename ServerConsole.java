//cs314 P1 Jon McQuillan, Matt Loidolt, Zach Cable

import java.io.*;

import common.*;
import ocsf.server.*;
import ocsf.client.*;

public class ServerConsole implements ChatIF {

	EchoServer server; //instance of Echoserver
	
	/**
	 * Constructor to be able to use the current server
	 * @param server
	 */
	ServerConsole(EchoServer server){
		this.server = server;
	}
	

	/**
	 * accepts input to the server console.  Sends input formatted as commands
	 * to executeServerCommand()
	 */
	public void accept() {
		/*try {
			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
			String message;

			while (true) {
				message = fromConsole.readLine();
				if (!message.startsWith("#", 0)) {
					System.out.println("Message received: " + message + " from server");
					display(message);
				} else {
					executeServerCommand(message);
				}
			}
		} catch (Exception ex) {
			System.out.println("Unexpected error while reading from console!");
		} */
	}
	
	/**
	 * executes various valid commands given to the server console.
	 * 
	 * @param message : command to be executed
	 */
	public void executeServerCommand(String message){
		try{
			if (message.equals("#quit")) {//quits the server and closes the program
				quit();
			} else if (message.equals("#stop")) {//stops the server from listening for new connections
				stop();
			} else if (message.equals("#close")) {//closes the server to all clients
				close();
			} else if (message.startsWith("#setport")) {//sets a new server port
					int port = Integer.parseInt(message.substring(8, message.length() - 1).trim());
					setPort(port);
			} else if (message.equals("#start")) {//starts a server listening for connections
				start();
			} else if (message.equals("#getport")) {//gets the current port
				getPort();
			} else {//default message if illegal command issued
				System.out.println("Valid commands prefaced with '#' are: quit,"
						+ " stop, close, setport <port>, start, and getport.");
			}
		} catch(Exception ex) {
			System.out.println("Unexpected error while reading from console!");
		}
	}
	
	/**
	 * Quits the server and sends a message to all clients that the server is quitting
	 * @throws IOException
	 */
	private void quit() throws IOException{
		display("Server is exiting.");
		server.close();
		System.out.println("Server is exiting.");
		System.exit(0);
	}
	
	/**
	 * Stops the server from listening for new connections
	 */
	private void stop(){
		display("Server has stopped listening for new connections.");
		server.stopListening();
	}
	
	/**
	 * Closes the server for all connections
	 * @throws IOException
	 */
	private void close() throws IOException{
		display("Server is closed.");
		server.close();
	}
	
	/**
	 * Sets a new port for the server to listen on if the server is closed
	 * @param port New port for the server to lisen on
	 */
	private void setPort(int port){
		if (!server.isListening()) {
			display("Server port now set to: " + port);
			server.setPort(port);
		} else {
			System.out.println("Server needs to be closed to change port.");
		}
	}
	
	/**
	 * Starts a server listening for new connections
	 * @throws IOException
	 */
	private void start() throws IOException{
		if (!server.isListening()) {
			display("Server has started listening for new connections.");
			server.listen();
		} else {
			System.out.println("Server is already started.");
		}
	}
	
	/**
	 * Gets the port that the server is listening on and prints it to the console
	 */
	private void getPort(){
		int port = server.getPort();
		display("Server port is: " + port);
	}

	/**
	 * This method overrides the method in the ChatIF interface. It displays a
	 * message onto the screen.
	 * 
	 * @param message The string to be displayed.
	 */
	public void display(String message) {
		//todo
		//change so server sends all messages as user SERVER MSG
		server.getClientConnections();
		String msg = "SERVER MSG> " + message;
		server.sendToAllClients(msg);
	}
}