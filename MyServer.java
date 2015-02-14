/*Client Server Communication via Sockets
*
*Copyright(C) 2014-2014 Aaditya Bagga < aaditya_gnulinux@zoho.com>
*
*  This program is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  any later version.
*
*  This program is distributed WITHOUT ANY WARRANTY;
*  without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*  See the GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*Server module
*/

import java.io.*;
import java.net.*;
import common.Server;

class MyServer
{		
    public void runSer(int sock)
    {
	try
	{
		// Create a server socket
		ServerSocket ss = new ServerSocket(sock);
	
		System.out.println("-----------------------------------------------------------\nClient-Server Chat Application \n-----------------------------------------------------------\nPress Ctrl^C or Alt+F4 to quit this application.\n");
			
		// Wait for connection
		System.out.println("Server- Using port: " + ss.getLocalPort() + "\nWaiting for connection from Client..\n");
		Socket s = ss.accept();

		// Got the connection
		System.out.println("Connection received from client " + s.getRemoteSocketAddress() + " (" + s.getInetAddress().getHostName() + ")");
		System.out.println("\nWaiting for response... ");

		// Get Input and Output streams
		// Output stream needs to be obtained first else it fails (why?)
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.flush();	// Why?
		ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Create an object of the server class and pass it the parameters
		Server ser = new Server("Server", ois, oos);
		
		//Infinte recieve message - send message loop
		int status=0;
		while(true)
		{
			status = ser.recvMessage("Client");
			if (status == 1)
				break;

			System.out.print("\nEnter message ");
			status = ser.sendMessage("Client");
			if (status == 1)
				break;
				
			System.out.println("\nWaiting for response... ");
		}
	}
		
	catch(Exception e)
	{
		System.out.println(e);
		// Can we not exit if client disconnects?
		System.exit(1);
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
			/*Default port no when no command line argument specified*/
			s.runSer(1025);
		}

	}
}
