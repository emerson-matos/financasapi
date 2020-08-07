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
import org.springframework.hateoas.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.controle.financeiro.model.dto.BankAccountDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { BankAccountDTOResourceAssembler.class })
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class BankAccountDTOResourceAssemblerTest {

	@InjectMocks
	private BankAccountDTOResourceAssembler resourceAssembler;


	@Test
	public void testToResource() {
		BankAccountDTO bankAccountMock = new BankAccountDTO();
		bankAccountMock.setId(UUID.randomUUID());
		Resource<BankAccountDTO> response = resourceAssembler.toResource(bankAccountMock);
		assertTrue(response.hasLinks());
		assertNotNull(response.getLink("self"));
		assertNotNull(response.getLink("bankaccounts"));
		assertEquals(bankAccountMock, response.getContent());
	}

}
