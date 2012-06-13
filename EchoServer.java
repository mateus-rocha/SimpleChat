// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;
import java.util.ArrayList;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  ArrayList<String> users;
  String callingUser = "";
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
    users = new ArrayList<String>();
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
		if(((String) msg).startsWith("#login ")) {
			if(client.getInfo("loginID") == ( (String) msg).substring(7) ){
				try {
					client.sendToClient("You cannot login twice.") ;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				client.setInfo("loginID", ( (String) msg).substring(7) ) ;
				users.add( ((String) msg).substring(7) );
			}
		}
		///////////////////////////////////////////////////////////////////
		else if(((String) msg).startsWith("#whoblocksme")){
			this.sendToAllClients("#doyoublock " + client.getInfo("loginID")) ;
			// save the calling user
			callingUser = (String) client.getInfo("loginID") ;
		}
		else if(((String) msg).startsWith("#ifidoblock")){
			/* index to determine location of each block, corresponds to index in
		 	arraylist of users*/
			
			if(((String) msg).charAt(12) == 'y')
				this.sendToAllClients("#whoblocksme " + callingUser + " Messages to "
						+ (String) client.getInfo("loginID") + " are being blocked.") ;
		}
		///////////////////////////////////////////////////////////////////////////
		else {
			this.sendToAllClients(client.getInfo("loginID") + " >>  " + msg.toString());
		}
  }
    
  /**
	 * 
	 */
	public void clientConnected(ConnectionToClient client){
		System.out.println("The client "+client+" has connected.");
	}
	  
	/**
	 * 
	 */
	public void clientDisconnected(ConnectionToClient client){
		System.out.println("The client "+client +" has disconnected.");
		//users.remove(client.getInfo("loginID")) ;
	}
	
	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port "
				+ getPort());
	}

	/**
	 * Checks if a user is on the list of current valid users.  Persists even
	 * if a user logs off
	 * 
	 * @param user
	 * @return
	 */
	public boolean doesUserExist(String user){
		return (users.contains(user));
	}
	
	/**
	 * This method overrides the one in the superclass. Called when the server
	 * stops listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there
	 * is no UI in this phase).
	 * 
	 * @param args
	 *            [0] The port number to listen on. Defaults to 5555 if no
	 *            argument is entered.
	 */
	
	 public static void main(String[] args) { 
		 int port = 0; //Port to listen on

		 try { 
			 port = Integer.parseInt(args[0]); //Get port from command line 
		 }
		 catch(Throwable t) { 
			 port = DEFAULT_PORT; //Set port to 5555
		 }
	 
		 EchoServer sv = new EchoServer(port);
		 ServerConsole server = new ServerConsole(sv);
		 try { 
			 sv.listen(); //Start listening for connections 
		 } catch (Exception ex) { 
			 System.out.println("ERROR - Could not listen for clients!");
		 }
		 server.accept();
	 }
}
//End of EchoServer class
