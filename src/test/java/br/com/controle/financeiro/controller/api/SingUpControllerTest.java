package br.com.controle.financeiro.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import br.com.controle.financeiro.configuration.auth.firebase.FirebaseTokenHolder;
import br.com.controle.financeiro.controlefinanceiro.ControlefinanceiroApplication;
import br.com.controle.financeiro.service.FirebaseService;
import br.com.controle.financeiro.service.UserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ControlefinanceiroApplication.class })
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class SingUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FirebaseService firebaseService;

    @MockBean
    private UserService userService;

    @Test
    public void signUp() throws Exception {
        FirebaseTokenHolder holder = mock(FirebaseTokenHolder.class);

        doReturn(holder).when(firebaseService).parseToken(anyString());
        doReturn("some@one.tk").when(holder).getEmail();
        doReturn("123456").when(holder).getUid();

        mockMvc.perform(MockMvcRequestBuilders.post("/open/signup").header("X-Firebase-User", "123"))
               .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void signUpWithouToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/open/signup"))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


}