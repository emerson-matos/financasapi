package br.com.controle.financeiro.model.dto;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.UserEntity;

import org.junit.Test;

public class ClientDTOTest {

    @Test
    public void testfromClientShouldReturnAValidClientDTO() {
        ClientDTO client;
        Client mock = new Client(UUID.randomUUID(), "client", new UserEntity());

        client = ClientDTO.fromClient(mock);

        assertEquals(mock.getId(), client.getId());
        assertEquals(mock.getName(), client.getName());
    }

    @Test
    public void testToClientShouldReturnAValidClient() {
        Client client;
        ClientDTO mock = new ClientDTO();

        mock.setName("client");
        mock.setId(UUID.randomUUID());
        mock.setOwner("owner");
        client = mock.toClient(new UserEntity());

        assertEquals(mock.getId(), client.getId());
        assertEquals(mock.getName(), client.getName());
    }

}
