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
import java.util.List;

@Path("/api/generator")
public class GenerateController {


    public GenerateController() {
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
//
//
//
//
//        private val indexHtml by lazy {
//            HtmlController::class.java.getResource("/reactapp/index.html").readText()
//                    .replace("<script", "<script defer=\"defer\"")
//        }
//        private val runtimeMainJs by lazy {
//            HtmlController::class.java.getResource("/reactapp/js/runtime-main.js").readText()
//        }
//        private val mainJs by lazy {
//            HtmlController::class.java.getResource("/reactapp/js/main.chunk.js").readText()
//        }
//        private val secondJs by lazy {
//            HtmlController::class.java.getResource("/reactapp/js/2.chunk.js").readText()
//        }
//        private val initJs by lazy(::readInitJs)
//        private val renderJs by lazy(::readRenderJs)
//        private val engine by lazy(::initializeEngine)
//
//        @Autowired
//                constructor(serverApi: ServerApi) {
//            this.serverApi = serverApi
//        }
//
//        @GetMapping("/", "/r/**")
//        @ResponseBody
//        fun blog(request: HttpServletRequest): String {
//            engine.eval("window.requestUrl = '"+request.requestURI+"'")
//            val html = engine.eval(renderJs)
//            return indexHtml.replace("<div id=\"root\"></div>", "<div id=\"root\">$html</div>")
//        }
//
//        private fun readInitJs(): String {
//            val startIndex = indexHtml.indexOf("<script defer=\"defer\">")+"<script>".length
//            val endIndex = indexHtml.indexOf("</script>", startIndex)
//
//            return indexHtml.substring(startIndex, endIndex)
//        }
//
//        private fun readRenderJs(): String {
//            val startIndex = indexHtml.indexOf("<script defer=\"defer\" type=\"module\">")+"<script defer=\"defer\" type=\"module\">".length
//            val endIndex = indexHtml.indexOf("</script>", startIndex)
//
//            return indexHtml.substring(startIndex, endIndex)
//        }
//
//        private fun initializeEngine(): GraalJSScriptEngine {
//            val sw = StopWatch()
//            sw.start()
//            engine.put("api", serverApi)

        context.eval("js", "window = { location: { hostname: 'localhost' } }");
        context.eval("js", "navigator = {}");
        context.eval("js", "self = { navigator }");
        final var paths = List.of(
                "javascript/react.production.min.js",
                "javascript/react-dom.production.min.js",
                "javascript/react-dom-server.browser.production.min.js"
                );

        for (var path: paths) {
            final var url = Thread.currentThread().getContextClassLoader().getResource(path);

            context.eval(Source.newBuilder("js", url).buildLiteral());
        }

        context.eval("js", "window.isServer = true");

        final var url = Thread.currentThread().getContextClassLoader().getResource("javascript/example.js");
        final var src = Source.newBuilder("js", url).buildLiteral();
        final var evaluated = context.eval(src);
        final var upperCasedTemplateName = evaluated.execute(request.templateName());
        return upperCasedTemplateName.asString();
    }
}
