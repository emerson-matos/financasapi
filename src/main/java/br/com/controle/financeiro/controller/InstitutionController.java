package br.com.controle.financeiro.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.controle.financeiro.controller.linkbuilder.InstitutionDTOResourceAssembler;
import br.com.controle.financeiro.model.dto.InstitutionDTO;
import br.com.controle.financeiro.model.entity.Institution;
import br.com.controle.financeiro.model.exception.InstitutionNotFoundException;
import br.com.controle.financeiro.model.repository.InstitutionRepository;

@RestController
@RequestMapping("/institution")
public class InstitutionController {

	private static final Logger LOG = LoggerFactory.getLogger(InstitutionController.class);

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private InstitutionDTOResourceAssembler institutionDTOResourceAssembler;

	@GetMapping
	public Resources<Resource<InstitutionDTO>> allInstitutions() {
		LOG.debug("finding allInstitutions");

		final List<Resource<InstitutionDTO>> institutions = institutionRepository.findAll().stream()
				.map(InstitutionDTO::fromInstitution).map(institutionDTOResourceAssembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(institutions,
				linkTo(methodOn(InstitutionController.class).allInstitutions()).withSelfRel());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public Resource<InstitutionDTO> newInstitution(@RequestBody final InstitutionDTO institution) {
		LOG.debug("creating newInstitution");
		InstitutionDTO savedInstitution = InstitutionDTO
				.fromInstitution(institutionRepository.save(institution.toInstitution()));
		return institutionDTOResourceAssembler.toResource(savedInstitution);
	}

	@GetMapping(path = "/{id}")
	public Resource<InstitutionDTO> oneInstitution(@PathVariable(value = "id") final long id) {
		LOG.debug("searching oneInstitution ${}", id);
		final Institution institution = institutionRepository.findById(id)
				.orElseThrow(() -> new InstitutionNotFoundException(id));
		return institutionDTOResourceAssembler.toResource(InstitutionDTO.fromInstitution(institution));
	}

	@PutMapping(path = "/{id}")
	public Resource<InstitutionDTO> replaceInstitution(@RequestBody final InstitutionDTO newInstitution,
			@PathVariable final Long id) {
		LOG.info("replaceInstitution");
		Institution savedInstitution = institutionRepository.findById(id).map(inst -> {
			inst.setName(newInstitution.getName());
			return institutionRepository.save(inst);
		}).orElseGet(() -> {
			newInstitution.setId(id);
			return institutionRepository.save(newInstitution.toInstitution());
		});

		return institutionDTOResourceAssembler.toResource(InstitutionDTO.fromInstitution(savedInstitution));
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}")
	public void deleteInstitution(@PathVariable final Long id) {
		LOG.debug("trying to deleteInstitution ${}", id);
		institutionRepository.deleteById(id);
	}
}
