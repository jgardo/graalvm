package dev.jgardo.graalvm.playground;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/generator")
public class GenerateController {

    public record GenerateRequest(
        @JsonProperty("templateName") String templateName,
        @JsonProperty("params") JsonNode params
    ) {}

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String generate(GenerateRequest request) {
        return "The templateName is %s".formatted(request.templateName);
    }
}
