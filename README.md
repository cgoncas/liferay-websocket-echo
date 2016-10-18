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