package br.com.controle.financeiro.controller.api

import br.com.controle.financeiro.model.repository.ClientRepository
import br.com.controle.financeiro.model.repository.UserRepository
import br.com.controle.financeiro.model.entity.UserEntity
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class ClientControllerTests(@Autowired private val mockMvc: MockMvc) : BaseModelTemplate() {

    @MockkBean private lateinit var clientRepository: ClientRepository
    @MockkBean private lateinit var userRepository: UserRepository

    @BeforeAll
    fun setup() {
        every { clientRepository.save(any()) } returns client
        every { userRepository.findAll() } returns listOf(owner)
    }

    @Test
    fun clientGetAllTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(API_CLIENT)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
    }

    @Test
    fun clientPostTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(API_CLIENT)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(CLIENT_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andReturn()
    }

    @Test
    fun clientPutOldClientTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.put(API_CLIENT + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(CLIENT_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andReturn()
    }

    @Test
    fun clientPutNewClientTest() {
        every { clientRepository.findById(any()) } returns Optional.of(client)
        mockMvc.perform(
                        MockMvcRequestBuilders.put(API_CLIENT + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(CLIENT_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andReturn()
    }

    @Test
    fun clientGetOneNotFoundTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(API_CLIENT + "/{id}", UUID.randomUUID())
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andReturn()
    }

    @Test
    fun clientGetOneFoundTest() {
        every { clientRepository.findByIdAndOwner(any(), any()) } returns Optional.of(client)
        mockMvc.perform(
                        MockMvcRequestBuilders.get(API_CLIENT + "/{id}", UUID.randomUUID())
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
    }

    @Test
    fun clientDeleteTest() {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_CLIENT + "/{id}", UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNoContent)
                .andReturn()
    }

    @Test
    fun clientDeleteNotFoundTest() {
        every { clientRepository.deleteById(any()) } throws EmptyResultDataAccessException(1)
        mockMvc.perform(MockMvcRequestBuilders.delete(API_CLIENT + "/{id}", UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNoContent)
                .andReturn()
    }

    companion object {
        const val API_CLIENT = "/api/client"
        const val CLIENT_JSON = "{\"name\":\"Pedro\"}"
    }
}
