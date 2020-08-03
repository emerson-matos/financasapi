package br.com.controle.financeiro.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

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

import br.com.controle.financeiro.controlefinanceiro.ControleFinanceiroApplication;
import br.com.controle.financeiro.controller.RestResponseEntityExceptionHandler;
import br.com.controle.financeiro.controller.api.linkbuilder.BankAccountDTOResourceAssembler;
import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.repository.BankAccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ControleFinanceiroApplication.class, BankAccountDTOResourceAssembler.class })
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Import({ RestResponseEntityExceptionHandler.class })
@WithMockUser(value = "someone")
public class BankAccountControllerTests {

    private static final String BANK_ACCOUNT_URI = "/api/bankaccount";
    private static final String ACCOUNT_JSON = "{\"agency\":\"account\", \"number\":\"1\", \"dac\":\"1\"}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountRepository accountRepository;

    private BankAccount bankAccount;

    @Before
    public void setup() {
        setupBankAccount();
        when(accountRepository.save(any())).thenReturn(bankAccount);
    }

    private void setupBankAccount() {
        bankAccount = new BankAccount();
        bankAccount.setId(1L);
    }

    @Test
    public void bankAccountGetAllTestAcceptJSON() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BANK_ACCOUNT_URI).accept(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void bankAccountPostTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BANK_ACCOUNT_URI).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                              .accept(MediaType.APPLICATION_JSON_UTF8).content(ACCOUNT_JSON))
               .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    }

    @Test
    public void bankAccountPutOldAccountTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(BANK_ACCOUNT_URI + "/{id}", 1)
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(ACCOUNT_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void bankAccountPutNewAccountTest() throws Exception {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(bankAccount));

        mockMvc.perform(MockMvcRequestBuilders.put(BANK_ACCOUNT_URI + "/{id}", 1)
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(ACCOUNT_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void bankAccountGetOneNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BANK_ACCOUNT_URI + "/{id}", 1)
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void bankAccountGetOneFoundTest() throws Exception {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(bankAccount));

        mockMvc.perform(MockMvcRequestBuilders.get(BANK_ACCOUNT_URI + "/{id}", 1)
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void bankAccountDeleteTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BANK_ACCOUNT_URI + "/{id}", 5))
               .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
    }

}
