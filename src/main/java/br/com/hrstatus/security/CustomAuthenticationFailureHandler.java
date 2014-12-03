package br.com.hrstatus.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler extends
		SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		super.onAuthenticationFailure(request, response, exception);
		String defaultFailureUrl = "/login.jsf";
		if (exception.getClass().isAssignableFrom(
				UsernameNotFoundException.class)) {
			response.addHeader("BAD_CREDENTIAL", defaultFailureUrl);
		} else if (exception.getClass().isAssignableFrom(
				DisabledException.class)) {
			response.addHeader("BAD_CREDENTIAL", defaultFailureUrl);
		}
	}
}