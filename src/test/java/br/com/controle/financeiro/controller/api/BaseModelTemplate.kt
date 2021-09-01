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
import java.math.BigDecimal
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import br.com.controle.financeiro.ControleFinanceiroApplication
import br.com.controle.financeiro.configuration.security.SecurityConfig
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
import br.com.controle.financeiro.model.entity.*
import org.springframework.web.context.WebApplicationContext
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDateTime
import java.util.*

open class BaseModelTemplate {
    var bankAccount: BankAccount? = null
    var card: Card? = null
    var client: Client? = null
    var institution: Institution? = null
    var transaction: Transaction? = null
    var owner: UserEntity? = null
    fun setupModel() {
        setupOwner()
        setupInstitution()
        setupClient()
        setupCard()
        setupBankAccount()
        setupTransaction()
    }

    private fun setupTransaction() {
        transaction = Transaction(
            UUID.randomUUID(), "name", LocalDateTime.now(), BigDecimal(1),
            Currency.getInstance("BRL"), Client(), BankAccount(), Card(), owner
        )
    }

    private fun setupInstitution() {
        institution = Institution(UUID.randomUUID(), "identifier", "name")
    }

    private fun setupCard() {
        card = Card(UUID.randomUUID(), "card", "5432", client, institution, owner)
    }

    private fun setupClient() {
        client = Client(UUID.randomUUID(), "name", owner)
    }

    private fun setupOwner() {
        owner = UserEntity()
        owner!!.name = "owner"
        owner!!.setPassword(BCryptPasswordEncoder().encode("owner"))
        owner!!.email = "some@one.com"
        owner!!.id = UUID.randomUUID().toString()
        owner!!.setAuthorities(listOf(Role(SecurityConfig.Roles.ROLE_USER)))
    }

    private fun setupBankAccount() {
        bankAccount = BankAccount(UUID.randomUUID(), "5432", "543", "8", client, institution, owner)
    }
}