package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests(authorize ->{
			authorize.antMatchers("/","/webjars/**","/login","/resources/**").permitAll();
			authorize.antMatchers("/beers/find","/beers*").permitAll();
			authorize.antMatchers(HttpMethod.GET,"/api/v1/beer/**").permitAll();
			authorize.mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
		})
		.authorizeRequests()
			.anyRequest().authenticated()
			.and()
		.formLogin().and()
		.httpBasic();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("admin")
		.password("admin")
		.roles("ADMIN")
		.and()
		.withUser("user")
		.password("$2a$10$7clbI5RSuRwSJcTFoMAk3e73NTAja35Br0JkrfiDhr/fA/VWcYiqK")
		.roles("USER")
		.and()
		.withUser("scott")
		.password("tiger")
		.roles("CUSTOMER");
	}

	
	/*
	 * @Override
	 * 
	 * @Bean protected UserDetailsService userDetailsService() { UserDetails admin =
	 * User .builder() .username("admin") .password("admin") .roles("ADMIN")
	 * .build(); UserDetails user = User .builder() .username("user")
	 * .password("password") .roles("USER") .build();
	 * 
	 * return new InMemoryUserDetailsManager(admin, user); }
	 * 
	 */
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		//return NoOpPasswordEncoder.getInstance();
		return new BCryptPasswordEncoder();
	}
	
	/*
	 *Using LDAP Password Encoder
	 *use encode method to encode password
	 *
	 * @Bean public PasswordEncoder passwordEncoder2() { return new
	 * LdapShaPasswordEncoder(); }
	 */
	
	
	/*
	 * Using SHA256 encryption
	 * use encode method to encode password
	 * 
	 * @Bean public PasswordEncoder passwordEncoder3() { return new
	 * StandardPasswordEncoder(); }
	 */
	

}
