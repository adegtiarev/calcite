/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SocketFactoryImpl.java
*    
*    Copyrights:
*      copyright (c) nicolas gallagher and jonathan neal
*      copyright (c) 2014 alexander farkas (afarkas)
*      copyright (c) 2013 coby chapple
*      copyright 2008 google inc.  all rights reserved
*      copyright (c) 2013 scott jehl
*      copyright (c) 2008-present tom preston-werner and jekyll contributors
*    
*    Licenses:
*      Apache License 2.0
*      SPDXId: Apache-2.0
*    
*    Auto-attribution by Threatrix, Inc.
*    
*    ------ END LICENSE ATTRIBUTION ------
*/
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.runtime;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import javax.net.SocketFactory;

/**
 * Extends the SocketFactory object with the main functionality being that the
 * created sockets inherit a set of options whose values are set in the
 * SocketFactoryImpl.
 *
 * <blockquote><pre>
 * 1.  SO_KEEPALIVE          - is keepalive enabled?
 * 2.  OOBINLINE             - is out of band in-line enabled?
 * 3.  SO_REUSEADDR          - should the address be reused?
 * 4.  TCP_NODELAY           - should data buffering for tcp be used?
 * 5.  SO_RCVBUF             - size of receive buffer
 * 6.  SO_SNDBUF             - size of send buffer
 * 7.  SO_TIMEOUT            - read timeout (millisecs)
 * 8.  SO_CONNECT_TIMEOUT    - connect timeout (millisecs)
 * 9.  SO_LINGER             - is lingering enabled?
 * 10. LINGER                - amount of time to linger (seconds)
 * </pre></blockquote>
 */
public class SocketFactoryImpl extends SocketFactory {
  /**
   * Whether keep-alives should be sent.
   */
  public static final boolean SO_KEEPALIVE = false;

  /**
   * Whether out-of-band in-line is enabled.
   */
  public static final boolean OOBINLINE = false;

  /**
   * Whether the address should be reused.
   */
  public static final boolean SO_REUSEADDR = false;

  /**
   * Whether to not buffer send(s).
   */
  public static final boolean TCP_NODELAY = true;

  /**
   * Size of receiving buffer.
   */
  public static final int SO_RCVBUF = 8192;

  /**
   * Size of sending buffer iff needed.
   */
  public static final int SO_SNDBUF = 1024;

  /**
   * Read timeout in milliseconds.
   */
  public static final int SO_TIMEOUT = 12000;

  /**
   * Connect timeout in milliseconds.
   */
  public static final int SO_CONNECT_TIMEOUT = 5000;

  /**
   * Enabling lingering with 0-timeout will cause the socket to be
   * closed forcefully upon execution of {@code close()}.
   */
  public static final boolean SO_LINGER = true;

  /**
   * Amount of time to linger.
   */
  public static final int LINGER = 0;

  @Override public Socket createSocket() throws IOException {
    Socket s = new Socket();
    return applySettings(s);
  }

  /**
   * Applies the current settings to the given socket.
   *
   * @param s Socket to apply the settings to
   * @return Socket the input socket
   */
  protected Socket applySettings(Socket s) {
    try {
      s.setKeepAlive(SO_KEEPALIVE);
      s.setOOBInline(OOBINLINE);
      s.setReuseAddress(SO_REUSEADDR);
      s.setTcpNoDelay(TCP_NODELAY);
      s.setOOBInline(OOBINLINE);

      s.setReceiveBufferSize(SO_RCVBUF);
      s.setSendBufferSize(SO_SNDBUF);
      s.setSoTimeout(SO_TIMEOUT);
      s.setSoLinger(SO_LINGER, LINGER);
    } catch (SocketException e) {
      throw new RuntimeException(e);
    }
    return s;
  }

  @Override public Socket createSocket(String host, int port)
      throws IOException {
    Socket s = createSocket();
    s.connect(new InetSocketAddress(host, port), SO_CONNECT_TIMEOUT);
    return s;
  }

  @Override public Socket createSocket(InetAddress host, int port)
      throws IOException {
    Socket s = createSocket();
    s.connect(new InetSocketAddress(host, port), SO_CONNECT_TIMEOUT);
    return s;
  }

  @Override public Socket createSocket(String host, int port, InetAddress local,
      int localPort) throws IOException {
    Socket s  = createSocket();
    s.bind(new InetSocketAddress(local, localPort));
    s.connect(new InetSocketAddress(host, port), SO_CONNECT_TIMEOUT);
    return s;
  }

  @Override public Socket createSocket(InetAddress host, int port,
      InetAddress local, int localPort) throws IOException {
    Socket s  = createSocket();
    s.bind(new InetSocketAddress(local, localPort));
    s.connect(new InetSocketAddress(host, port), SO_CONNECT_TIMEOUT);
    return s;
  }

  /**
   * Returns a copy of the environment's default socket factory.
   *
   * @see javax.net.SocketFactory#getDefault()
   */
  public static SocketFactory getDefault() {
    return new SocketFactoryImpl();
  }
}
