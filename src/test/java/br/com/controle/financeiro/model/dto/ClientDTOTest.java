package br.com.controle.financeiro.model.dto;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.UserEntity;

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
        UserEntity user = new UserEntity();
        user.setId(1L);
        mock.setId(UUID.randomUUID());
        mock.setName("client");
        mock.setOwner(user.getId());

        client = mock.toClient(user);

        assertEquals(mock.getId(), client.getId());
        assertEquals(mock.getName(), client.getName());
    }
}
