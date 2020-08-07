package br.com.controle.financeiro.service.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import br.com.controle.financeiro.configuration.security.SecurityConfig.Roles;
import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseAuthenticationToken;
import br.com.controle.financeiro.model.entity.Role;
import br.com.controle.financeiro.model.entity.UserEntity;
import br.com.controle.financeiro.model.repository.RoleRepository;
import br.com.controle.financeiro.model.repository.UserRepository;
import br.com.controle.financeiro.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = UserServiceImpl.NAME)
public class UserServiceImpl implements UserService {

    public static final String NAME = "UserService";
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserDetails loadUserByUsername(String username) {
        Optional<UserEntity> user = userRepository.findById(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Bad credentials");
        }

        UserEntity userDetails = user.get();

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (GrantedAuthority role : userDetails.getAuthorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        return new User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    @Transactional
    @Secured(value = Roles.ROLE_ANONYMOUS)
    public UserEntity registerUser(RegisterUserInit init) {

        Optional<UserEntity> userLoaded = userRepository.findById(init.getUserName());

        if (userLoaded.isEmpty()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setName(init.getName());
            userEntity.setEmail(init.getEmail());
            userEntity.setId(init.getUserName());
            userEntity.setAuthorities(getUserRoles());
            // TODO firebase users should not be able to login via username and
            // password so for now generation of password is OK
            userEntity.setPassword(UUID.randomUUID().toString());
            userRepository.save(userEntity);
            logger.info("registerUser -> user created");
            return userEntity;
        } else {
            logger.info("registerUser -> user exists");
            return userLoaded.get();
        }
    }

    @Override
    public UserEntity getAuthenticatedUser() {
        String user;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof FirebaseAuthenticationToken) {
            user = String.valueOf(((UserDetails) auth.getPrincipal()).getUsername());
            return userRepository.findById(user).orElseThrow();
        }
        throw new UsernameNotFoundException("Bad credentials");
    }

    private List<Role> getUserRoles() {
        return Collections.singletonList(getRole(Roles.ROLE_USER));
    }

    private Role getRole(String authority) {
        Role adminRole = roleRepository.findByAuthority(authority);
        return Objects.requireNonNullElseGet(adminRole, () -> new Role(authority));
    }

}
