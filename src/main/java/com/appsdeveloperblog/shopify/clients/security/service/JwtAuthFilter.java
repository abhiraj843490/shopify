package com.appsdeveloperblog.shopify.clients.security.service;

import com.appsdeveloperblog.shopify.clients.config.ShopifyDetailsService;
import com.appsdeveloperblog.shopify.clients.config.ShopifyUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@WebFilter( filterName = "mdcFilter", urlPatterns = { "/*" } )
public class JwtAuthFilter  extends OncePerRequestFilter{
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ShopifyDetailsService shopifyDetailsService;
    private static final String USER = "user";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //1. Header
        String authHeader = request.getHeader("Authorization");
        //2
        String token = null; String username = null;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }
        //3
        if(username!=null && SecurityContextHolder.getContext()
                .getAuthentication()==null){
            UserDetails userDetails = shopifyDetailsService.loadUserByUsername(username);

            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails
                                , null
                                ,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                //MDC configure
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                ShopifyUserDetails user = (ShopifyUserDetails) authentication.getPrincipal();
                MDC.put(USER, user.getUsername());
            }
        }
        filterChain.doFilter(request,response);
        MDC.clear();
    }
}
