<%@ page
		import="com.liferay.websocket.example.echo.endpoint.EchoWebSocketEndpoint" %>

<%@ include file="/init.jsp" %>

<div id="content">
	<div id="left_col">
		<aui:input label="url" name="urlInputText" readonly="true" type="text" value='<%= "ws://" + request.getServerName() + ":" + request.getServerPort() + EchoWebSocketEndpoint.ECHO_WEBSOCKET_PATH %>' />
		</br>

		<aui:button name="connect" onClick="initWebSocket();" value="connect" />
		<aui:button name="disconnect" onClick="stopWebSocket();" value="disconnect" />
		<aui:button name="state" onClick="checkSocket();" value="state" />
		<br />
		<aui:input name="message" onkeydown="ifevent(.keyCode==13)sendMessage();" type="text" />
		<aui:button name="send" onClick="sendMessage();" value="send" />
	</div>

	<div id="right_col">
		<aui:input name="debugTextArea" readonly="true" type="textarea" />
	</div>
</div>

<script type="text/javascript">
	var debugTextArea = document.getElementById('<portlet:namespace />debugTextArea');

	var wsUri = document.getElementById('<portlet:namespace />urlInputText').value;

	function debug(message) {
		debugTextArea.value += message + "\n\n";
		debugTextArea.scrollTop = debugTextArea.scrollHeight;
	}

	function sendMessage() {
		var msg = document.getElementById('<portlet:namespace />message').value;
		if ( websocket != null ) {
			document.getElementById('<portlet:namespace />message').value = "";
			websocket.send(msg);
			debug("Message sent: " + msg);
		}
		else {
			debug("Can't sent message, the connection is not open");
		}
	}

	var websocket = null;

	function initWebSocket() {
		try {
			if (typeof MozWebSocket == 'function')
				WebSocket = MozWebSocket;
			if ( websocket && websocket.readyState == 1 )
				websocket.close();
			websocket = new WebSocket( wsUri );
			websocket.onopen = function(evt) {
				debug("CONNECTED");
			};
			websocket.onclose = function(evt) {
				debug("DISCONNECTED");
			};
			websocket.onmessage = function(evt) {
				debug( "Message received: " + evt.data );
			};
			websocket.onerror = function(evt) {
				debug('ERROR: ' + evt.data);
			};
		}
		catch (exception) {
			debug('ERROR: ' + exception);
		}
	}

	function stopWebSocket() {
		if (websocket) {
			websocket.close();
		}
	}

	function checkSocket() {
		if (websocket != null) {
			var stateStr;
			switch (websocket.readyState) {
				case 0: {
					stateStr = "CONNECTING";
					break;
				}
				case 1: {
					stateStr = "OPEN";
					break;
				}
				case 2: {
					stateStr = "CLOSING";
					break;
				}
				case 3: {
					stateStr = "CLOSED";
					break;
				}
				default: {
					stateStr = "UNKNOW";
					break;
				}
			}
			debug("WebSocket state = " + websocket.readyState + " ( " + stateStr + " )");
		} else {
			debug("WebSocket is null");
		}
	}
</script>