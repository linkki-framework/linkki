# Playground

Spring Boot application for testing/experimenting with linkki stuff.

## Run
Start the application under `src/main/java/org/linkki/samples/playground/PlaygroundApplication.java`. Once up and running, it will be available under [here](http://localhost:8080/linkki-sample-test-playground-vaadin-flow).

## Troubleshooting
If you get the following error:

```
com.vaadin.flow.server.ExecutionFailedException: Discovered @Theme annotation with theme name 'linkki', but could not find the theme directory in the project or available as a jar dependency. ...
```

Delete the directory `node_modules` and rebuild the linkki project with the "Production" profile (-Pproduction).
