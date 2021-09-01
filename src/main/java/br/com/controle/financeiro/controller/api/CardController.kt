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
import br.com.controle.financeiro.model.entity.Card
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
@RequestMapping(value = ["/api/card"], produces = [MediaType.APPLICATION_JSON_VALUE])
class CardController(
    private val cardRepository: CardRepository, private val cardDTOResourceAssembler: CardDTOResourceAssembler,
    private val clientRepository: ClientRepository, private val institutionRepository: InstitutionRepository,
    private val userService: UserService
) {
    @GetMapping
    fun allCards(): CollectionModel<EntityModel<CardDTO>> {
        LOG.debug("finding allCards")
        val owner = userService.authenticatedUser
        val cards = cardRepository.findAllByOwner(owner).stream().map { card: Card? -> CardDTO.fromCard(card) }
            .map { entity: CardDTO -> cardDTOResourceAssembler.toModel(entity) }
            .collect(Collectors.toList())
        return CollectionModel(
            cards, WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                    CardController::class.java
                ).allCards()
            ).withSelfRel()
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun newCard(@RequestBody card: @Valid CardDTO?): EntityModel<CardDTO> {
        LOG.debug("creating newCard")
        //TODO extract to service
        val owner = userService.authenticatedUser
        val client = clientRepository.findByIdAndOwner(card?.client, owner).orElseThrow()
        val institution = card?.institution?.let { institutionRepository.findById(it).orElseThrow() }!!
        val savedCard: CardDTO = CardDTO.fromCard(
            cardRepository.save(
                card.toCard(client, institution, owner)
            )
        )
        return cardDTOResourceAssembler.toModel(savedCard)
    }

    @GetMapping(path = ["/{id}"])
    fun oneCard(@PathVariable(value = "id") id: UUID?): EntityModel<CardDTO> {
        LOG.debug("searching oneCard \${}", id)
        val owner = userService.authenticatedUser
        val savedCard = cardRepository.findByIdAndOwner(id, owner).orElseThrow { CardNotFoundException(id) }
        return cardDTOResourceAssembler.toModel(CardDTO.fromCard(savedCard))
    }

    @PutMapping(path = ["/{id}"])
    fun replaceCard(@RequestBody newCard: CardDTO, @PathVariable id: UUID?): EntityModel<CardDTO> {
        LOG.info("replaceCard")
        //TODO verify DTO integrity
        val owner = userService.authenticatedUser
        val savedCard = cardRepository.findByIdAndOwner(id, owner).map { card: Card ->
            card.name = newCard.name
            cardRepository.save(card)
        }.orElseGet {

            //TODO extract to service
            val c = clientRepository.findByIdAndOwner(newCard.client, owner).orElseThrow()
            val i = institutionRepository.findById(newCard.institution).orElseThrow()!!
            newCard.id = id
            cardRepository.save(newCard.toCard(c, i, owner))
        }
        return cardDTOResourceAssembler.toModel(CardDTO.fromCard(savedCard))
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = ["/{id}"])
    fun deleteCard(@PathVariable id: UUID) {
        LOG.debug("trying to deleteCard \${}", id)
        //TODO verify authenticated permission
        cardRepository.deleteById(id)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CardController::class.java)
    }
}