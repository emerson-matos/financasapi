package br.com.controle.financeiro.controller.api.linkbuilder

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.beans.factory.annotation.Autowired
import br.com.controle.financeiro.controller.api.ins
import br.com.controle.financeiro.model.dto.InstitutionDTO

@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class InstitutionDTOResourceAssemblerTest (
    @Autowired private val resourceAssembler: InstitutionDTOResourceAssembler
 ) {   @Test
    fun testToResource() {
        val institutionMock = InstitutionDTO(ins.name, ins.identifier, ins.id)
        val response = resourceAssembler.toModel(institutionMock)
        Assertions.assertTrue(response.hasLinks())
        Assertions.assertNotNull(response.getLink("self"))
        Assertions.assertNotNull(response.getLink("institutions"))
        Assertions.assertEquals(institutionMock, response.content)
    }
}
