//
// Copyright (C) 2002 Sortova Consulting Group, Inc.  All rights reserved.
// Parts Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.sortova.com/
//
//
// Tab Size = 8
//
//
//
package org.opennms.netmgt.capsd;

import java.lang.*;
import java.lang.reflect.UndeclaredThrowableException; 

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


import java.net.InetAddress;

import java.util.Map;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.opennms.core.utils.ThreadCategory;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.opennms.netmgt.utils.ParameterMap;
import org.opennms.netmgt.utils.ExecRunner;

/**
 * <P>This class is designed to be used by the capabilities
 * daemon to test for the existance of a generic service
 * by calling an external script or program. The  external
 * script or program will be passed two options: --hostname,
 * the IP address of the host to be tested, and --timeout,
 * the timeout in seconds. Additional options or arguments
 * can be specified in the capsd configuration.</P>
 *
 * @author <A HREF="mailto:mike@opennms.org">Mike</A>
 * @author <A HREF="mailto:weave@opennms.org">Weaver</A>
 * @author <A HREF="http://www.opennsm.org">OpenNMS</A>
 * @author <A HREF="mailto:ayres@net.orst.edu">Bill Ayres</A>
 *
 *
 */
public final class GpPlugin 
	extends AbstractPlugin
{	
	/**
	 * The protocol supported by the plugin
	 */
	private final static String 	PROTOCOL_NAME	= "GP";

	/**
	 * Default number of retries for GP requests
	 */
	private final static int	DEFAULT_RETRY	= 0;
	
	/**
	 * Default timeout (in milliseconds) for GP requests
	 */
	private final static int	DEFAULT_TIMEOUT	= 5000; // in milliseconds

	/**
	 * <P>Test to see if the passed script-host-argument
	 * combination is the endpoint for a GP server. If there
	 * is a GP server at that destination then a value of true
	 * is returned from the method. Otherwise a false value is
	 * returned to the caller.  In order to return true the script
	 * must generate a banner line which contains the text from
	 * the banner or match argument.</P>
	 *
	 * @param host		The host to pass to the script
	 * @param retry	        The number of retry attempts to make
	 * @param timeout	The timeout value for each retry
	 * @param script	The external script or program to call
	 * @param args		The arguments to pass to the script
	 * @param regex		The regular expression used to determine banner match
	 * @param bannerResult
	 *
	 * @return True if a connection is established with the script and the banner line
	 *		returned by the script matches the regular expression regex.
	 */
	private boolean isServer(InetAddress host, int retry, int timeout, String script, String args, RE regex, StringBuffer bannerResult)
	{
		Category log = ThreadCategory.getInstance(getClass());


		boolean isAServer = false;

		log.debug("poll: address = " + host.getHostAddress()
			  + ", script = " + script + ", arguments = " + args
			  + ", timeout(seconds) = " + timeout + ", retry = " + retry);


		for (int attempts=0; attempts <= retry && !isAServer; attempts++)
		{
			try
			{
				int exitStatus = 100;
				ExecRunner er = new ExecRunner();
				er.setMaxRunTimeSecs(timeout);
				if (args == null)
					exitStatus = er.exec(script + " --hostname " + host.getHostAddress() + " --timeout " +timeout);
				else
					exitStatus = er.exec(script + " --hostname " + host.getHostAddress() + " --timeout " +timeout + " " + args);
				if (exitStatus != 0)
				{
					log.debug(script + " failed with exit code " + exitStatus);
					isAServer = false;
				}
				if (er.isMaxRunTimeExceeded())
				{
					log.debug(script + " failed. Timeout exceeded");
					isAServer = false;
				}
				else
				{
					if ( exitStatus == 0 )
					{
						String response = "";
						String error = "";
						response = er.getOutString();
						error = er.getErrString();
						if (response.equals(""))
							log.debug(script + " returned no output");
						if (!error.equals(""))
							log.debug(script + " error = " + error);
						if(regex == null || regex.match(response))
						{
							if (log.isDebugEnabled())
								log.debug("isServer: matching response = " + response);
							isAServer = true;
							if(bannerResult != null)
								bannerResult.append(response);
						}
						else
						{
							isAServer=false;
							if (log.isDebugEnabled())
								log.debug("isServer: NON-matching response = " + response);
						}
					}
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				isAServer = false;
				e.fillInStackTrace();
				log.debug(script + " ArrayIndexOutOfBoundsException");
			}
			catch(InterruptedIOException e)
			{
				// This is an expected exception
				//
				isAServer = false;
			}
			catch(IOException e)
			{
				isAServer = false;
				e.fillInStackTrace();
				log.debug("IOException occurred. Check for proper operation of " + script);
			}
			catch(Exception e)
			{
				isAServer = false;
				e.fillInStackTrace();
				log.debug(script + " Exception occurred");
			}
		}
	
		//
		// return the status of the server
		//
		log.debug("poll: GP - isAServer = " + isAServer + "  " + host.getHostAddress());
		return isAServer;
	}


	/**
	 * Returns the name of the protocol that this plugin
	 * checks on the target system for support.
	 *
	 * @return The protocol name for this plugin.
	 */
	public String getProtocolName()
	{
		return PROTOCOL_NAME;
	}

	/**
	 * Returns true if the protocol defined by this
	 * plugin is supported. If the protocol is not 
	 * supported then a false value is returned to the 
	 * caller.
	 *
	 * @param address	The address to check for support.
	 *
	 * @return True if the protocol is supported by the address.
	 *
	 * @throws java.lang.UnsupportedOperationException This is always
	 * 	thrown by this plugin.
	 */
	public boolean isProtocolSupported(InetAddress address)
	{
		throw new UnsupportedOperationException("Undirected GP checking not supported");
	}

	/**
	 * Returns true if the protocol defined by this
	 * plugin is supported. If the protocol is not 
	 * supported then a false value is returned to the 
	 * caller. The qualifier map passed to the method is
	 * used by the plugin to return additional information
	 * by key-name. These key-value pairs can be added to 
	 * service events if needed.
	 *
	 * @param address	The address to check for support.
	 * @param qualifiers	The map where qualification are set
	 *			by the plugin.
	 *
	 * @return True if the protocol is supported by the address.
	 */
	public boolean isProtocolSupported(InetAddress address, Map qualifiers)
	{
		int retry = DEFAULT_RETRY;
		int timeout = DEFAULT_TIMEOUT;
		String banner = null;
		String match  = null;
		String script = null;
		String args = null;
		if(qualifiers != null)
		{
			retry = ParameterMap.getKeyedInteger(qualifiers, "retry", DEFAULT_RETRY);
			timeout = ParameterMap.getKeyedInteger(qualifiers, "timeout", DEFAULT_TIMEOUT);
			script = ParameterMap.getKeyedString(qualifiers, "script", null);
			args = ParameterMap.getKeyedString(qualifiers, "args", null);
			banner= ParameterMap.getKeyedString(qualifiers, "banner", null);
			match = ParameterMap.getKeyedString(qualifiers, "match", null);
		}
		if(script == null)
		{
			throw new RuntimeException("GpPlugin: required parameter 'script' is not present in supplied properties.");
		}

		//
		// convert timeout to seconds for ExecRunner
		//
		if (0 < timeout && timeout < 1000)
			timeout = 1;
		else
			timeout = timeout/1000;

		try
		{
			StringBuffer bannerResult = null;
			RE regex = null;
			if(match == null && (banner == null || banner.equals("*")))
			{
				regex = null;
			}
			else if(match != null)
			{
				regex = new RE(match);
				bannerResult = new StringBuffer();
			}
			else if(banner != null)
			{
				regex = new RE(banner);
				bannerResult = new StringBuffer();
			}

			boolean result = isServer(address, retry, timeout, script, args, regex, bannerResult);
			if(result && qualifiers != null)
			{
				if(bannerResult != null && bannerResult.length() > 0)
					qualifiers.put("banner", bannerResult.toString());
			}

			return result;
		}
		catch(RESyntaxException e)
		{
			throw new java.lang.reflect.UndeclaredThrowableException(e);
		}
	}
}
