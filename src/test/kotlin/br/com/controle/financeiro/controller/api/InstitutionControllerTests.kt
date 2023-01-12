package br.com.controle.financeiro.controller.api

import br.com.controle.financeiro.model.repository.InstitutionRepository
import br.com.controle.financeiro.model.repository.UserRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class InstitutionControllerTests(@Autowired private val mockMvc: MockMvc) : BaseModelTemplate() {

    @MockkBean private lateinit var institutionRepository: InstitutionRepository
    @MockkBean private lateinit var userRepository: UserRepository

    @BeforeAll
    fun setup() {
        every { institutionRepository.save(any()) } returns institution
    }

    @Test
    fun institutionGetAllTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(API_INSTITUTION)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
    }

    @Test
    fun institutionPostTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(API_INSTITUTION)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(INSTITUTION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andReturn()
    }

    @Test
    fun institutionPutOldInstitutionTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.put(API_INSTITUTION + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(INSTITUTION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andReturn()
    }

    @Test
    fun institutionPutNewInstitutionTest() {
        every { institutionRepository.findById(any()) } returns Optional.of(institution)
        mockMvc.perform(
                        MockMvcRequestBuilders.put(API_INSTITUTION + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(INSTITUTION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andReturn()
    }

    @Test
    fun institutionGetOneNotFoundTest() {
        mockMvc.perform(MockMvcRequestBuilders.get(API_INSTITUTION + "/{id}", UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andReturn()
    }

    @Test
    fun institutionGetOneFoundTest() {
        every { institutionRepository.findById(any()) } returns Optional.of(institution)
        mockMvc.perform(MockMvcRequestBuilders.get(API_INSTITUTION + "/{id}", UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
    }

    @Test
    fun institutionDeleteTest() {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_INSTITUTION + "/{id}", UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNoContent)
                .andReturn()
    }

    companion object {
        const val API_INSTITUTION = "/api/institution"
        const val INSTITUTION_JSON = "{\"name\":\"institution\", \"identifier\": \"bank\"}"
    }
}
