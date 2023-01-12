package br.com.controle.financeiro.controller.api.linkbuilder

import org.springframework.test.context.ActiveProfiles
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import br.com.controle.financeiro.model.dto.ClientDTO
import br.com.controle.financeiro.controller.api.cl

@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class ClientDTOResourceAssemblerTest(
    @Autowired    private val resourceAssembler: ClientDTOResourceAssembler
) {
    @Test
    fun testToResource() {
        val clientMock = ClientDTO("mock", cl.owner, cl.id)
        val response = resourceAssembler.toModel(clientMock)
        Assertions.assertTrue(response.hasLinks())
        Assertions.assertNotNull(response.getLink("self"))
        Assertions.assertNotNull(response.getLink("clients"))
        Assertions.assertEquals(clientMock, response.content)
    }
}
