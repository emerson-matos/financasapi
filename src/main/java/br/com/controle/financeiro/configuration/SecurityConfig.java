package br.com.controle.financeiro.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import br.com.controle.financeiro.configuration.auth.firebase.FirebaseAuthenticationProvider;
import br.com.controle.financeiro.configuration.auth.firebase.FirebaseFilter;
import br.com.controle.financeiro.service.FirebaseService;
import br.com.controle.financeiro.service.impl.UserServiceImpl;

@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	public static class Roles {
		public static final String ANONYMOUS = "ANONYMOUS";
		public static final String USER = "USER";
		public static final String ADMIN = "ADMIN";

		private static final String ROLE = "ROLE_";
		public static final String ROLE_ANONYMOUS = ROLE + ANONYMOUS;
		public static final String ROLE_USER = ROLE + USER;
		public static final String ROLE_ADMIN = ROLE + ADMIN;
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Configuration
	protected static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

		@Autowired
		@Qualifier(value = UserServiceImpl.NAME)
		private UserDetailsService userService;

		@Value("${br.com.controle.financeiro.firebase.enabled}")
		private Boolean firebaseEnabled;

		@Autowired
		private FirebaseAuthenticationProvider firebaseProvider;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userService);
			if (Boolean.TRUE.equals(firebaseEnabled)) {
				auth.authenticationProvider(firebaseProvider);
			}
		}
	}

	@Configuration
	protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

		private static final String[] ADMIN_ENDPOINT = { "/admin/**", "/health/**" };
		private static final String[] OPEN_ENDPOINT = { "/open/**", "/signup/**" };
		private static final String[] USER_ENDPOINT = { "/transaction/**", "/bankaccount/**", "/card/**", "/client/**",
				"/institution/**" };

		@Value("${br.com.controle.financeiro.firebase.enabled}")
		private Boolean firebaseEnabled;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			if (Boolean.TRUE.equals(firebaseEnabled)) {
				http.addFilterBefore(tokenAuthorizationFilter(), BasicAuthenticationFilter.class).authorizeRequests()//
						.antMatchers(OPEN_ENDPOINT).hasAnyRole(Roles.ANONYMOUS)//
						.antMatchers(USER_ENDPOINT).hasRole(Roles.USER)
						.antMatchers(ADMIN_ENDPOINT).hasAnyRole(Roles.ADMIN)//
						.and().csrf().disable()//
						.anonymous().authorities(Roles.ROLE_ANONYMOUS);//
			} else {
				http.httpBasic().and().authorizeRequests()//

						// .antMatchers(OPEN_ENDPOINT).hasAnyRole(Roles.ANONYMOUS)//
						// .antMatchers(USER_ENDPOINT).hasRole(Roles.USER)//
						// .antMatchers(ADMIN_ENDPOINT).hasAnyRole(Roles.ADMIN)//
						.antMatchers("/**").permitAll()//
						.and().csrf().disable()//
						.anonymous().authorities(Roles.ROLE_ANONYMOUS);//
			}
		}

		@Autowired(required = false)
		private FirebaseService firebaseService;

		private FirebaseFilter tokenAuthorizationFilter() {
			return new FirebaseFilter(firebaseService);
		}

	}
}