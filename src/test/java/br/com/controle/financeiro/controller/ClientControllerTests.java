package br.com.controle.financeiro.controller;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.controle.financeiro.controller.linkbuilder.ClientDTOResourceAssembler;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.repository.ClientRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ClientController.class, ClientDTOResourceAssembler.class })
@AutoConfigureMockMvc
@Import({ RestResponseEntityExceptionHandler.class })
public class ClientControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private Client client;

	// @InjectMocks
	// private ClientController controller;

	@MockBean
	private ClientRepository clientRepository;

	@Spy
	private ClientDTOResourceAssembler clientDTOResourceAssembler;

	@Before
	public void setup() {
		// this.mockMvc =
		// MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new
		// RestResponseEntityExceptionHandler())
		// .build();
		setupClient();
	}

	private void setupClient() {
		client = new Client("mock");
		client.setId(1L);
	}

	@Test
	public void clientGetAllTest() throws Exception {
		// given(clientRepository.findAll()).willReturn(Arrays.asList(client, client,
		// client));
		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.get("/client").accept("*/*"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), andReturn.getResponse().getStatus());
	}

	@Test
	public void clientPostTest() throws Exception {
		// given(clientRepository.findAll()).willReturn(Arrays.asList(client, client,
		// client));
		MvcResult andReturn = mockMvc
				.perform(MockMvcRequestBuilders.post("/client").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(("{\"name\":\"Pedro\"}")))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), andReturn.getResponse().getStatus());
	}

	@Test
	public void clientPutTest() throws Exception {
		// given(clientRepository.findAll()).willReturn(Arrays.asList(client, client,
		// client));
		MvcResult andReturn = mockMvc
				.perform(MockMvcRequestBuilders.put("/client/{id}", 1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(("{\"name\":\"Pedro\"}")))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), andReturn.getResponse().getStatus());
	}

	@Test
	public void clientGetOneTest() throws Exception {
		// given(clientRepository.findAll()).willReturn(Arrays.asList(client, client,
		// client));
		MvcResult andReturn = mockMvc
				.perform(MockMvcRequestBuilders.get("/client/{id}", 1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(("{\"name\":\"Pedro\"}")))
				.andExpect(MockMvcResultMatchers.status().isNotFound()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		assertEquals(HttpStatus.NOT_FOUND.value(), andReturn.getResponse().getStatus());
	}

	@Test
	public void clientDeleteTest() throws Exception {
		// given(clientRepository.findAll()).willReturn(Arrays.asList(client, client,
		// client));
		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.delete("/client/{id}", 5))
				.andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		assertEquals(HttpStatus.NO_CONTENT.value(), andReturn.getResponse().getStatus());
	}

}
