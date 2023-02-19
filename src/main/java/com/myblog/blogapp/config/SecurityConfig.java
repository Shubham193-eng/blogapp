package com.myblog.blogapp.config;

import com.myblog.blogapp.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private CustomUserDetailsService userDetailsService;// loadByUserName()
    @Bean//this annotation is used to perform method based dependencies as well as this obj is managed by spring boot
    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //Basic authentication (auth)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //hcd4ah formula
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/**").permitAll()//this helps us to gives the permit for all
                //users as well as admin.
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
    //passed userDetailsService class obj and password encoder for authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
////Inmemory authentication,In which we store the username as well as password temporarly.
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//
//        UserDetails pankaj = User.builder().username("pankaj").password(passwordEncoder().encode("1234")).roles("USER").build();
//        UserDetails admin = User.builder().username("admin").password(passwordEncoder().encode("Admin")).roles("ADMIN").build();
//
//        return new InMemoryUserDetailsManager(pankaj,admin);
//    }
}
