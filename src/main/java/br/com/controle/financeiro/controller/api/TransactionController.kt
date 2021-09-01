package br.com.controle.financeiro.controller.api

import java.util.UUID
import br.com.controle.financeiro.model.entity.Institution
import br.com.controle.financeiro.model.entity.UserEntity
import br.com.controle.financeiro.model.dto.CardDTO
import com.fasterxml.jackson.annotation.JsonProperty
import br.com.controle.financeiro.model.dto.ClientDTO
import br.com.controle.financeiro.model.entity.BankAccount
import br.com.controle.financeiro.model.dto.BankAccountDTO
import br.com.controle.financeiro.model.dto.InstitutionDTO
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import java.math.BigDecimal
import java.util.Locale
import br.com.controle.financeiro.model.dto.TransactionDTO
import org.springframework.data.jpa.domain.AbstractPersistable
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import org.springframework.security.core.GrantedAuthority
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.ManyToMany
import java.lang.RuntimeException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import br.com.controle.financeiro.spring.conditionals.FirebaseConditionImpl
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata
import br.com.controle.financeiro.service.impl.UserServiceImpl
import br.com.controle.financeiro.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import br.com.controle.financeiro.model.repository.UserRepository
import br.com.controle.financeiro.model.repository.RoleRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.HashSet
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.access.annotation.Secured
import br.com.controle.financeiro.configuration.security.SecurityConfig.Roles
import br.com.controle.financeiro.service.UserService.RegisterUserInit
import org.springframework.security.core.context.SecurityContextHolder
import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseAuthenticationToken
import br.com.controle.financeiro.spring.conditionals.FirebaseCondition
import br.com.controle.financeiro.service.FirebaseParserService
import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseTokenHolder
import com.google.firebase.auth.AbstractFirebaseAuth
import java.lang.IllegalArgumentException
import com.google.firebase.auth.FirebaseToken
import br.com.controle.financeiro.service.exception.FirebaseTokenInvalidException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import br.com.controle.financeiro.controller.api.CardController
import br.com.controle.financeiro.controller.api.ClientController
import br.com.controle.financeiro.controller.api.BankAccountController
import br.com.controle.financeiro.controller.api.InstitutionController
import br.com.controle.financeiro.controller.api.TransactionController
import br.com.controle.financeiro.model.repository.CardRepository
import br.com.controle.financeiro.controller.api.linkbuilder.CardDTOResourceAssembler
import br.com.controle.financeiro.model.repository.ClientRepository
import br.com.controle.financeiro.model.repository.InstitutionRepository
import org.springframework.hateoas.CollectionModel
import java.util.stream.Collectors
import br.com.controle.financeiro.model.exception.CardNotFoundException
import br.com.controle.financeiro.controller.api.linkbuilder.ClientDTOResourceAssembler
import br.com.controle.financeiro.model.exception.ClientNotFoundException
import br.com.controle.financeiro.model.repository.BankAccountRepository
import br.com.controle.financeiro.controller.api.linkbuilder.BankAccountDTOResourceAssembler
import br.com.controle.financeiro.model.exception.BankAccountNotFoundException
import br.com.controle.financeiro.controller.api.linkbuilder.InstitutionDTOResourceAssembler
import br.com.controle.financeiro.model.exception.InstitutionNotFoundException
import br.com.controle.financeiro.model.repository.TransactionRepository
import br.com.controle.financeiro.controller.api.linkbuilder.TransactionDTOResourceAssembler
import br.com.controle.financeiro.model.exception.TransactionNotFoundException
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.http.ResponseEntity
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.hateoas.client.LinkDiscoverers
import org.springframework.hateoas.client.LinkDiscoverer
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer
import org.springframework.plugin.core.SimplePluginRegistry
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.spi.DocumentationType
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.SecurityReference
import springfox.documentation.service.ApiKey
import org.springframework.web.filter.OncePerRequestFilter
import kotlin.Throws
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.FilterChain
import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseFilter
import java.lang.SecurityException
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.apache.commons.lang3.builder.HashCodeBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.FirebaseApp
import br.com.controle.financeiro.configuration.security.FirebaseConfig
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseOptions
import java.io.ByteArrayInputStream
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import br.com.controle.financeiro.configuration.security.SecurityConfig.ApplicationSecurity
import br.com.controle.financeiro.model.entity.Transaction
import org.slf4j.LoggerFactory
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.boot.autoconfigure.SpringBootApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import kotlin.jvm.JvmStatic
import org.springframework.boot.SpringApplication
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.function.Function
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/api/transaction"], produces = [MediaType.APPLICATION_JSON_VALUE])
class TransactionController(
    private val accountRepository: BankAccountRepository, private val cardRepository: CardRepository,
    private val clientRepository: ClientRepository,
    private val transactionRepository: TransactionRepository, private val userService: UserService,
    private val transactionDTOResourceAssembler: TransactionDTOResourceAssembler
) {
    @GetMapping
    fun allTransactions(): CollectionModel<EntityModel<TransactionDTO>> {
        LOG.debug("finding allTransactions")
        val owner = userService.authenticatedUser
        val transactions = transactionRepository.findAllByOwner(owner).stream().map { t: Transaction? ->
            TransactionDTO.fromTransaction(
                t
            )
        }
            .map { entity: TransactionDTO -> transactionDTOResourceAssembler.toModel(entity) }
            .collect(Collectors.toList())
        return CollectionModel(
            transactions,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TransactionController::class.java).allTransactions())
                .withSelfRel()
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun newTransaction(@RequestBody transaction: @Valid TransactionDTO?): EntityModel<TransactionDTO> {
        LOG.debug("creating newTransaction")
        //TODO deal when an transaction doesn't belong to a card and a bank account simultaneously
        //TODO extract to service
        val owner = userService.authenticatedUser
        val client = clientRepository.findByIdAndOwner(transaction?.client, owner).orElseThrow()
        val account = accountRepository.findByIdAndOwner(transaction?.account, owner).orElseThrow()
        val card = cardRepository.findByIdAndOwner(transaction?.card, owner).orElseThrow()
        val savedTransaction: TransactionDTO = TransactionDTO.fromTransaction(
            transactionRepository.save(
                transaction!!.toTransaction(client, account, card, owner)
            )
        )
        return transactionDTOResourceAssembler.toModel(savedTransaction)
    }

    @GetMapping(path = ["/{id}"])
    fun oneTransaction(@PathVariable(value = "id") id: UUID?): EntityModel<TransactionDTO> {
        LOG.debug("searching oneTransaction \${}", id)
        val owner = userService.authenticatedUser
        val transaction =
            transactionRepository.findByIdAndOwner(id, owner).orElseThrow { TransactionNotFoundException(id) }
        return transactionDTOResourceAssembler.toModel(TransactionDTO.fromTransaction(transaction))
    }

    @PutMapping(path = ["/{id}"])
    fun replaceTransaction(
        @RequestBody newTransaction: TransactionDTO,
        @PathVariable id: UUID?
    ): EntityModel<TransactionDTO> {
        LOG.info("replaceTransaction")
        //TODO verify DTO integrity
        val owner = userService.authenticatedUser
        val savedTransaction = transactionRepository.findByIdAndOwner(id, owner).map { transaction: Transaction ->
            transaction.name = newTransaction.name
            transactionRepository.save(transaction)
        }.orElseGet {

            //TODO extract to service
            val client = clientRepository.findByIdAndOwner(newTransaction.client, owner).orElseThrow()
            val account = accountRepository.findByIdAndOwner(newTransaction.account, owner).orElseThrow()
            val card = cardRepository.findByIdAndOwner(newTransaction.card, owner).orElseThrow()
            newTransaction.id = id
            transactionRepository.save(newTransaction.toTransaction(client, account, card, owner))
        }
        return transactionDTOResourceAssembler.toModel(TransactionDTO.fromTransaction(savedTransaction))
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = ["/{id}"])
    fun deleteTransaction(@PathVariable id: UUID) {
        LOG.debug("trying to deleteTransaction \${}", id)
        //TODO verify authenticated permission
        transactionRepository.deleteById(id)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(TransactionController::class.java)
    }
}