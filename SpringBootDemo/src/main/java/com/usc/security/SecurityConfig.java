package com.usc.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.usc.handler.AccessDeniedHandlerImpl;
import com.usc.handler.AuthenticationEntryPointImpl;
import com.usc.handler.AuthenticationFailureHandlerImpl;
import com.usc.handler.AuthenticationSuccessHandlerImpl;
import com.usc.handler.LogoutSuccessHandlerImpl;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AuthenticationEntryPointImpl authenticationEntryPointImpl;

    @Autowired
    AccessDeniedHandlerImpl accessDeniedHandlerimpl;

    @Autowired
    AuthenticationSuccessHandlerImpl authenticationSuccessHandlerIml;

    @Autowired
    AuthenticationFailureHandlerImpl authenticationFailureHandlerImpl;

    @Autowired
    LogoutSuccessHandlerImpl logoutSuccesshandlerImpl;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors()//csrf: 外部攻击
        .and().authorizeRequests().antMatchers("/index.html", "/products", "products/*").permitAll().and()
        .exceptionHandling().authenticationEntryPoint(authenticationEntryPointImpl).and().exceptionHandling()
        .accessDeniedHandler(accessDeniedHandlerimpl).and().formLogin().permitAll().loginProcessingUrl("/login")  //formLogin实现验证是否正确
        .successHandler(authenticationSuccessHandlerIml).failureHandler(authenticationFailureHandlerImpl)
        .usernameParameter("username").passwordParameter("password").and().logout().permitAll()
        .logoutUrl("/logout").logoutSuccessHandler(logoutSuccesshandlerImpl).and().rememberMe();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200");

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder(11);
        return encoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

}