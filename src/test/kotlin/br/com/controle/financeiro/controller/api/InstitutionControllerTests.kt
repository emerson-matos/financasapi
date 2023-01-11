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
@SpringBootTest(classes = [ControleFinanceiroApplication::class, InstitutionDTOResourceAssembler::class])
@AutoConfigureMockMvc
@ActiveProfiles(profiles = ["test"])
@Import(
    RestResponseEntityExceptionHandler::class
)
@WithMockUser(value = "someone")
class InstitutionControllerTests : BaseModelTemplate() {
    @Autowired
    private val mockMvc: MockMvc? = null

    @MockBean
    private val institutionRepository: InstitutionRepository? = null
    @Before
    fun setup() {
        setupModel()
        Mockito.`when`<Any>(institutionRepository!!.save(ArgumentMatchers.any())).thenReturn(institution)
    }

    @Test
    @Throws(Exception::class)
    fun institutionGetAllTest() {
        mockMvc!!.perform(MockMvcRequestBuilders.get(API_INSTITUTION).accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(MockMvcResultMatchers.status().isOk).andReturn()
    }

    @Test
    @Throws(Exception::class)
    fun institutionPostTest() {
        mockMvc!!.perform(
            MockMvcRequestBuilders.post(API_INSTITUTION).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(INSTITUTION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated).andReturn()
    }

    @Test
    @Throws(Exception::class)
    fun institutionPutOldInstitutionTest() {
        mockMvc!!.perform(
            MockMvcRequestBuilders.put(API_INSTITUTION + "/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(INSTITUTION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful).andReturn()
    }

    @Test
    @Throws(Exception::class)
    fun institutionPutNewInstitutionTest() {
        Mockito.`when`(institutionRepository!!.findById(ArgumentMatchers.any())).thenReturn(Optional.of(institution))
        mockMvc!!.perform(
            MockMvcRequestBuilders.put(API_INSTITUTION + "/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(INSTITUTION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful).andReturn()
    }

    @Test
    @Throws(Exception::class)
    fun institutionGetOneNotFoundTest() {
        mockMvc!!.perform(MockMvcRequestBuilders.get(API_INSTITUTION + "/{id}", UUID.randomUUID()))
            .andExpect(MockMvcResultMatchers.status().isNotFound).andReturn()
    }

    @Test
    @Throws(Exception::class)
    fun institutionGetOneFoundTest() {
        Mockito.`when`(institutionRepository!!.findById(ArgumentMatchers.any())).thenReturn(Optional.of(institution))
        mockMvc!!.perform(MockMvcRequestBuilders.get(API_INSTITUTION + "/{id}", UUID.randomUUID()))
            .andExpect(MockMvcResultMatchers.status().isOk).andReturn()
    }

    @Test
    @Throws(Exception::class)
    fun institutionDeleteTest() {
        mockMvc!!.perform(MockMvcRequestBuilders.delete(API_INSTITUTION + "/{id}", UUID.randomUUID()))
            .andExpect(MockMvcResultMatchers.status().isNoContent).andReturn()
    }

    companion object {
        const val API_INSTITUTION = "/api/institution"
        const val INSTITUTION_JSON = "{\"name\":\"institution\", \"identifier\": \"bank\"}"
    }
}