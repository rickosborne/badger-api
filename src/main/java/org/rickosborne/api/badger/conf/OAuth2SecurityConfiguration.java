package org.rickosborne.api.badger.conf;

import org.rickosborne.api.badger.data.User;
import org.rickosborne.api.badger.service.ClientAndUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;


@Configuration
public class OAuth2SecurityConfiguration {

    @Configuration
    @EnableResourceServer
    protected static class ResourceServer extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.authorizeRequests().antMatchers("/oauth/token").anonymous();
            http.authorizeRequests().antMatchers(HttpMethod.GET, "/**").access("#oauth2.hasScope('read')");
            http.authorizeRequests().antMatchers("/**").access("#oauth2.hasScope('write')");
        }

    }

    @Configuration
    @EnableAuthorizationServer
    @Order(Ordered.LOWEST_PRECEDENCE - 100)
    protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        private ClientAndUserDetailsService detailsService;

        public OAuth2Config() throws Exception {
            ClientDetailsService clientDetails = new InMemoryClientDetailsServiceBuilder()
                .withClient("admin")
                    .authorizedGrantTypes("password")
                    .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                    .scopes("read", "write")
                    .resourceIds("api")
                .and()
                .withClient("user")
                    .authorizedGrantTypes("password")
                    .authorities("ROLECLIENT")
                    .scopes("read")
                    .resourceIds("api")
                    .accessTokenValiditySeconds(3600)
                .and()
                .build();
            UserDetailsService userDetails = new InMemoryUserDetailsManager(
                Arrays.<UserDetails>asList(
                    User.create("admin", "password", "ADMIN", "USER"),
                    User.create("user", "password", "USER")
                )
            );
            detailsService = new ClientAndUserDetailsService(clientDetails, userDetails);
        }

        @Bean public ClientDetailsService clientDetailsService() throws Exception { return detailsService; }
        @Bean public UserDetailsService userDetailsService() throws Exception { return detailsService; }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(clientDetailsService());
        }

    }

}
