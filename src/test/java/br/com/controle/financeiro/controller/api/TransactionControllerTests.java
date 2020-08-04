package br.com.controle.financeiro.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

import br.com.controle.financeiro.controlefinanceiro.ControleFinanceiroApplication;
import br.com.controle.financeiro.controller.RestResponseEntityExceptionHandler;
import br.com.controle.financeiro.controller.api.linkbuilder.TransactionDTOResourceAssembler;
import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Transaction;
import br.com.controle.financeiro.model.repository.TransactionRepository;
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
public class TransactionControllerTests {

    public static final String API_TRANSACTION = "/api/transaction";
    public static final String TRANSACTION_JSON =
            "{\"name\": \"Trip\",\"transactionDate\": \"2020-07-18T04:45:20.871140Z\",\"value\": 10.22,\"currency\": " +
                    "\"BRL\"}";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    private Transaction transaction;

    @Before
    public void setup() {
        setupTransaction();
        when(transactionRepository.save(any())).thenReturn(transaction);
    }

    private void setupTransaction() {
        transaction = new Transaction(UUID.randomUUID(), "name", LocalDateTime.now(), new BigDecimal(1), Currency.getInstance("BRL"), new Client(), new BankAccount(), new Card());
    }

    @Test
    public void transactionGetAllTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_TRANSACTION).accept(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void transactionPostTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(API_TRANSACTION).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                      .content(TRANSACTION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated())
               .andReturn();
    }

    @Test
    public void transactionPutOldTransactionTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put(API_TRANSACTION + "/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8)
                                      .content(TRANSACTION_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
               .andReturn();
    }

    @Test
    public void transactionPutNewTransactionTest() throws Exception {
        when(transactionRepository.findById(any())).thenReturn(Optional.of(transaction));

        mockMvc.perform(
                MockMvcRequestBuilders.put(API_TRANSACTION + "/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8)
                                      .content(TRANSACTION_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
               .andReturn();
    }

    @Test
    public void transactionGetOneNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_TRANSACTION + "/{id}", 1)
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void transactionGetOneFoundTest() throws Exception {
        when(transactionRepository.findById(any())).thenReturn(Optional.of(transaction));

        mockMvc.perform(MockMvcRequestBuilders.get(API_TRANSACTION + "/{id}", 1)
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void transactionDeleteTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_TRANSACTION + "/{id}", 5))
               .andExpect(MockMvcResultMatchers.status().isNoContent())
               .andReturn();
    }

}
