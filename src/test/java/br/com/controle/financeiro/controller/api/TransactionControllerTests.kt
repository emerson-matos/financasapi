package br.com.controle.financeiro.controller.api

import br.com.controle.financeiro.model.dto.ClientDTO.Companion.fromClient
import br.com.controle.financeiro.model.dto.ClientDTO.id
import br.com.controle.financeiro.model.entity.Client.name
import br.com.controle.financeiro.model.dto.ClientDTO.name
import br.com.controle.financeiro.model.dto.ClientDTO.owner
import br.com.controle.financeiro.model.dto.ClientDTO.toClient
import br.com.controle.financeiro.service.impl.UserServiceImpl.loadUserByUsername
import br.com.controle.financeiro.model.entity.UserEntity.getUsername
import br.com.controle.financeiro.model.entity.UserEntity.getPassword
import br.com.controle.financeiro.model.entity.UserEntity.getAuthorities
import br.com.controle.financeiro.service.impl.UserServiceImpl.registerUser
import br.com.controle.financeiro.model.entity.UserEntity.email
import br.com.controle.financeiro.service.FirebaseParserService.parseToken
import br.com.controle.financeiro.service.impl.FirebaseServiceImpl.parseToken
import br.com.controle.financeiro.service.impl.FirebaseParserServiceImpl.parseToken
import br.com.controle.financeiro.model.dto.CardDTO.id
import br.com.controle.financeiro.model.dto.CardDTO.name
import br.com.controle.financeiro.controller.api.linkbuilder.CardDTOResourceAssembler.toModel
import br.com.controle.financeiro.controller.api.linkbuilder.ClientDTOResourceAssembler.toModel
import br.com.controle.financeiro.model.dto.BankAccountDTO.id
import br.com.controle.financeiro.controller.api.linkbuilder.BankAccountDTOResourceAssembler.toModel
import br.com.controle.financeiro.model.dto.InstitutionDTO.id
import br.com.controle.financeiro.model.dto.InstitutionDTO.name
import br.com.controle.financeiro.controller.api.linkbuilder.InstitutionDTOResourceAssembler.toModel
import br.com.controle.financeiro.model.dto.TransactionDTO.id
import br.com.controle.financeiro.controller.api.linkbuilder.TransactionDTOResourceAssembler.toModel
import br.com.controle.financeiro.model.entity.UserEntity.name
import br.com.controle.financeiro.model.entity.UserEntity.setPassword
import br.com.controle.financeiro.model.entity.UserEntity.id
import br.com.controle.financeiro.model.entity.UserEntity.setAuthorities
import br.com.controle.financeiro.service.UserService.authenticatedUser
import br.com.controle.financeiro.model.repository.ClientRepository.findByIdAndOwner
import br.com.controle.financeiro.model.repository.CardRepository.findByIdAndOwner
import br.com.controle.financeiro.service.FirebaseService.parseToken
import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseTokenHolder.email
import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseTokenHolder.uid
import br.com.controle.financeiro.model.repository.BankAccountRepository.findByIdAndOwner
import br.com.controle.financeiro.model.repository.TransactionRepository.findByIdAndOwner
import br.com.controle.financeiro.model.dto.ClientDTO
import br.com.controle.financeiro.model.entity.UserEntity
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import org.mockito.InjectMocks
import br.com.controle.financeiro.service.impl.UserServiceImpl
import org.mockito.Mock
import br.com.controle.financeiro.model.repository.UserRepository
import br.com.controle.financeiro.model.repository.RoleRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.mockito.Mockito
import org.mockito.ArgumentMatchers
import org.springframework.security.core.userdetails.UserDetails
import br.com.controle.financeiro.service.UserService.RegisterUserInit
import org.springframework.security.core.GrantedAuthority
import br.com.controle.financeiro.service.impl.FirebaseServiceImpl
import br.com.controle.financeiro.service.FirebaseParserService
import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseTokenHolder
import br.com.controle.financeiro.service.impl.FirebaseParserServiceImpl
import com.google.firebase.auth.AbstractFirebaseAuth
import java.lang.IllegalArgumentException
import br.com.controle.financeiro.service.exception.FirebaseTokenInvalidException
import kotlin.Throws
import com.google.firebase.auth.FirebaseAuthException
import org.springframework.boot.test.context.SpringBootTest
import br.com.controle.financeiro.controller.api.linkbuilder.CardDTOResourceAssembler
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.context.ActiveProfiles
import br.com.controle.financeiro.model.dto.CardDTO
import org.springframework.hateoas.EntityModel
import br.com.controle.financeiro.controller.api.linkbuilder.ClientDTOResourceAssembler
import br.com.controle.financeiro.controller.api.linkbuilder.BankAccountDTOResourceAssembler
import br.com.controle.financeiro.model.dto.BankAccountDTO
import br.com.controle.financeiro.controller.api.linkbuilder.InstitutionDTOResourceAssembler
import br.com.controle.financeiro.model.dto.InstitutionDTO
import br.com.controle.financeiro.controller.api.linkbuilder.TransactionDTOResourceAssembler
import br.com.controle.financeiro.model.dto.TransactionDTO
import br.com.controle.financeiro.model.entity.BankAccount
import br.com.controle.financeiro.model.entity.Institution
import java.math.BigDecimal
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import br.com.controle.financeiro.ControleFinanceiroApplication
import br.com.controle.financeiro.controller.RestResponseEntityExceptionHandler
import org.springframework.security.test.context.support.WithMockUser
import br.com.controle.financeiro.controller.api.BaseModelTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.boot.test.mock.mockito.MockBean
import br.com.controle.financeiro.model.repository.CardRepository
import br.com.controle.financeiro.model.repository.ClientRepository
import br.com.controle.financeiro.model.repository.InstitutionRepository
import br.com.controle.financeiro.service.UserService
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import br.com.controle.financeiro.controller.api.CardControllerTests
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import br.com.controle.financeiro.controller.api.ClientControllerTests
import org.springframework.dao.EmptyResultDataAccessException
import br.com.controle.financeiro.model.repository.BankAccountRepository
import br.com.controle.financeiro.controller.api.BankAccountControllerTests
import br.com.controle.financeiro.controller.api.InstitutionControllerTests
import br.com.controle.financeiro.model.repository.TransactionRepository
import br.com.controle.financeiro.controller.api.TransactionControllerTests
import org.junit.Before
import org.junit.Test
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.web.context.WebApplicationContext
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.lang.Exception
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [ControleFinanceiroApplication::class, TransactionDTOResourceAssembler::class])
@AutoConfigureMockMvc
@ActiveProfiles(profiles = ["test"])
@Import(
    RestResponseEntityExceptionHandler::class
)
@WithMockUser(value = "someone")
class TransactionControllerTests : BaseModelTemplate() {
    @Autowired
    private val mockMvc: MockMvc? = null

