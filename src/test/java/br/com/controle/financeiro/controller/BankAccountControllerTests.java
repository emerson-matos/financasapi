package br.com.controle.financeiro.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import br.com.controle.financeiro.controlefinanceiro.ControlefinanceiroApplication;
import br.com.controle.financeiro.controller.linkbuilder.BankAccountDTOResourceAssembler;
import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.repository.BankAccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ControlefinanceiroApplication.class, BankAccountDTOResourceAssembler.class })
@AutoConfigureMockMvc
@EnableWebMvc
@Import({ RestResponseEntityExceptionHandler.class })
public class BankAccountControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private BankAccount bankAccount;

	@MockBean
	private BankAccountRepository accountRepository;

	@Spy
	private BankAccountDTOResourceAssembler accountDTOResourceAssembler;

	private byte[] contaJson = "{\"agency\":\"account\", \"number\":\"1\", \"dac\":\"1\"}".getBytes();

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
	public void bankAccountGetAllTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/bankaccount").accept("*/*"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void bankAccountPostTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/bankaccount").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.accept("*/*").content(contaJson)).andExpect(MockMvcResultMatchers.status().isCreated())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void bankAccountPutOldAccountTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/bankaccount/{id}", 1)
				.header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE).content(contaJson))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void bankAccountPutNewAccountTest() throws Exception {
		when(accountRepository.findById(anyLong())).thenReturn(Optional.of(bankAccount));

		mockMvc.perform(MockMvcRequestBuilders.put("/bankaccount/{id}", 1)
				.header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE).content(("{\"name\":\"account\"}")))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void bankAccountGetOneNotFoundTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/bankaccount/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(("{\"name\":\"account\"}"))).andExpect(MockMvcResultMatchers.status().isNotFound())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void bankAccountGetOneFoundTest() throws Exception {
		when(accountRepository.findById(anyLong())).thenReturn(Optional.of(bankAccount));

		mockMvc.perform(MockMvcRequestBuilders.get("/bankaccount/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(("{\"name\":\"account\"}"))).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void bankAccountDeleteTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/bankaccount/{id}", 5))
				.andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

}
