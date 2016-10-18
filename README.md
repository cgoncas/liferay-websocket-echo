# Liferay Websocket Echo Portlet Example

## What is this?

This is an example of how to use the Liferay Websocket Whiteboard

This example will be executed in the following environment:

* Liferay Portal 7.0.0 GA3

## Creating a Liferay Porltet

### New MVC Portlet
We are going to use [Blade](https://dev.liferay.com/develop/tutorials/-/knowledge_base/7-0/blade-cli) to create a `Echo Portlet`:

```sh
blade create -t mvcportlet -p com.liferay.websocket.example.echo -c EchoPortlet echo-portlet
```

### Add websocket dependency
We need to add the dependencies in the [build file](build.gradle), as shown below:

```gradle
dependencies {
...
    compileOnly group: "javax.websocket", name: "javax.websocket-api", version: "1.1"
...
}
```

### Create a Echo WebSocket Endpoint
Now we can create a [endpoint](src/main/java/com/liferay/websocket/example/echo/endpoint/EchoWebSocketEndpoint.java) that returns to the client the same message that receives.

```java
public class EchoWebSocketEndpoint extends Endpoint {

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
```

To register this endpoint in the path `\o\echo` we will use declarative services as follow:

```java
@Component(
	immediate = true,
	property = {"org.osgi.http.websocket.endpoint.path=/o/echo"},
	service = Endpoint.class
)
```