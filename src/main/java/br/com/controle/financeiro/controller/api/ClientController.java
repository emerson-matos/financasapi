package br.com.controle.financeiro.controller.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseAuthenticationToken;
import br.com.controle.financeiro.controller.api.linkbuilder.ClientDTOResourceAssembler;
import br.com.controle.financeiro.model.dto.ClientDTO;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.UserEntity;
import br.com.controle.financeiro.model.exception.ClientNotFoundException;
import br.com.controle.financeiro.model.repository.ClientRepository;
import br.com.controle.financeiro.model.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/client", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ClientController {

    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);

    private final ClientRepository clientRepository;

    private final UserRepository userRepository;

    private final ClientDTOResourceAssembler clientDTOResourceAssembler;

    public ClientController(final ClientRepository clientRepository, final UserRepository userRepository,
                            final ClientDTOResourceAssembler clientDTOResourceAssembler) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.clientDTOResourceAssembler = clientDTOResourceAssembler;
    }

    @GetMapping
    public Resources<Resource<ClientDTO>> allClients() {
        LOG.debug("finding allClients");
        UserEntity owner = userRepository.findById(getUser()).get();
        final List<ClientDTO> clients =
                clientRepository.findAllByOwner(owner).stream().map(ClientDTO::fromClient).collect(Collectors.toList());
        List<Resource<ClientDTO>> cr =
                clients.stream().map(clientDTOResourceAssembler::toResource).collect(Collectors.toList());

        return new Resources<>(cr, linkTo(methodOn(ClientController.class).allClients()).withSelfRel());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public Resource<ClientDTO> newClient(@RequestBody @Valid final ClientDTO client) {
        LOG.debug("creating newClient");
        // TODO extract to service
        UserEntity owner = userRepository.findById(getUser()).get();
        client.setOwner(owner.getId());
        ClientDTO clientDTO = ClientDTO.fromClient(clientRepository.save(client.toClient(owner)));
        return clientDTOResourceAssembler.toResource(clientDTO);
    }

    @GetMapping(path = "/{id}")
    public Resource<ClientDTO> oneClient(@PathVariable(value = "id") final UUID id) {
        LOG.debug("searching oneClient {}", id);
        Client client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
        return clientDTOResourceAssembler.toResource(ClientDTO.fromClient(client));
    }

    @PutMapping(path = "/{id}")
    public Resource<ClientDTO> replaceClient(@RequestBody final ClientDTO newClient, @PathVariable final UUID id) {
        LOG.info("replaceClient");
        //TODO verify DTO integrity
        Client savedClient = clientRepository.findById(id).map(client -> {
            client.setName(newClient.getName());
            return clientRepository.save(client);
        }).orElseGet(() -> {
            newClient.setId(id);
            // TODO extract to service
            UserEntity owner = userRepository.findById(getUser()).get();
            return clientRepository.save(newClient.toClient(owner));
        });

        return clientDTOResourceAssembler.toResource(ClientDTO.fromClient(savedClient));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void deleteClient(@PathVariable final UUID id) {
        LOG.debug("trying to deleteClient {}", id);
        clientRepository.deleteById(id);
    }


    // TODO() extrair para um service
    public static String getUser() {
        String user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof FirebaseAuthenticationToken) {
            user = String.valueOf(((UserDetails) auth.getPrincipal()).getUsername());
        }
        return user;
    }

}
