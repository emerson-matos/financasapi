package br.com.controle.financeiro.controlefinanceiro

import br.com.controle.financeiro.model.dto.ClientDTO.Companion.fromClient
import br.com.controle.financeiro.model.dto.ClientDTO
import java.util.UUID
import br.com.controle.financeiro.model.entity.UserEntity
import org.springframework.test.context.junit4.SpringRunner
import org.mockito.InjectMocks
import br.com.controle.financeiro.service.impl.UserServiceImpl
import org.mockito.Mock
import br.com.controle.financeiro.model.repository.UserRepository
import br.com.controle.financeiro.model.repository.RoleRepository
import org.mockito.Mockito
import org.mockito.ArgumentMatchers
import br.com.controle.financeiro.service.UserService.RegisterUserInit
import java.lang.IllegalArgumentException
import kotlin.Throws
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
import br.com.controle.financeiro.ControleFinanceiroApplication
import br.com.controle.financeiro.controller.RestResponseEntityExceptionHandler
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
import org.junit.Test
import org.springframework.web.context.WebApplicationContext
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class ControleFinanceiroApplicationTests {
    @Test
    fun contextLoads() {
    }
}
