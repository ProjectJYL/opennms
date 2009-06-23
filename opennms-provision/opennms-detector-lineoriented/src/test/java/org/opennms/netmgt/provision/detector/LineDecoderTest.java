/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2008 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 * OpenNMS Licensing       <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 */
package org.opennms.netmgt.provision.detector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opennms.netmgt.provision.DetectFuture;
import org.opennms.netmgt.provision.detector.simple.AsyncLineOrientedDetector;
import org.opennms.netmgt.provision.server.SimpleServer;
import org.opennms.netmgt.provision.server.exchange.RequestHandler;
import org.opennms.netmgt.provision.support.NullDetectorMonitor;

/**
 * @author Donald Desloge
 *
 */
public class LineDecoderTest {
    
    public static class TestServer extends SimpleServer{
        
        @Override
        protected void sendBanner(OutputStream out) throws IOException {
            String[] tokens = getBanner().split("");
            
            for(int i = 0; i < tokens.length; i++) {
                String str = tokens[i];
                out.write(str.getBytes());
                out.flush();
                
            }
            out.write("\r\n".getBytes());
            
        }
        
        @Override
        protected RequestHandler errorString(final String error) {
            return new RequestHandler() {

                public void doRequest(OutputStream out) throws IOException {
                    out.write(String.format("%s", error).getBytes());
                    
                }
                
            };
        }
        
        @Override
        protected RequestHandler shutdownServer(final String response) {
            return new RequestHandler() {
                
                public void doRequest(OutputStream out) throws IOException {
                    out.write(String.format("%s\r\n", response).getBytes());
                    stopServer();
                }
                
            };
        }
    }
    
    public static class TestDetector extends AsyncLineOrientedDetector{

        public TestDetector() {
            super("POP3", 110, 5000, 1);
           
        }

        @Override
        protected void onInit() {
            expectBanner(startsWith("+OK"));
            send(request("QUIT"), startsWith("+OK"));
        }
        
    }
    
    private TestServer m_server;
    private TestDetector m_detector;
    
    @Before
    public void setUp() throws Exception {
        m_server = new TestServer() {
            
            public void onInit() {
                setBanner("+OK");
                addResponseHandler(contains("QUIT"), shutdownServer("+OK"));
            }
        };
        m_server.init();
        m_server.startServer();
    }

    @After
    public void tearDown() throws Exception {
        if(m_server != null) {
            m_server.stopServer();
        }
    }
    
    @Test
    public void testSuccess() throws Exception {
        
        m_detector = createDetector(m_server.getLocalPort());
        m_detector.setIdleTime(100);
        assertTrue( doCheck( m_detector.isServiceDetected(m_server.getInetAddress(), new NullDetectorMonitor())));
    }
    
    
    @Ignore
    @Test
    public void testFailureWithBogusResponse() throws Exception {
        m_server.setBanner("Oh Henry");
        
        m_detector = createDetector(m_server.getLocalPort());
        
        assertFalse( doCheck( m_detector.isServiceDetected( m_server.getInetAddress(), new NullDetectorMonitor())));
        
    }
    
    @Ignore
    @Test
    public void testMonitorFailureWithNoResponse() throws Exception {
        m_server.setBanner(null);
        m_detector = createDetector(m_server.getLocalPort());
        
        assertFalse( doCheck( m_detector.isServiceDetected( m_server.getInetAddress(), new NullDetectorMonitor())));
        
    }
    
    @Ignore
    @Test
    public void testDetectorFailWrongPort() throws Exception{
        
        m_detector = createDetector(9000);
        
        assertFalse( doCheck( m_detector.isServiceDetected( m_server.getInetAddress(), new NullDetectorMonitor())));
    }
    
    private TestDetector createDetector(int port) {
        TestDetector detector = new TestDetector();
        detector.setServiceName("TEST");
        detector.setTimeout(1000);
        detector.setPort(port);
        detector.init();
        return detector;
    }
    
    private boolean  doCheck(DetectFuture future) throws Exception {
        
        future.await();
        
        return future.isServiceDetected();
    }
}