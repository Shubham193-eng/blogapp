package com.myblog.blogapp.security;

//import antlr.StringUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.util.StringUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//validation of token is done in this class
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

 @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //gwt JWT(token) from http request
        String token=getJWTfromRequest(request);

        //validation token

            if(StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                //get username from token
                String username = tokenProvider.getUsernameFromJWT(token);

                //load user associated with token
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //set spring security
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request,response);
        }

    
     //Bearer<accessToken>
    private String getJWTfromRequest(HttpServletRequest request) {
        String bearerToken=request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken))
            if (bearerToken.startsWith("Bearer")) {
                return bearerToken.substring(7, bearerToken.length());
            }
        return null;
    }
}
