package es.molabs.boapi.infrastructure.configuration;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorMapping extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(request, includeStackTrace);
        Throwable exception = getError(request);

        if (exception instanceof IllegalArgumentException) {
            map.put("status", HttpStatus.BAD_REQUEST.value());
            map.put("message", exception.getMessage());
            map.remove("error");
        }

        return map;
    }
}
