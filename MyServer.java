/*Client Server Communication via Sockets

Copyright (C) 2014  Aaditya Bagga  aaditya_gnulinux@zoho.com

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  any later version.

  This program is distributed WITHOUT ANY WARRANTY;
  without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.

Server*/

import java.io.*;
import java.net.*;

class MyServer
{		
    String sent="",recieved=null;
    ObjectInputStream ois=null;
    ObjectOutputStream oos=null;
    BufferedReader br=null;
    ServerSocket s=null;
    
    public void runSer(int sock)
    {
	try
	{
		//1. Create a server socket
		s=new ServerSocket(sock);
	
		System.out.println("-----------------------------------------------------------\nClient-Server Chat Application \n-----------------------------------------------------------\nPress Ctrl^C or Alt+F4 to quit this application.\n");
			
		//2. Wait for connection
		System.out.println("Server- Using port: "+s.getLocalPort()+"\nWaiting for connection from Client..\n");
		Socket connection=s.accept();
		System.out.println("Connection received from client.\nIP: "+connection.getInetAddress()+"\tPort: "+connection.getPort()+"\tName: "+connection.getInetAddress().getHostName());
		System.out.println("\nWaiting for response... ");

		//3. Get Input and Output streams
		oos=new ObjectOutputStream(connection.getOutputStream());
		oos.flush();
		ois=new ObjectInputStream(connection.getInputStream());
		
		br=new BufferedReader(new InputStreamReader(System.in));
		
		int status=0;
		while(true)
		{
			status=receiveMessage();
			if (status == 1)
				break;

			System.out.print("Enter message ");					
			status=sendMessage();
			if (status == 1)
				break;
				
			System.out.println("\nWaiting for response... ");
		}
	}
		
	catch(Exception e)
	{
		System.out.println(e);
		System.exit(1);
	}	
		
    }
    
    int receiveMessage()
    {
	try
	{
		recieved=(String)ois.readObject();
		System.out.println("\nCLIENT->");
		System.out.println(recieved);
		return 0;
				
	}
	
	catch(Exception e)
	{
		System.out.println("Error: "+e);
		System.out.println("Connection lost from client");
		return 1;
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
		System.out.println("Error "+e);
		System.out.println("Unable to reach client");
		return 1;
		
	}
      }
		
	public static void main(String args[])
	{
	    MyServer s=new MyServer();

	    /*Parse command line options*/

		try {
			if(args[0] != null);
			// If no error thrown
			s.runSer(Integer.parseInt(args[0]));
		}
		catch(ArrayIndexOutOfBoundsException e1)
		{
			s.runSer(1025);
			//System.out.println("Please enter arguments");
		}

	}
}  	    
	
			
