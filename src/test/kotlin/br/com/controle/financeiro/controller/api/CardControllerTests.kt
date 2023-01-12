package br.com.controle.financeiro.controller.api

import br.com.controle.financeiro.model.repository.CardRepository
import br.com.controle.financeiro.model.repository.ClientRepository
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
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class CardControllerTests(@Autowired val mockMvc: MockMvc) : BaseModelTemplate() {
    @MockkBean private lateinit var cardRepository: CardRepository
    @MockkBean private lateinit var clientRepository: ClientRepository
    @MockkBean private lateinit var institutionRepository: InstitutionRepository
    @MockkBean private lateinit var userRepository: UserRepository

    @BeforeAll
    fun setup() {
        every { cardRepository.save(any()) } returns card
        every { institutionRepository.findById(any()) } returns Optional.of(institution)
        every { clientRepository.findByIdAndOwner(any(), any()) } returns Optional.of(client)
    }

    @Test
    fun cardGetAllTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(API_CARD_URL)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
    }

    @Test
    fun cardPostTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(API_CARD_URL)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(CARD_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andReturn()
    }

    @Test
    fun cardPutOldCardTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.put(API_CARD_URL + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(CARD_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andReturn()
    }

    @Test
    fun cardPutNewCardTest() {
        every { cardRepository.findById(any()) } returns Optional.of(card)
        mockMvc.perform(
                        MockMvcRequestBuilders.put(API_CARD_URL + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(CARD_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andReturn()
    }

    @Test
    fun cardGetOneNotFoundTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(API_CARD_URL + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andReturn()
    }

    @Test
    fun cardGetOneFoundTest() {
        every { cardRepository.findByIdAndOwner(any(), any()) } returns Optional.of(card)
        mockMvc.perform(
                        MockMvcRequestBuilders.get(API_CARD_URL + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
    }

    @Test
    fun cardDeleteTest() {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_CARD_URL + "/{id}", UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNoContent)
                .andReturn()
    }

    companion object {
        private const val CARD_JSON =
                "{\"name\": \"Emerson\",\"number\": \"5423\",\"client\": \"a3dbfd2a-8e96-417e-bc01-ff7a798bf4c4\"," +
                        "\"institution\": \"9f7c1d94-c41f-4cab-a6eb-f4180f2a0e0b\"}"
        const val API_CARD_URL = "/api/card"
    }
}
