/*Client Server Communication via Sockets
Author- Aaditya Bagga	 Date- 29-09-2013
Client*/

import java.io.*;
import java.net.*;

class MyClient
{
    String sent="",recieved=null;
    ObjectInputStream ois=null;
    ObjectOutputStream oos=null;
    BufferedReader br=null;
    Socket s=null;
    
    public void runCli(String ipaddr,int portno)
    {
    	try
	{
		/*s=new Socket("localhost",1025);*/
		s=new Socket(ipaddr,portno);
		
		System.out.println("-----------------------------------------------------------\nClient-Server Chat Application \n-----------------------------------------------------------\nPress Ctrl^C or Alt+F4 to quit this application.");
			
		System.out.println("\nClient- Using port: "+s.getLocalPort()+"\nSuccessfully connected to Server.\nIP: "+s.getInetAddress()+"\tPort: "+s.getPort()+"\tName: "+s.getInetAddress().getHostName()+"\n");
		ois=new ObjectInputStream(s.getInputStream());
		oos=new ObjectOutputStream(s.getOutputStream());
		br=new BufferedReader(new InputStreamReader(System.in));
		
		int status=0;			
		while(true)
		{
			System.out.print("Enter message ");
			status=sendMessage();
			if (status == 1)
				break;
			
			System.out.println("\nWaiting for response... ");
			status=receiveMessage();
			if (status == 1)
				break;
				
		}
	}
		
	catch(Exception e)
	{
		System.out.println(e);
		System.out.println("Server not available");
	}
    }
    
    int sendMessage()
    {
	    try
	    {
		System.out.println("(Type /y to terminate the message) :");
		String temp=null;
		sent="";
			
		while(true)
		{
			temp=br.readLine();
			if(temp.equalsIgnoreCase("/y"))
				break;
			else
				sent=sent+temp+"\n";
			
		} 

		oos.writeObject(sent);
		oos.flush();
		return 0;
	    }
		
	   catch(Exception e)
	   {
		System.out.println("Error: "+e);
		System.out.println("Unable to reach server");
		return 1;
	   }
     }
	
     int receiveMessage()
     {
	try
	{
		recieved=(String)ois.readObject();
		System.out.println("\nSERVER->");
		System.out.println(recieved);
		return 0;
		
	}
		
	catch(Exception e)
	{
		System.out.println("Error: "+e);
		System.out.println("Server not reachable");
		return 1;
	}
    }
    
    public static void main(String args[]) throws Exception
	{
	    MyClient c1=new MyClient();

	    /*Parse command line options*/

		try {
			if(args[0] != null && args[1] != null);
			// If no error thrown
			c1.runCli(args[0],Integer.parseInt(args[1]));
		}
		catch(ArrayIndexOutOfBoundsException e1)
		{
			c1.runCli("127.0.0.1",1025);
			//System.out.println("Please enter arguments");
		}
	}
}
