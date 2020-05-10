package br.com.controle.financeiro.model.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.controle.financeiro.model.entity.Client;

public class ClientDTOTest {

    @Test
    public void testfromClientShouldReturnAValidClientDTO() throws Exception {
        ClientDTO client;
        Client mock = new Client("client");
        mock.setId(1L);

        client = ClientDTO.fromClient(mock);

        assertEquals(mock.getId(), client.getId());
        assertEquals(mock.getName(), client.getName());
    }

    @Test
    public void testToClientShouldReturnAValidClient() throws Exception {
        Client client;
        ClientDTO mock = new ClientDTO("client");
        mock.setId(1L);

        client = mock.toClient();

        assertEquals(mock.getId(), client.getId());
        assertEquals(mock.getName(), client.getName());
    }
}
