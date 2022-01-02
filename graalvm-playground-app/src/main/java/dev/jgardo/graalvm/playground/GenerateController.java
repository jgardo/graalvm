package dev.jgardo.graalvm.playground;

import com.fasterxml.jackson.databind.JsonNode;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URL;

@Path("/api/generator")
public class GenerateController {

    private final URL url;

    public GenerateController() {
        this.url = Thread.currentThread().getContextClassLoader().getResource("javascript/example.js");
    }

    public record GenerateRequest(
        String templateName,
        JsonNode params
    ) {}

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String generate(GenerateRequest request) {
        final var upperCasedTemplateName = invokeJs(request);
        return "The templateName is %s".formatted(upperCasedTemplateName);
    }

    private String invokeJs(GenerateRequest request) {
        final var context = Context.newBuilder("js")
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(className -> true)
                .build();

        final var src = Source.newBuilder("js", url).buildLiteral();
        final var evaluated = context.eval(src);
        final var upperCasedTemplateName = evaluated.execute(request.templateName());
        return upperCasedTemplateName.asString();
    }
}
