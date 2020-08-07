package br.com.controle.financeiro.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import br.com.controle.financeiro.controlefinanceiro.ControleFinanceiroApplication;
import br.com.controle.financeiro.controller.RestResponseEntityExceptionHandler;
import br.com.controle.financeiro.controller.api.linkbuilder.BankAccountDTOResourceAssembler;
import br.com.controle.financeiro.model.repository.BankAccountRepository;
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
@SpringBootTest(classes = { ControleFinanceiroApplication.class, BankAccountDTOResourceAssembler.class })
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Import({ RestResponseEntityExceptionHandler.class })
@WithMockUser(value = "someone")
public class BankAccountControllerTests extends BaseTemplateTest {

    private static final String BANK_ACCOUNT_URI = "/api/bankaccount";
    private static final String ACCOUNT_JSON =
            "{\"agency\": \"agency\",\"number\": \"5423\",\"dac\": \"5423\"," +
                    "\"owner\":\"4609f55b-9c05-4361-9e7a-be2d52cfd6af\",\"institution\": " +
                    "\"4609f55b-9c05-4361-9e7a-be2d52cfd6aa\"}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountRepository accountRepository;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private InstitutionRepository institutionRepository;

    @MockBean
    private UserService userService;

    @Before
    public void setup() {
        this.setupModel();
        when(userService.getAuthenticatedUser()).thenReturn(owner);
        when(institutionRepository.findById(any())).thenReturn(Optional.of(institution));
        when(clientRepository.findByIdAndOwner(any(), any())).thenReturn(Optional.of(client));
        when(accountRepository.save(any())).thenReturn(bankAccount);
    }

    @Test
    public void bankAccountGetAllTest() throws Exception {
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
        mockMvc.perform(MockMvcRequestBuilders.put(BANK_ACCOUNT_URI + "/{id}", UUID.randomUUID())
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(ACCOUNT_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void bankAccountPutNewAccountTest() throws Exception {
        when(accountRepository.findByIdAndOwner(any(), any())).thenReturn(Optional.of(bankAccount));

        mockMvc.perform(MockMvcRequestBuilders.put(BANK_ACCOUNT_URI + "/{id}", UUID.randomUUID())
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(ACCOUNT_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void bankAccountGetOneNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BANK_ACCOUNT_URI + "/{id}", UUID.randomUUID())
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void bankAccountGetOneFoundTest() throws Exception {
        when(accountRepository.findByIdAndOwner(any(), any())).thenReturn(Optional.of(bankAccount));

        mockMvc.perform(MockMvcRequestBuilders.get(BANK_ACCOUNT_URI + "/{id}", UUID.randomUUID())
                                              .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void bankAccountDeleteTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BANK_ACCOUNT_URI + "/{id}", UUID.randomUUID()))
               .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
    }

}
