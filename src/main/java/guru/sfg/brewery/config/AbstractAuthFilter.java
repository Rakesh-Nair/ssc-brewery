package guru.sfg.brewery.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractAuthFilter extends AbstractAuthenticationProcessingFilter{

	protected AbstractAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}
	
	protected abstract String getPassword(HttpServletRequest request);
	
	protected abstract String getUsername(HttpServletRequest request);
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (logger.isDebugEnabled()) {
			logger.debug("Request is to process authentication");
		}

		try {
			Authentication authResult = attemptAuthentication(request, response);
			if(authResult != null) {
				successfulAuthentication(request, response, chain, authResult);
			}
			else {
				chain.doFilter(request, response);
			}
		}
		catch(AuthenticationException e) {
			unsuccessfulAuthentication(request, response, e);
		}
			
		
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		SecurityContextHolder.clearContext();

		if (log.isDebugEnabled()) {
			log.debug("Authentication request failed: " + failed.toString(), failed);
			log.debug("Updated SecurityContextHolder to contain null Authentication");
		}
		
		response.sendError(HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.getReasonPhrase());
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain, Authentication authResult)
			throws IOException, ServletException {

		if (logger.isDebugEnabled()) {
			logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
					+ authResult);
		}

		SecurityContextHolder.getContext().setAuthentication(authResult);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		String username = getUsername(request);
		if(username == null)
			username = "";
		String password = getPassword(request);
		if(password == null)
			password = "";
		log.debug("Authenticating user "+username);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
		
		if(!StringUtils.isEmpty(username)) {
			return this.getAuthenticationManager().authenticate(token);
		}
		else {
			return null;
		}
		
		
	}
	

}
