package br.com.controle.financeiro.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import br.com.controle.financeiro.controlefinanceiro.ControleFinanceiroApplication;
import br.com.controle.financeiro.controller.RestResponseEntityExceptionHandler;
import br.com.controle.financeiro.controller.api.linkbuilder.TransactionDTOResourceAssembler;
import br.com.controle.financeiro.model.repository.BankAccountRepository;
import br.com.controle.financeiro.model.repository.CardRepository;
import br.com.controle.financeiro.model.repository.ClientRepository;
import br.com.controle.financeiro.model.repository.TransactionRepository;
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
@SpringBootTest(classes = { ControleFinanceiroApplication.class, TransactionDTOResourceAssembler.class })
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Import({ RestResponseEntityExceptionHandler.class })
@WithMockUser(value = "someone")
public class TransactionControllerTests extends BaseModelTemplate {

    public static final String API_TRANSACTION = "/api/transaction";
    public static final String TRANSACTION_JSON =
            "{\"name\": \"Viagem\",\"transactionDate\": \"2020-01-10 23:57:22\",\"value\": 10.22,\"currency\": " +
                    "\"BRL\",\"client\": \"962a9e4e-0d33-45cb-8a70-d8116a7070d3\",\"account\": \"962a9e4e-0d33-45cb" +
                    "-8a70-d8116a6960d3\",\"card\": \"962a9e4e-0d33-45cb-8a70-d8116a7070f3\"}";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountRepository accountRepository;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private UserService userService;

    @Before
    public void setup() {
        this.setupModel();
        when(accountRepository.findByIdAndOwner(any(), any())).thenReturn(Optional.of(bankAccount));
        when(cardRepository.findByIdAndOwner(any(), any())).thenReturn(Optional.of(card));
        when(clientRepository.findByIdAndOwner(any(), any())).thenReturn(Optional.of(client));
        when(transactionRepository.save(any())).thenReturn(transaction);
        when(userService.getAuthenticatedUser()).thenReturn(owner);
    }

    @Test
    public void transactionGetAllTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_TRANSACTION).accept(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void transactionPostTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(API_TRANSACTION).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                              .content(TRANSACTION_JSON))
               .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    }

    @Test
    public void transactionPutOldTransactionTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(API_TRANSACTION + "/{id}", UUID.randomUUID())
                                              .contentType(MediaType.APPLICATION_JSON_UTF8).content(TRANSACTION_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void transactionPutNewTransactionTest() throws Exception {
        when(transactionRepository.findByIdAndOwner(any(), any())).thenReturn(Optional.of(transaction));

        mockMvc.perform(MockMvcRequestBuilders.put(API_TRANSACTION + "/{id}", UUID.randomUUID())
                                              .contentType(MediaType.APPLICATION_JSON_UTF8).content(TRANSACTION_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void transactionGetOneNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_TRANSACTION + "/{id}", UUID.randomUUID())
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void transactionGetOneFoundTest() throws Exception {
        when(transactionRepository.findByIdAndOwner(any(), any())).thenReturn(Optional.of(transaction));

        mockMvc.perform(MockMvcRequestBuilders.get(API_TRANSACTION + "/{id}", UUID.randomUUID())
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void transactionDeleteTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_TRANSACTION + "/{id}", UUID.randomUUID()))
               .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
    }

}
