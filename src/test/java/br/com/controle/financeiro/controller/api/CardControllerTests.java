package br.com.controle.financeiro.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import br.com.controle.financeiro.controlefinanceiro.ControleFinanceiroApplication;
import br.com.controle.financeiro.controller.RestResponseEntityExceptionHandler;
import br.com.controle.financeiro.controller.api.linkbuilder.CardDTOResourceAssembler;
import br.com.controle.financeiro.model.entity.UserEntity;
import br.com.controle.financeiro.model.repository.CardRepository;
import br.com.controle.financeiro.model.repository.ClientRepository;
import br.com.controle.financeiro.model.repository.InstitutionRepository;
import br.com.controle.financeiro.service.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ControleFinanceiroApplication.class, CardDTOResourceAssembler.class })
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Import({ RestResponseEntityExceptionHandler.class })
@WithMockUser(value = "someone")
public class CardControllerTests extends BaseModelTemplate {

    private static final String CARD_JSON =
            "{\"name\": \"Emerson\",\"number\": \"5423\",\"client\": \"a3dbfd2a-8e96-417e-bc01-ff7a798bf4c4\"," +
                    "\"institution\": \"9f7c1d94-c41f-4cab-a6eb-f4180f2a0e0b\"}";
    public static final String API_CARD_URL = "/api/card";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private InstitutionRepository institutionRepository;

    @MockBean
    private UserService userService;

    @Before
    public void setup() {
        this.setupModel();
        when(cardRepository.save(any())).thenReturn(card);
        when(userService.getAuthenticatedUser()).thenReturn(owner);
        when(institutionRepository.findById(any())).thenReturn(Optional.of(institution));
        when(clientRepository.findByIdAndOwner(any(), any())).thenReturn(Optional.of(client));
    }

    @Test
    public void cardGetAllTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_CARD_URL).accept(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void cardPostTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(API_CARD_URL).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                              .content(CARD_JSON)).andExpect(MockMvcResultMatchers.status().isCreated())
               .andReturn();
    }

    @Test
    public void cardPutOldCardTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(API_CARD_URL + "/{id}", UUID.randomUUID())
                                              .contentType(MediaType.APPLICATION_JSON_UTF8).content(CARD_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void cardPutNewCardTest() throws Exception {
        when(cardRepository.findById(any())).thenReturn(Optional.of(card));

        mockMvc.perform(MockMvcRequestBuilders.put(API_CARD_URL + "/{id}", UUID.randomUUID())
                                              .contentType(MediaType.APPLICATION_JSON_UTF8).content(CARD_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void cardGetOneNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_CARD_URL + "/{id}", UUID.randomUUID())
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void cardGetOneFoundTest() throws Exception {
        when(cardRepository.findByIdAndOwner(any(), any(UserEntity.class))).thenReturn(Optional.of(card));

        mockMvc.perform(MockMvcRequestBuilders.get(API_CARD_URL + "/{id}", UUID.randomUUID())
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void cardDeleteTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_CARD_URL + "/{id}", UUID.randomUUID()))
               .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
    }

}
