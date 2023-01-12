package br.com.controle.financeiro.controller.api

import br.com.controle.financeiro.controller.api.InstitutionController
import br.com.controle.financeiro.controller.api.linkbuilder.InstitutionDTOResourceAssembler
import br.com.controle.financeiro.model.dto.InstitutionDTO
import br.com.controle.financeiro.model.entity.Institution
import br.com.controle.financeiro.model.exception.InstitutionNotFoundException
import br.com.controle.financeiro.model.repository.InstitutionRepository
import org.slf4j.LoggerFactory
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping(value = ["/api/institution"], produces = [MediaType.APPLICATION_JSON_VALUE])
class InstitutionController(
    private val institutionRepository: InstitutionRepository,
    private val institutionDTOResourceAssembler: InstitutionDTOResourceAssembler
) {
    @GetMapping
    fun allInstitutions(): CollectionModel<EntityModel<InstitutionDTO>> {
        LOG.debug("finding allInstitutions")
        val institutions = institutionRepository.findAll().stream().map { institution: Institution ->
            InstitutionDTO.fromInstitution(
                institution
            )
        }
            .map { entity: InstitutionDTO -> institutionDTOResourceAssembler.toModel(entity) }
        .toList()
        return CollectionModel.of(
            institutions,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(InstitutionController::class.java).allInstitutions())
                .withSelfRel()
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun newInstitution(@RequestBody institution: @Valid InstitutionDTO?): EntityModel<InstitutionDTO> {
        LOG.debug("creating newInstitution")
        val savedInstitution: InstitutionDTO = InstitutionDTO.fromInstitution(
            institutionRepository.save(
                institution!!.toInstitution()
            )
        )
        return institutionDTOResourceAssembler.toModel(savedInstitution)
    }

    @GetMapping(path = ["/{id}"])
    fun oneInstitution(@PathVariable(value = "id") id: UUID): EntityModel<InstitutionDTO> {
        LOG.debug("searching oneInstitution \${}", id)
        val institution = institutionRepository.findById(id).orElseThrow { InstitutionNotFoundException(id) }!!
        return institutionDTOResourceAssembler.toModel(InstitutionDTO.fromInstitution(institution))
    }

    @PutMapping(path = ["/{id}"])
    fun replaceInstitution(
        @RequestBody newInstitution: InstitutionDTO,
        @PathVariable id: UUID
    ): EntityModel<InstitutionDTO> {
        LOG.info("replaceInstitution")
        //TODO verify DTO integrity
        val savedInstitution = institutionRepository.findById(id).map { inst: Institution ->
            institutionRepository.save(Institution(newInstitution.name, inst.identifier,inst.id))
        }.orElseGet {
            institutionRepository.save(newInstitution.toInstitution())
        }
        return institutionDTOResourceAssembler.toModel(InstitutionDTO.fromInstitution(savedInstitution))
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = ["/{id}"])
    fun deleteInstitution(@PathVariable id: UUID) {
        LOG.debug("trying to deleteInstitution \${}", id)
        //TODO verify authenticated permission
        institutionRepository.deleteById(id)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(InstitutionController::class.java)
    }
}
