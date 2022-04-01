package org.acme.getting.started;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;

@Path("/hello")
public class GreetingResource {

    @Inject
    GreetingService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/greeting/{name}")
    public String greeting(@PathParam String name) {
        try (DaprClient client = new DaprClientBuilder().build()) {
            System.out.println("Waiting for Dapr sidecar ...");
            client.waitForSidecar(10000).block();
            System.out.println("Dapr sidecar is ready.");
    
            String message = name;
      
            client.saveState("java-native-test", "firstkey", "abc".getBytes()).block();
            System.out.println("Saving class with message: " + message);
            client.deleteState("java-native-test", "firstkey", "100", null).block();
          } catch (Exception ex) {            
              System.out.println("Unexpected exception.");
          }
          
          return service.greeting(name);

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }
}