package guru.sfg.brewery.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

public class RestUrlAuthFilter extends AbstractAuthFilter {

	protected RestUrlAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	@Override
	protected String getPassword(HttpServletRequest request) {
		return request.getParameter("apiKey");
	}

	@Override
	protected String getUsername(HttpServletRequest request) {
		return request.getParameter("apiSecret");
	}

}
