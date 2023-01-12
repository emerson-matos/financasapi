package br.com.controle.financeiro.controller.api

import br.com.controle.financeiro.model.repository.BankAccountRepository
import br.com.controle.financeiro.model.repository.CardRepository
import br.com.controle.financeiro.model.repository.ClientRepository
import br.com.controle.financeiro.model.repository.TransactionRepository
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
class TransactionControllerTests(@Autowired private val mockMvc: MockMvc) : BaseModelTemplate() {

    @MockkBean private lateinit var accountRepository: BankAccountRepository

    @MockkBean private lateinit var cardRepository: CardRepository

    @MockkBean private lateinit var clientRepository: ClientRepository

    @MockkBean private lateinit var transactionRepository: TransactionRepository

    @MockkBean private lateinit var userRepository: UserRepository

    @BeforeAll
    fun setup() {
        every { accountRepository.findByIdAndOwner(any(), any()) } returns Optional.of(bankAccount)
        every { cardRepository.findByIdAndOwner(any(), any()) } returns Optional.of(card)
        every { clientRepository.findByIdAndOwner(any(), any()) } returns Optional.of(client)
        every { transactionRepository.save(any()) } returns transaction
        every { userRepository.findAll() } returns listOf(owner)
    }

    @Test
    fun transactionGetAllTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(API_TRANSACTION)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
    }

    @Test
    fun transactionPostTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(API_TRANSACTION)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(TRANSACTION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andReturn()
    }

    @Test
    fun transactionPutOldTransactionTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.put(API_TRANSACTION + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(TRANSACTION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andReturn()
    }

    @Test
    fun transactionPutNewTransactionTest() {
        every { transactionRepository.findByIdAndOwner(any(), any()) } returns
                Optional.of(transaction)
        mockMvc.perform(
                        MockMvcRequestBuilders.put(API_TRANSACTION + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(TRANSACTION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andReturn()
    }

    @Test
    fun transactionGetOneNotFoundTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(API_TRANSACTION + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andReturn()
    }

    @Test
    fun transactionGetOneFoundTest() {
        every { transactionRepository.findByIdAndOwner(any(), any()) } returns
                Optional.of(transaction)
        mockMvc.perform(
                        MockMvcRequestBuilders.get(API_TRANSACTION + "/{id}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
    }

    @Test
    fun transactionDeleteTest() {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_TRANSACTION + "/{id}", UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNoContent)
                .andReturn()
    }

    companion object {
        const val API_TRANSACTION = "/api/transaction"
        const val TRANSACTION_JSON =
                "{\"name\": \"Viagem\",\"transactionDate\": \"2020-01-10 23:57:22\",\"value\": 10.22,\"currency\": " +
                        "\"BRL\",\"client\": \"962a9e4e-0d33-45cb-8a70-d8116a7070d3\",\"account\": \"962a9e4e-0d33-45cb" +
                        "-8a70-d8116a6960d3\",\"card\": \"962a9e4e-0d33-45cb-8a70-d8116a7070f3\"}"
    }
}
