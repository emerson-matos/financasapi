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
import br.com.controle.financeiro.controller.linkbuilder.TransactionDTOResourceAssembler;
import br.com.controle.financeiro.model.entity.Transaction;
import br.com.controle.financeiro.model.repository.TransactionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ControlefinanceiroApplication.class, TransactionDTOResourceAssembler.class })
@AutoConfigureMockMvc
@EnableWebMvc
@Import({ RestResponseEntityExceptionHandler.class })
public class TransactionControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private Transaction transaction;

	@MockBean
	private TransactionRepository transactionRepository;

	@Spy
	private TransactionDTOResourceAssembler transactionDTOResourceAssembler;

	@Before
	public void setup() {
		setupTransaction();
		when(transactionRepository.save(any())).thenReturn(transaction);
	}

	private void setupTransaction() {
		transaction = new Transaction();
		transaction.setId(1L);
	}

	@Test
	public void transactionGetAllTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/transaction").accept("*/*"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void transactionPostTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/transaction").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.accept("*/*").content(("{\"name\":\"transaction\"}"))).andExpect(MockMvcResultMatchers.status().isCreated())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void transactionPutOldTransactionTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/transaction/{id}", 1)
				.header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE).content(("{\"name\":\"Transaction\"}")))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void transactionPutNewTransactionTest() throws Exception {
		when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));

		mockMvc.perform(MockMvcRequestBuilders.put("/transaction/{id}", 1)
				.header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE).content(("{\"name\":\"transaction\"}")))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void transactionGetOneNotFoundTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/transaction/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(("{\"name\":\"transaction\"}"))).andExpect(MockMvcResultMatchers.status().isNotFound())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void transactionGetOneFoundTest() throws Exception {
		when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));

		mockMvc.perform(MockMvcRequestBuilders.get("/transaction/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(("{\"name\":\"transaction\"}"))).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void transactionDeleteTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/transaction/{id}", 5))
				.andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

}
