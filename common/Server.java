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

package common;

import java.io.*;
import java.net.*;

public class Server
{		
	// Data items
	String sent = "";
	String recieved = "";
	String whoami="";
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	// Initialization
	public Server (String w, ObjectInputStream ois, ObjectOutputStream oos)
	{
		whoami = w;
		this.ois = ois;
		this.oos = oos;
	}
    
    public int recvMessage(String recvfrom)
    {
	try
	{
		recieved = (String)ois.readObject();
		System.out.println("\n" + recvfrom + ":\n");
		System.out.println(recieved);
		return 0;
	}
	catch(Exception e)
	{
		System.out.println("Error: "+e);
		System.out.println("Connection lost from " + recvfrom);
		return 1;
	}
		
    }

	// getMessage split from sendMessage to allow encrypting of messages
	// Idea of encrypting thx to tibs245
	int getMessage()
	{
		/* Return codes
		 * 0 = relevant message
		 * 1 = return
		 * 2 = return with whoami quit message
		 */
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("(Type /y to terminate the message, or /quit to end conversation):");
			String temp=null;
			sent = ""; // needed else erronous output, why?
			
			// Get the message
			while(true)
			{
				temp=br.readLine();
				if(temp.equalsIgnoreCase("/y"))
					return 0;
				else if(temp.equalsIgnoreCase("/quit")) {
					if(sent.equals(""))
					{
						return 1;
					}
					else
					{
						sent = sent + "(" + whoami + " quit the conversation)\n";
						return 2;
					}
				}
				else
				{
					sent = sent + temp + "\n";
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		return 1; // just to please the compiler
	}
    	public int sendMessage(String sendto)
	{
	    try
	    {
		int status = getMessage();			
		if(status == 0)
		{
			oos.writeObject(sent);
			oos.flush();
			return 0;
		}
		else if (status == 2)
		{
			// whoami quit message
			oos.writeObject(sent);
			oos.flush();
			return 1;
		}
		else
		{
			// no message
			oos.flush();
			return 1;
		}
	}
	catch(Exception e)
	{
		System.out.println("Error " + e);
		System.out.println("Unable to reach " + sendto);
		return 1;
	}
    }
}
