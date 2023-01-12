package br.com.controle.financeiro.controller.api.linkbuilder

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import br.com.controle.financeiro.model.dto.TransactionDTO
import br.com.controle.financeiro.controller.api.tra

@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class TransactionDTOResourceAssemblerTest(
    @Autowired private val resourceAssembler: TransactionDTOResourceAssembler
) {
    @Test
    fun testToResource() {
        val transactionMock = TransactionDTO(tra.name, tra.transactionDate, tra.value, tra.currency, tra.responsible.id, tra.bankAccount.id, tra.card.id, tra.id)
        val response = resourceAssembler.toModel(transactionMock)
        Assertions.assertTrue(response.hasLinks())
        Assertions.assertNotNull(response.getLink("self"))
        Assertions.assertNotNull(response.getLink("transactions"))
        Assertions.assertEquals(transactionMock, response.content)
    }
}
