package com.digiqual.filter;

import com.digiqual.security.JwtTokenService;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
public class JwtRouteGuardFilter implements GlobalFilter, Ordered {

    private final JwtTokenService jwtTokenService;

    public JwtRouteGuardFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        if (!path.startsWith("/api/") || path.startsWith("/api/auth/") || path.startsWith("/api/student/public/")) {
            return chain.filter(exchange);
        }

        String requiredRole = requiredRoleForPath(path);
        if (requiredRole == null) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        if (!jwtTokenService.isValid(token)) {
            return unauthorized(exchange, "Invalid token");
        }

        Claims claims = jwtTokenService.parseClaims(token);
        String tokenRole = String.valueOf(claims.get("role", String.class)).toUpperCase(Locale.ROOT);
        if (!requiredRole.equals(tokenRole)) {
            return forbidden(exchange, "Insufficient role for this route");
        }

        String email = claims.getSubject();
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Email", email == null ? "" : email)
                .header("X-User-Role", tokenRole)
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 20;
    }

    private String requiredRoleForPath(String path) {
        if (path.startsWith("/api/admin/")) {
            return "ADMIN";
        }
        if (path.startsWith("/api/partner/")) {
            return "PARTNER";
        }
        if (path.startsWith("/api/student/")) {
            return "STUDENT";
        }
        return null;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        return writeError(exchange, HttpStatus.UNAUTHORIZED, message);
    }

    private Mono<Void> forbidden(ServerWebExchange exchange, String message) {
        return writeError(exchange, HttpStatus.FORBIDDEN, message);
    }

    private Mono<Void> writeError(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String json = "{\"success\":false,\"message\":\"" + message + "\"}";
        byte[] body = json.getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory()
                .wrap(body)));
    }
}
