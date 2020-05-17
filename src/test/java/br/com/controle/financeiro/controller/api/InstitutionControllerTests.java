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
import br.com.controle.financeiro.controller.api.linkbuilder.InstitutionDTOResourceAssembler;
import br.com.controle.financeiro.model.entity.Institution;
import br.com.controle.financeiro.model.repository.InstitutionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ControlefinanceiroApplication.class, InstitutionDTOResourceAssembler.class })
@AutoConfigureMockMvc
@EnableWebMvc
@Import({ RestResponseEntityExceptionHandler.class })
public class InstitutionControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private Institution institution;

	@MockBean
	private InstitutionRepository institutionRepository;

	@Spy
	private InstitutionDTOResourceAssembler institutionDTOResourceAssembler;

	@Before
	public void setup() {
		setupInstitution();
		when(institutionRepository.save(any())).thenReturn(institution);
	}

	private void setupInstitution() {
		institution = new Institution("mock");
		institution.setId(1L);
	}

	@Test
	public void institutionGetAllTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/institution").accept("*/*"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void institutionPostTest() throws Exception {
		byte[] institutionJson = "{\"name\":\"institution\", \"identifier\": \"001\"}".getBytes();
		mockMvc.perform(MockMvcRequestBuilders.post("/institution").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.accept("*/*").content(institutionJson)).andExpect(MockMvcResultMatchers.status().isCreated())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void institutionPutOldInstitutionTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/institution/{id}", 1)
				.header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE).content(("{\"name\":\"institution\"}")))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void institutionPutNewInstitutionTest() throws Exception {
		when(institutionRepository.findById(anyLong())).thenReturn(Optional.of(institution));

		mockMvc.perform(MockMvcRequestBuilders.put("/institution/{id}", 1)
				.header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE).content(("{\"name\":\"institution\"}")))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void institutionGetOneNotFoundTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/institution/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void institutionGetOneFoundTest() throws Exception {
		when(institutionRepository.findById(anyLong())).thenReturn(Optional.of(institution));

		mockMvc.perform(MockMvcRequestBuilders.get("/institution/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	@Test
	public void institutionDeleteTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/institution/{id}", 5))
				.andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

}
