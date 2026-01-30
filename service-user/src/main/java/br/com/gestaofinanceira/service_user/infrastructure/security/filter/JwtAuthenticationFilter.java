package br.com.gestaofinanceira.service_user.infrastructure.security.filter;

import br.com.gestaofinanceira.service_user.application.port.TokenVerifier;

import br.com.gestaofinanceira.service_user.infrastructure.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenVerifier tokenVerifier;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(
            TokenVerifier tokenVerifier,
            UserDetailsServiceImpl userDetailsService
    ) {
        this.tokenVerifier = tokenVerifier;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null && tokenVerifier.isValid(token)) {

            tokenVerifier.extractUserId(token)
                    .map(userDetailsService::loadUserByPublicId)
                    .ifPresent(userDetails -> {
                        var auth = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    });
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth",
            "/auth/login"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PUBLIC_PATHS.stream()
                .anyMatch(p -> request.getServletPath().startsWith(p));
    }

}
