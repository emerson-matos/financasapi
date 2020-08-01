package br.com.controle.financeiro.configuration.security;

import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseAuthenticationProvider;
import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseFilter;
import br.com.controle.financeiro.service.FirebaseService;
import br.com.controle.financeiro.service.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends GlobalMethodSecurityConfiguration {

    public static class Roles {
        public static final String ANONYMOUS = "ANONYMOUS";
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";

        private static final String ROLE = "ROLE_";
        public static final String ROLE_ANONYMOUS = ROLE + ANONYMOUS;
        public static final String ROLE_USER = ROLE + USER;
        public static final String ROLE_ADMIN = ROLE + ADMIN;

        private Roles() {

        }

    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Configuration
    protected static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

        @Value("${br.com.controle.financeiro.firebase.enabled}")
        private Boolean firebaseEnabled;

        private final UserDetailsService userService;

        private final FirebaseAuthenticationProvider firebaseProvider;

        public AuthenticationSecurity(@Qualifier(value = UserServiceImpl.NAME) UserDetailsService userService,
                                      FirebaseAuthenticationProvider firebaseProvider) {
            this.userService = userService;
            this.firebaseProvider = firebaseProvider;
        }

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            auth.userDetailsService(userService).passwordEncoder(encoder);
            if (Boolean.TRUE.equals(firebaseEnabled)) {
                auth.authenticationProvider(firebaseProvider);
            }
        }

    }

    @Configuration
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

        private static final String[] ADMIN_ENDPOINT = { "/admin/**", "/actuator/**" };
        private static final String[] OPEN_ENDPOINT = { "/**" };
        private static final String[] USER_ENDPOINT = { "/api/**" };

        @Value("${br.com.controle.financeiro.firebase.enabled}")
        private Boolean firebaseEnabled;

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring()
               .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security",
                            "/swagger-ui.html", "/webjars/**", "/v2/swagger.json");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            if (Boolean.TRUE.equals(firebaseEnabled)) {
                http.addFilterBefore(tokenAuthorizationFilter(), BasicAuthenticationFilter.class);
            }
            http.authorizeRequests()//
                .antMatchers(ADMIN_ENDPOINT).hasAnyRole(Roles.ADMIN)//
                .antMatchers(USER_ENDPOINT).hasRole(Roles.USER)//
                .antMatchers(OPEN_ENDPOINT).hasAnyRole(Roles.ANONYMOUS)//
                .and().csrf().disable()//
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
                .and().anonymous().authorities(Roles.ROLE_ANONYMOUS);//
        }

        @Autowired(required = false)
        private FirebaseService firebaseService;

        private FirebaseFilter tokenAuthorizationFilter() {
            return new FirebaseFilter(firebaseService);
        }

    }

}