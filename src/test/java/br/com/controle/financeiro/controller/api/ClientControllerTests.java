package br.com.controle.financeiro.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import br.com.controle.financeiro.controlefinanceiro.ControleFinanceiroApplication;
import br.com.controle.financeiro.controller.RestResponseEntityExceptionHandler;
import br.com.controle.financeiro.controller.api.linkbuilder.ClientDTOResourceAssembler;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.UserEntity;
import br.com.controle.financeiro.model.repository.ClientRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ControleFinanceiroApplication.class, ClientDTOResourceAssembler.class })
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Import({ RestResponseEntityExceptionHandler.class })
@WithMockUser(value = "someone")
public class ClientControllerTests {

    public static final String API_CLIENT = "/api/client";
    public static final String CLIENT_JSON = "{\"name\":\"Pedro\"}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientRepository clientRepository;

    private Client client;

    @Before
    public void setup() {
        setupClient();
        when(clientRepository.save(any())).thenReturn(client);
    }

    private void setupClient() {
        client = new Client(UUID.randomUUID(), "mock", new UserEntity());
    }

    @Test
    public void clientGetAllTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_CLIENT).accept(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void clientPostTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(API_CLIENT).contentType(MediaType.APPLICATION_JSON_UTF8)
                                              .content(CLIENT_JSON))
               .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    }

    @Test
    public void clientPutOldClientTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(API_CLIENT + "/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8)
                                              .content(CLIENT_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void clientPutNewClientTest() throws Exception {
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));

        mockMvc.perform(MockMvcRequestBuilders.put(API_CLIENT + "/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8)
                                              .content(CLIENT_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void clientGetOneNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_CLIENT + "/{id}", 1).accept(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void clientGetOneFoundTest() throws Exception {
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));

        mockMvc.perform(MockMvcRequestBuilders.get(API_CLIENT + "/{id}", 1).accept(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void clientDeleteTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_CLIENT + "/{id}", 5))
               .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
    }

    @Test
    public void clientDeleteNotFoundTest() throws Exception {
        doThrow(new EmptyResultDataAccessException(5)).when(clientRepository).deleteById(any());

        mockMvc.perform(MockMvcRequestBuilders.delete(API_CLIENT + "/{id}", 5))
               .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
    }

}
