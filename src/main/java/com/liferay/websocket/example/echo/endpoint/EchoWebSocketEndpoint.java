/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 * <p/>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.websocket.example.echo.endpoint;

import java.io.IOException;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import org.osgi.service.component.annotations.Component;

/**
 * @author Cristina González
 */
@Component(
	immediate = true,
	property = {"org.osgi.http.websocket.endpoint.path=/o/echo"},
	service = Endpoint.class
)
public class EchoWebSocketEndpoint extends Endpoint {

	public static final String ECHO_WEBSOCKET_PATH = "/o/echo";

	@Override
	public void onOpen(final Session session, EndpointConfig endpointConfig) {
		session.addMessageHandler(
			new MessageHandler.Whole<String>() {

				@Override
				public void onMessage(String text) {
					try {
						RemoteEndpoint.Basic remoteEndpoint =
							session.getBasicRemote();

						remoteEndpoint.sendText(text);
					}
					catch (IOException ioe) {
						throw new RuntimeException(ioe);
					}
				}

			});
	}

}