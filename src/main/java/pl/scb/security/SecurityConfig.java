package pl.scb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableWebSecurity
public class SecurityConfig {
    private final String[] allowedResources = {"/sciborowka-auth/**","/sciborowka-client/**","/images/**/**/**","/budowlanka-client/**"};
    @Autowired
    private JwtFilter filter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c->{})
                .authorizeRequests()
                .antMatchers(this.allowedResources).permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/sciborowka-admin/**","/sciborowka-admin/**/**").hasAuthority("SCIBOROWKA")
                .and()
                .authorizeRequests()
                .antMatchers("/budowlanka-admin/**", "/budowlanka-admin/**/**").hasAuthority("BUDOWLANKA")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic();
        return http.build();
   }
}