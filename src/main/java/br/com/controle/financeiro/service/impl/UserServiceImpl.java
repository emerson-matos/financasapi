package br.com.controle.financeiro.service.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import br.com.controle.financeiro.configuration.SecurityConfig.Roles;
import br.com.controle.financeiro.model.entity.Role;
import br.com.controle.financeiro.model.entity.UserEntity;
import br.com.controle.financeiro.model.repository.RoleRepository;
import br.com.controle.financeiro.model.repository.UserRepository;
import br.com.controle.financeiro.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service(value = UserServiceImpl.NAME)
public class UserServiceImpl implements UserService {

    public static final String NAME = "UserService";
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userDao;

    @Autowired
    private RoleRepository roleRepository;

    public UserDetails loadUserByUsername(String username) {
        Optional<UserEntity> user = userDao.findByUsername(username);
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

        Optional<UserEntity> userLoaded = userDao.findByUsername(init.getUserName());

        if (userLoaded.isEmpty()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(init.getUserName());
            userEntity.setEmail(init.getEmail());

            userEntity.setAuthorities(getUserRoles());
            // TODO firebase users should not be able to login via username and
            // password so for now generation of password is OK
            userEntity.setPassword(UUID.randomUUID().toString());
            userDao.save(userEntity);
            logger.info("registerUser -> user created");
            return userEntity;
        } else {
            logger.info("registerUser -> user exists");
            return userLoaded.get();
        }
    }

    @PostConstruct
    public void init() {

        if (userDao.count() == 0) {
            UserEntity adminEntity = new UserEntity();
            adminEntity.setUsername("admin");
            adminEntity.setPassword(new BCryptPasswordEncoder().encode("admin"));
            adminEntity.setEmail("some@one.com");

            adminEntity.setAuthorities(getAdminRoles());
            userDao.save(adminEntity);

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername("user1");
            userEntity.setPassword(new BCryptPasswordEncoder().encode("user1"));
            userEntity.setEmail("some@one.com");
            userEntity.setAuthorities(getUserRoles());

            userDao.save(userEntity);
        }
    }

    private List<Role> getAdminRoles() {
        return Collections.singletonList(getRole(Roles.ROLE_ADMIN));
    }

    private List<Role> getUserRoles() {
        return Collections.singletonList(getRole(Roles.ROLE_USER));
    }

    private Role getRole(String authority) {
        Role adminRole = roleRepository.findByAuthority(authority);
        return Objects.requireNonNullElseGet(adminRole, () -> new Role(authority));
    }

}
