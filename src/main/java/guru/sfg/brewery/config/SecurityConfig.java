package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import guru.sfg.brewery.SfgPasswordEncoderFactories;

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
		.password("{sha256}604375dae89ad7ba4d044c8060bff66c6092da2ebc13caeccc518e55cc4cd44a7d109eb7beaa11f3")
		.roles("ADMIN")
		.and()
		.withUser("user")
		.password("{bcrypt}$2a$10$7clbI5RSuRwSJcTFoMAk3e73NTAja35Br0JkrfiDhr/fA/VWcYiqK")
		.roles("USER")
		.and()
		.withUser("scott")
		.password("{ldap}{SSHA}EHE55kHGh/we3BBlJvpIvEH7sapTEet6oqJGlA==")
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
		//return new BCryptPasswordEncoder();
		return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
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
