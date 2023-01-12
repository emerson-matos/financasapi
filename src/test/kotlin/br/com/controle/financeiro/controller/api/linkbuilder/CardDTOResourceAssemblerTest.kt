package br.com.controle.financeiro.controller.api.linkbuilder

import org.springframework.test.context.ActiveProfiles
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import br.com.controle.financeiro.model.dto.CardDTO
import br.com.controle.financeiro.controller.api.ca
import java.util.UUID

@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class CardDTOResourceAssemblerTest(
    @Autowired private val resourceAssembler: CardDTOResourceAssembler
) {
    @Test
    fun testToResource() {
        val cardMock = CardDTO("mock", "number", ca.responsible, ca.institution)
        val response = resourceAssembler.toModel(cardMock)
        Assertions.assertTrue(response.hasLinks())
        Assertions.assertNotNull(response.getLink("self"))
        Assertions.assertNotNull(response.getLink("cards"))
        Assertions.assertEquals(cardMock, response.content)
    }
}
