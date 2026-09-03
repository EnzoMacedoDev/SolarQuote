package br.com.solarquote.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String metodo = request.getMethod();
        String uri = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");
        System.out.println("[JWT DEBUG] " + metodo + " " + uri + " | Header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            boolean valido = jwtService.tokenValido(token);
            System.out.println("[JWT DEBUG] Token valido? " + valido);

            if (valido) {
                String username = jwtService.extrairUsername(token);
                System.out.println("[JWT DEBUG] Username extraido: " + username);

                var authentication = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            System.out.println("[JWT DEBUG] Nenhum header Authorization Bearer encontrado.");
        }

        filterChain.doFilter(request, response);
    }
}