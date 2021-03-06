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

import br.com.controle.financeiro.model.dto.InstitutionDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { InstitutionDTOResourceAssembler.class })
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class InstitutionDTOResourceAssemblerTest {

	@InjectMocks
	private InstitutionDTOResourceAssembler resourceAssembler;


	@Test
	public void testToResource() {
		InstitutionDTO institutionMock = new InstitutionDTO();
		institutionMock.setId(UUID.randomUUID());
		institutionMock.setName("mock");
		EntityModel<InstitutionDTO> response = resourceAssembler.toModel(institutionMock);
		assertTrue(response.hasLinks());
		assertNotNull(response.getLink("self"));
		assertNotNull(response.getLink("institutions"));
		assertEquals(institutionMock, response.getContent());
	}

}
