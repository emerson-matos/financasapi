package br.com.controle.financeiro.controller.api.linkbuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.controle.financeiro.model.dto.CardDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CardDTOResourceAssembler.class })
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class CardDTOResourceAssemblerTest {

	@InjectMocks
	private CardDTOResourceAssembler resourceAssembler;


	@Test
	public void testToResource() {
		CardDTO cardMock = new CardDTO();
		cardMock.setId(UUID.randomUUID());
		cardMock.setName("mock");
		EntityModel<CardDTO> response = resourceAssembler.toModel(cardMock);
		assertTrue(response.hasLinks());
		assertNotNull(response.getLink("self"));
		assertNotNull(response.getLink("cards"));
		assertEquals(cardMock, response.getContent());
	}

}
