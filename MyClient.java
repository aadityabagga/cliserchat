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
		System.out.println("(Type /y to terminate the message, or /quit to end conversation):");
		String temp=null;
		sent="";
			
		while(true)
		{
			temp=br.readLine();
			if(temp.equalsIgnoreCase("/y"))
				break;
			else if(temp.equalsIgnoreCase("/quit")) {
				if(sent.equals("")) {
					//Flush stream and exit
					oos.flush();
					return 1;
				} else {
					oos.writeObject(sent+"(Client quit the conversation)\n");
					oos.flush();
					return 1;
				}
			}
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
	    String ipaddr="127.0.0.1";

	    /*Parse command line options*/

		try {
			if(args[0] != null) {
				ipaddr=args[0];
				if(args[1] != null) {
					// If no error thrown
					c1.runCli(args[0],Integer.parseInt(args[1]));
				}
				
			}
			
			
		}
		catch(ArrayIndexOutOfBoundsException e1)
		{
			c1.runCli(ipaddr,1025);
			//System.out.println("Please enter arguments");
		}
	}
}
