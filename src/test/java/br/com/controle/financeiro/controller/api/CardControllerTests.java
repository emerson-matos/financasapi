package br.com.controle.financeiro.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import br.com.controle.financeiro.controlefinanceiro.ControleFinanceiroApplication;
import br.com.controle.financeiro.controller.RestResponseEntityExceptionHandler;
import br.com.controle.financeiro.controller.api.linkbuilder.CardDTOResourceAssembler;
import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.repository.CardRepository;
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
public class CardControllerTests {

    private static final String CARD_JSON = "{\"name\":\"Card\", \"number\":\"1234132\"}";
    public static final String API_CARD_URL = "/api/card";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardRepository cardRepository;

    private Card card;

    @Before
    public void setup() {
        setupCard();
        when(cardRepository.save(any())).thenReturn(card);
    }

    private void setupCard() {
        card = new Card();
        card.setId(1L);
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
        mockMvc.perform(
                MockMvcRequestBuilders.put(API_CARD_URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8)
                                      .content(CARD_JSON)).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
               .andReturn();
    }

    @Test
    public void cardPutNewCardTest() throws Exception {
        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

        mockMvc.perform(
                MockMvcRequestBuilders.put(API_CARD_URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8)
                                      .content(CARD_JSON)).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
               .andReturn();
    }

    @Test
    public void cardGetOneNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_CARD_URL + "/{id}", 1)
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void cardGetOneFoundTest() throws Exception {
        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

        mockMvc.perform(MockMvcRequestBuilders.get(API_CARD_URL + "/{id}", 1)
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void cardDeleteTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_CARD_URL + "/{id}", 5))
               .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
    }

}
