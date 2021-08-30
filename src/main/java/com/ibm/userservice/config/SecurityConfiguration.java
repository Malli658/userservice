package com.ibm.userservice.config;

import static java.lang.String.format;





import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.ibm.userservice.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	
	private  Logger logger= LoggerFactory.getLogger(SecurityConfiguration.class);
	@Autowired
    private  UserRepository userRepo;
	@Autowired
    private  JwtTokenFilter jwtTokenFilter;
	
	@Value("${springdoc.api-docs.path}")
    private String restApiDocPath;
    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;
	
	public SecurityConfiguration() {
		// TODO Auto-generated constructor stub
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
	}
	
	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    	auth.userDetailsService(userName->userRepo.findByUsername(userName).orElseThrow(()-> new UsernameNotFoundException(format("User: %s, not found", userName))));
	    }
	    
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	    /*@Override
	    public void configure(WebSecurity web) throws Exception {
	    	logger.info("SecurityConfiguration.configure()");
	      web
	        .ignoring()
	           .antMatchers("/api/public/**");
	      web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");// #3
	    }*/
	    
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        // Enable CORS and disable CSRF
	        http = http.cors().and().csrf().disable();

	        // Set session management to stateless
	        http = http
	                .sessionManagement()
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	                .and();

	        // Set unauthorized requests exception handler
	        http = http
	                .exceptionHandling()
	                .authenticationEntryPoint(
	                        (request, response, ex) -> {
	                            logger.error("Unauthorized request - {}", ex.getMessage());
	                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
	                        }
	                )
	                .and();

	        // Set permissions on endpoints
	        http.authorizeRequests()
	                // Swagger endpoints must be publicly accessible
	                 .antMatchers("/api/public/**").permitAll()
	                .antMatchers(format("%s/**", swaggerPath)).permitAll()
	                // Our private endpoints
	                .anyRequest().authenticated();

	        // Add JWT token filter
	        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
	    }
	    
	    /*@Override
	    public void configure(WebSecurity web) throws Exception {
	        web.ignoring().antMatchers("/api/public/**");
	    }*/
	    
	 // Used by spring security if CORS is enabled.
	    /*@Bean
	    public CorsFilter corsFilter() {
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        CorsConfiguration config = new CorsConfiguration();
	        config.setAllowCredentials(true);
	        config.addAllowedOrigin("*");
	        config.addAllowedHeader("*");
	        config.addAllowedMethod("*");
	        source.registerCorsConfiguration("/**", config);
	        return new CorsFilter(source);
	    }*/

	    // Expose authentication manager bean
	    @Override @Bean
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }
}