    @MockBean
    private val accountRepository: BankAccountRepository? = null

    @MockBean
    private val cardRepository: CardRepository? = null

    @MockBean
    private val clientRepository: ClientRepository? = null

    @MockBean
    private val transactionRepository: TransactionRepository? = null

    @MockBean
    private val userService: UserService? = null
    @Before
    fun setup() {
        setupModel()
        Mockito.`when`(accountRepository!!.findByIdAndOwner(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(
            Optional.of(bankAccount)
        )
        Mockito.`when`(cardRepository!!.findByIdAndOwner(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(
            Optional.of(card)
        )
        Mockito.`when`(clientRepository!!.findByIdAndOwner(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(
            Optional.of(client)
        )
        Mockito.`when`<Any>(transactionRepository!!.save(ArgumentMatchers.any())).thenReturn(transaction)
        Mockito.`when`(userService!!.authenticatedUser).thenReturn(owner)
    }

    @Test
    @Throws(Exception::class)
    fun transactionGetAllTest() {
        mockMvc!!.perform(MockMvcRequestBuilders.get(API_TRANSACTION).accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(MockMvcResultMatchers.status().isOk).andReturn()
    }

    @Test
    @Throws(Exception::class)
    fun transactionPostTest() {
        mockMvc!!.perform(
            MockMvcRequestBuilders.post(API_TRANSACTION).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TRANSACTION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated).andReturn()
    }

    @Test
    @Throws(Exception::class)
    fun transactionPutOldTransactionTest() {
        mockMvc!!.perform(
            MockMvcRequestBuilders.put(API_TRANSACTION + "/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(TRANSACTION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful).andReturn()
    }

    @Test
    @Throws(Exception::class)
    fun transactionPutNewTransactionTest() {
        Mockito.`when`(transactionRepository!!.findByIdAndOwner(ArgumentMatchers.any(), ArgumentMatchers.any()))
            .thenReturn(
                Optional.of(transaction)
            )
        mockMvc!!.perform(
            MockMvcRequestBuilders.put(API_TRANSACTION + "/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(TRANSACTION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful).andReturn()
    }

    @Test
    @Throws(Exception::class)
    fun transactionGetOneNotFoundTest() {
        mockMvc!!.perform(
            MockMvcRequestBuilders.get(API_TRANSACTION + "/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound).andReturn()
    }

    @Test
    @Throws(Exception::class)
    fun transactionGetOneFoundTest() {
        Mockito.`when`(transactionRepository!!.findByIdAndOwner(ArgumentMatchers.any(), ArgumentMatchers.any()))
            .thenReturn(
                Optional.of(transaction)
            )
        mockMvc!!.perform(
            MockMvcRequestBuilders.get(API_TRANSACTION + "/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isOk).andReturn()
    }

    @Test
    @Throws(Exception::class)
    fun transactionDeleteTest() {
        mockMvc!!.perform(MockMvcRequestBuilders.delete(API_TRANSACTION + "/{id}", UUID.randomUUID()))
            .andExpect(MockMvcResultMatchers.status().isNoContent).andReturn()
    }

    companion object {
        const val API_TRANSACTION = "/api/transaction"
        const val TRANSACTION_JSON =
            "{\"name\": \"Viagem\",\"transactionDate\": \"2020-01-10 23:57:22\",\"value\": 10.22,\"currency\": " +
                    "\"BRL\",\"client\": \"962a9e4e-0d33-45cb-8a70-d8116a7070d3\",\"account\": \"962a9e4e-0d33-45cb" +
                    "-8a70-d8116a6960d3\",\"card\": \"962a9e4e-0d33-45cb-8a70-d8116a7070f3\"}"
    }
}