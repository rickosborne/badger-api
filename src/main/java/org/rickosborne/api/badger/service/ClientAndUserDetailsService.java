package org.rickosborne.api.badger.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;

public class ClientAndUserDetailsService implements UserDetailsService, ClientDetailsService {

    private final ClientDetailsService clientDetails;
    private final UserDetailsService userDetails;
    private final ClientDetailsUserDetailsService bothDetails;

    public ClientAndUserDetailsService(ClientDetailsService clients, UserDetailsService users) {
        super();
        clientDetails = clients;
        userDetails = users;
        bothDetails = new ClientDetailsUserDetailsService(clients);
    }

    @Override
    public ClientDetails loadClientByClientId (String clientId) throws ClientRegistrationException {
        return clientDetails.loadClientByClientId(clientId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = null;
        try {
            user = userDetails.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            user = bothDetails.loadUserByUsername(username);
        }
        return user;
    }
}
