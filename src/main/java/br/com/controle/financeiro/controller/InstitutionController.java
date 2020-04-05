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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.controle.financeiro.controller.linkbuilder.InstitutionResourceAssembler;
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
	private InstitutionResourceAssembler institutionResourceAssembler;

	@GetMapping
	public Resources<Resource<Institution>> allInstitutions() {
		LOG.debug("finding allInstitutions");

		final List<Resource<Institution>> institutions = institutionRepository.findAll().stream()
				.map(institutionResourceAssembler::toResource).collect(Collectors.toList());

		return new Resources<>(institutions, linkTo(methodOn(InstitutionController.class).allInstitutions()).withSelfRel());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Resource<Institution> newInstitution(@RequestBody final Institution institution) {
		LOG.debug("creating newInstitution");
		return institutionResourceAssembler.toResource(institutionRepository.save(institution));
	}

	@GetMapping(path = "/{id}")
	public Resource<Institution> oneInstitution(@PathVariable(value = "id") final long id) {
		LOG.debug("searching oneInstitution ${}", id);
		final Institution c = institutionRepository.findById(id).orElseThrow(() -> new InstitutionNotFoundException(id));
		return institutionResourceAssembler.toResource(c);
	}

	@PutMapping(path = "/{id}")
	public Institution replaceInstitution(@RequestBody final Institution newInstitution, @PathVariable final Long id) {
		LOG.info("replaceInstitution");
		return institutionRepository.findById(id).map(inst -> {
			inst.setName(newInstitution.getName());
			return institutionRepository.save(inst);
		}).orElseGet(() -> {
			newInstitution.setId(id);
			return institutionRepository.save(newInstitution);
		});
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}")
	public void deleteInstitution(@PathVariable final Long id) {
		LOG.debug("trying to deleteInstitution ${}", id);
		institutionRepository.deleteById(id);
	}
}
