package br.com.controle.financeiro.controller.api

import br.com.controle.financeiro.model.repository.BankAccountRepository
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
class BankAccountControllerTests(@Autowired val mockMvc: MockMvc) : BaseModelTemplate() {

    @MockkBean private lateinit var accountRepository: BankAccountRepository

    @MockkBean private lateinit var clientRepository: ClientRepository

    @MockkBean private lateinit var institutionRepository: InstitutionRepository

    @MockkBean private lateinit var userRepository: UserRepository

    @BeforeAll
    fun setup() {
        every { institutionRepository.findById(any()) } returns Optional.of(institution)
        every { clientRepository.findByIdAndOwner(any(), any()) } returns Optional.of(client)

        every { accountRepository.save(any()) } returns bankAccount
    }

    @Test
    fun bankAccountGetAllTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(BANK_ACCOUNT_URI)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
    }

    @Test
    fun bankAccountPostTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(BANK_ACCOUNT_URI)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .content(ACCOUNT_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andReturn()
    }

    @Test
    fun bankAccountPutOldAccountTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.put(BANK_ACCOUNT_URI + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(ACCOUNT_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andReturn()
    }

    @Test
    fun bankAccountPutNewAccountTest() {
        every { accountRepository.findByIdAndOwner(any(), any()) } returns Optional.of(bankAccount)
        mockMvc.perform(
                        MockMvcRequestBuilders.put(BANK_ACCOUNT_URI + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(ACCOUNT_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andReturn()
    }

    @Test
    fun bankAccountGetOneNotFoundTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(BANK_ACCOUNT_URI + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andReturn()
    }

    @Test
    fun bankAccountGetOneFoundTest() {
        every { accountRepository.findByIdAndOwner(any(), any()) } returns Optional.of(bankAccount)
        mockMvc.perform(
                        MockMvcRequestBuilders.get(BANK_ACCOUNT_URI + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
    }

    @Test
    fun bankAccountDeleteTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete(BANK_ACCOUNT_URI + "/{id}", UUID.randomUUID())
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent)
                .andReturn()
    }

    companion object {
        private const val BANK_ACCOUNT_URI = "/api/bankaccount"
        private const val ACCOUNT_JSON =
                "{\"agency\": \"agency\",\"number\": \"5423\",\"dac\": \"5423\"," +
                        "\"owner\":\"4609f55b-9c05-4361-9e7a-be2d52cfd6af\",\"institution\": " +
                        "\"4609f55b-9c05-4361-9e7a-be2d52cfd6aa\"}"
    }
}
