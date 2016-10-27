/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
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

package com.liferay.websocket.example.echo.endpoint.test;

import com.google.common.io.Files;
import com.liferay.arquillian.portal.annotation.PortalURL;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Cristina González
 */
@RunWith(Arquillian.class)
public class EchoWebsocketFuntionalTest {

	@Deployment
	public static JavaArchive create() {
		File tempDir = Files.createTempDir();

		try {
			ProcessBuilder processBuilder = new ProcessBuilder(
				"./gradlew", "jar", "-Pdir=" + tempDir.getAbsolutePath());

			Process process = processBuilder.start();

			process.waitFor();

			BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));

			String line = bufferedReader.readLine();

			while (line != null) {
				System.out.println(line);

				line = bufferedReader.readLine();
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}

		File jarFile = new File(
			tempDir.getAbsolutePath() +
				"/com.liferay.websocket.example.echo-1.0.0.jar");

		if (!jarFile.exists()) {
			try {
				jarFile.createNewFile();
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		return ShrinkWrap.createFromZipFile(JavaArchive.class, jarFile);
	}

	@RunAsClient
	@Test
	public void testConnect() {
		_browser.get(_portlerURL.toExternalForm());

		_connect.click();

		try {
			Thread.sleep(10000);
		}
		catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		String logValue = _log.getAttribute("value");

		Assert.assertEquals("CONNECTED", lastLine(logValue));
	}

	@RunAsClient
	@Test
	public void testDisconnect() {
		_browser.get(_portlerURL.toExternalForm());

		_connect.click();

		_disconnect.click();

		try {
			Thread.sleep(10000);
		}
		catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		String logValue = _log.getAttribute("value");

		Assert.assertEquals("DISCONNECTED", lastLine(logValue));
	}

	@RunAsClient
	@Test
	public void testSendMessage() {
		_browser.get(_portlerURL.toExternalForm());

		_connect.click();

		_message.clear();

		_message.sendKeys("ECHO");

		_send.click();

		try {
			Thread.sleep(10000);
		}
		catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		String logValue = _log.getAttribute("value");

		Assert.assertEquals("Message received: ECHO", lastLine(logValue));
	}

	private String lastLine(String paragraph) {
		if (paragraph == null) {
			return "";
		}

		String[] lines = paragraph.split("\n");

		return lines[lines.length - 1];
	}

	@Drone
	private WebDriver _browser;

	@FindBy(css = "button[id$=connect]")
	private WebElement _connect;

	@FindBy(css = "button[id$=disconnect]")
	private WebElement _disconnect;

	@FindBy(css = "textarea[id$=debugTextArea]")
	private WebElement _log;

	@FindBy(css = "input[id$='message']")
	private WebElement _message;

	@PortalURL("echo_portlet")
	private URL _portlerURL;

	@FindBy(css = "button[id$=send]")
	private WebElement _send;

}