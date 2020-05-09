package br.com.controle.financeiro.controller.api;

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
import br.com.controle.financeiro.controller.RestResponseEntityExceptionHandler;
import br.com.controle.financeiro.controller.api.linkbuilder.ClientDTOResourceAssembler;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.repository.ClientRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ControlefinanceiroApplication.class, ClientDTOResourceAssembler.class })
@AutoConfigureMockMvc
@EnableWebMvc
@Import({ RestResponseEntityExceptionHandler.class })
public class ClientControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private Client client;

	@MockBean
	private ClientRepository clientRepository;

	@Spy
	private ClientDTOResourceAssembler clientDTOResourceAssembler;

	@Before
	public void setup() {
		setupClient();
		when(clientRepository.save(any())).thenReturn(client);
	}

	private void setupClient() {
		client = new Client("mock");
		client.setId(1L);
	}

	@Test
	public void clientGetAllTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/client").accept("*/*"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void clientPostTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/client").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.accept("*/*").content(("{\"name\":\"Pedro\"}"))).andExpect(MockMvcResultMatchers.status().isCreated())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void clientPutOldClientTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/client/{id}", 1)
				.header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE).content(("{\"name\":\"Pedro\"}")))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void clientPutNewClientTest() throws Exception {
		when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));

		mockMvc.perform(MockMvcRequestBuilders.put("/client/{id}", 1)
				.header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE).content(("{\"name\":\"Pedro\"}")))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void clientGetOneNotFoundTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/client/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(("{\"name\":\"Pedro\"}"))).andExpect(MockMvcResultMatchers.status().isNotFound())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void clientGetOneFoundTest() throws Exception {
		when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));

		mockMvc.perform(MockMvcRequestBuilders.get("/client/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(("{\"name\":\"Pedro\"}"))).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void clientDeleteTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/client/{id}", 5))
				.andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

}
