package br.com.controle.financeiro.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import br.com.controle.financeiro.controlefinanceiro.ControleFinanceiroApplication;
import br.com.controle.financeiro.controller.RestResponseEntityExceptionHandler;
import br.com.controle.financeiro.controller.api.linkbuilder.InstitutionDTOResourceAssembler;
import br.com.controle.financeiro.model.entity.Institution;
import br.com.controle.financeiro.model.repository.InstitutionRepository;

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
@SpringBootTest(classes = { ControleFinanceiroApplication.class, InstitutionDTOResourceAssembler.class })
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Import({ RestResponseEntityExceptionHandler.class })
@WithMockUser(value = "someone")
public class InstitutionControllerTests {

    public static final String API_INSTITUTION = "/api/institution";
    public static final String INSTITUTION_JSON = "{\"name\":\"institution\"}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstitutionRepository institutionRepository;

    private Institution institution;

    @Before
    public void setup() {
        setupInstitution();
        when(institutionRepository.save(any())).thenReturn(institution);
    }

    private void setupInstitution() {
        institution = new Institution(1L, "mock", "mock");
    }

    @Test
    public void institutionGetAllTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_INSTITUTION).accept(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void institutionPostTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(API_INSTITUTION).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                              .content(INSTITUTION_JSON))
               .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    }

    @Test
    public void institutionPutOldInstitutionTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put(API_INSTITUTION + "/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8)
                                      .content(INSTITUTION_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void institutionPutNewInstitutionTest() throws Exception {
        when(institutionRepository.findById(anyLong())).thenReturn(Optional.of(institution));

        mockMvc.perform(
                MockMvcRequestBuilders.put(API_INSTITUTION + "/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8)
                                      .content(INSTITUTION_JSON))
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void institutionGetOneNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_INSTITUTION + "/{id}", 1))
               .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void institutionGetOneFoundTest() throws Exception {
        when(institutionRepository.findById(anyLong())).thenReturn(Optional.of(institution));

        mockMvc.perform(MockMvcRequestBuilders.get(API_INSTITUTION + "/{id}", 1))
               .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void institutionDeleteTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_INSTITUTION + "/{id}", 5))
               .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
    }

}
