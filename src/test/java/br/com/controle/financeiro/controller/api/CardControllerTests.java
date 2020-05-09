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
import br.com.controle.financeiro.controller.api.linkbuilder.CardDTOResourceAssembler;
import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.repository.CardRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ControlefinanceiroApplication.class, CardDTOResourceAssembler.class })
@AutoConfigureMockMvc
@EnableWebMvc
@Import({ RestResponseEntityExceptionHandler.class })
public class CardControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private Card card;

	@MockBean
	private CardRepository cardRepository;

	@Spy
	private CardDTOResourceAssembler cardDTOResourceAssembler;

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
		mockMvc.perform(MockMvcRequestBuilders.get("/card").accept("*/*"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void cardPostTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/card").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.accept("*/*").content(("{\"name\":\"Card\"}"))).andExpect(MockMvcResultMatchers.status().isCreated())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void cardPutOldCardTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/card/{id}", 1)
				.header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE).content(("{\"name\":\"Card\"}")))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void cardPutNewCardTest() throws Exception {
		when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

		mockMvc.perform(MockMvcRequestBuilders.put("/card/{id}", 1)
				.header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE).content(("{\"name\":\"Card\"}")))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void cardGetOneNotFoundTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/card/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(("{\"name\":\"Card\"}"))).andExpect(MockMvcResultMatchers.status().isNotFound())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void cardGetOneFoundTest() throws Exception {
		when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

		mockMvc.perform(MockMvcRequestBuilders.get("/card/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(("{\"name\":\"Card\"}"))).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void cardDeleteTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/card/{id}", 5))
				.andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

}
