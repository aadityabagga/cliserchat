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

Client module*/

import java.io.*;
import java.net.*;
import common.Server;

class MyClient
{
	public void runCli(String ipaddr,int portno)
	{
		try
		{
			/*Create a socket for connecting to server*/
			Socket s = new Socket(ipaddr,portno);

			System.out.println("-----------------------------------------------------------\nClient-Server Chat Application \n-----------------------------------------------------------\nPress Ctrl^C to terminate this application.");

			System.out.println("\nCurrent role: Client \nUsing port: "+s.getLocalPort()+"\nSuccessfully connected to Server "+s.getRemoteSocketAddress() + " (" + s.getInetAddress().getHostName()+")\n");

			/*Create streams for input and output*/
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			// Create an object of the server class and pass it the parameters
			Server cli = new Server("Client", ois, oos);

			/*Infinite send - recieve loop*/
			int status=0;
			while(true)
			{
				System.out.print("\nEnter message ");
				status = cli.sendMessage("Server");
				if (status == 1)
					break;

				System.out.println("\nWaiting for response... ");
				status = cli.recvMessage("Server");
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
    
	public static void main(String args[]) throws Exception
	{
		MyClient c1=new MyClient();
		String ipaddr="127.0.0.1";	/*Default IP address of server = localhost*/

		/*Parse command line options*/
		try
		{
			if(args[0] != null)
			{
				ipaddr=args[0];
				if(args[1] != null)
				{
					// If no error thrown
					c1.runCli(args[0],Integer.parseInt(args[1]));
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e1)
		{
			/*Default port for connection is 1025*/			
			c1.runCli(ipaddr,1025);

		}
	}
}
