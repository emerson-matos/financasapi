package br.com.controle.financeiro.controller.api.linkbuilder

import br.com.controle.financeiro.controller.api.ba
import br.com.controle.financeiro.model.dto.BankAccountDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class BankAccountDTOResourceAssemblerTest(
        @Autowired private val resourceAssembler: BankAccountDTOResourceAssembler
) {

    @Test
    fun testToResource() {
        val bankAccountMock = BankAccountDTO(ba.agency, ba.number, ba.dac, ba.responsible, ba.institution)
        val response = resourceAssembler.toModel(bankAccountMock)
        Assertions.assertTrue(response.hasLinks())
        Assertions.assertEquals(bankAccountMock, response.content)
        Assertions.assertNotNull(response.getLink("self"))
        Assertions.assertNotNull(response.getLink("bankaccounts"))
    }
}
