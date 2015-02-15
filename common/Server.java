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
	String whoami = "";
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	// Initialization
	public Server(String w, ObjectInputStream ois, ObjectOutputStream oos)
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
			System.out.println("\n" + recvfrom + ":");
			System.out.println(recieved);
			return 0;
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e);
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
		 * 1 = return with whoami quit message
		 */
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("(Type /y (on a new line, followed by enter) to terminate the message, or /quit (similar to /y) to end conversation):");
			String temp=null;
			sent = ""; // needed else erronous output, why?
			
			// Get the message
			while(true)
			{
				temp=br.readLine();
				if(temp.equalsIgnoreCase("/y"))
					return 0;
				else if(temp.equalsIgnoreCase("/quit"))
				{
					sent = sent + "(" + whoami + " quit the conversation)\n";
					return 1;
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
		    // Encrypt the message
		    //ObjectCrypter oc = new ObjectCrypter();
		    //sent = oc.encrypt();
		    //System.out.println(sent);
		    // Transmit the message
		    oos.writeObject(sent);
		    oos.flush();
		    // Quit if /quit was typed (status == 1)
		    if(status == 0)
		    {
			    return 0;
		    }
		    else
		    {
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
