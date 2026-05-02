package scapp.apischuquiejdev.config.segurity;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;


@Component
public class ApiKeyInterceptor implements HandlerInterceptor {


    private final ApiKeyProperties props;

    public ApiKeyInterceptor(ApiKeyProperties props) {
        this.props = props;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!props.isEnabled()) {
            return true;
        }


        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        if (!props.isEnabled()) {
            return true;
        }



        String provided = request.getHeader(props.getHeaderName());
        String expected = props.getValue();

        if (provided == null || provided.trim().isEmpty()) {
            unauthorized(response, "Falta API Key.");
            return false;
        }

        if (expected == null || expected.trim().isEmpty()) {
            unauthorized(response, "Configuración API Key inválida en servidor.");
            return false;
        }

        if (!expected.equals(provided)) {
            unauthorized(response, "API Key inválida.");
            return false;
        }

        return true;
    }

    private void unauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        String body = "{\"status\":false,\"message\":\"" + message + "\"}";
        response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
    }
}

