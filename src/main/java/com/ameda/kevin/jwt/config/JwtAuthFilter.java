package com.ameda.kevin.jwt.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor // creates constructor with any final field that we provide
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader=request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            //if authHeader is null or authHeader doesn't start with Bearer string,then
            //pass request and response to the next filter
            filterChain.doFilter(request,response);
            return;
        }
        //extract token from authHeader now
        jwtToken=authHeader.substring(7);
        //extract userEmail from JWT token
        userEmail=jwtService.extractUsername(jwtToken);
        if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails=this.userDetailsService.loadUserByUsername(userEmail);
            if(jwtService.isTokenValid(jwtToken,userDetails)){
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);

    }
}
