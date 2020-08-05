package br.com.controle.financeiro.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import br.com.controle.financeiro.configuration.security.SecurityConfig;
import br.com.controle.financeiro.model.entity.Role;
import br.com.controle.financeiro.model.entity.UserEntity;
import br.com.controle.financeiro.model.repository.RoleRepository;
import br.com.controle.financeiro.model.repository.UserRepository;
import br.com.controle.financeiro.service.UserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository userDao;

    @Mock
    private RoleRepository roleRepository;

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameNotFound() {
        when(userDao.findById(anyString())).thenReturn(Optional.empty());

        service.loadUserByUsername("someone");
    }

    @Test
    public void loadUserByUsernameSucess() {
        UserEntity entity = mock(UserEntity.class);
        Collection<Role> rolesList = new ArrayList<>();

        Collections.addAll(rolesList, new Role("user"), new Role("user"));

        doReturn(Optional.of(entity)).when(userDao).findById(anyString());
        doReturn("someone").when(entity).getUsername();
        doReturn("123456").when(entity).getPassword();
        doReturn(rolesList).when(entity).getAuthorities();

        UserDetails user = service.loadUserByUsername("someone");

        assertEquals("someone", user.getUsername());
        assertEquals("123456", user.getPassword());
        assertNotNull(user.getAuthorities());
    }

    @Test
    public void registerUserAlredyRegistered() {
        UserEntity entity = mock(UserEntity.class);

        doReturn(Optional.of(entity)).when(userDao).findById(anyString());

        assertEquals(entity, service.registerUser(new UserService.RegisterUserInit("someone", "some@one.tk", "id")));
    }

    @Test
    public void registerUserWithSucess() {

        doReturn(Optional.empty()).when(userDao).findById(anyString());
        UserEntity entity = service.registerUser(new UserService.RegisterUserInit("someone", "some@one.tk", "id"));
        assertEquals("someone", entity.getUsername());
        assertEquals("some@one.tk", entity.getEmail());
        assertEquals(1, entity.getAuthorities().size());
        assertTrue(entity.getAuthorities().stream()
                         .anyMatch(x -> x.getAuthority().equals(SecurityConfig.Roles.ROLE_USER)));
    }

}