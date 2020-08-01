package br.com.controle.financeiro.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseTokenHolder;
import br.com.controle.financeiro.service.FirebaseParserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FirebaseServiceImplTest {

    @InjectMocks
    private FirebaseServiceImpl service;

    @Mock
    private FirebaseParserService parseService;


    @Test
    public void parseEmptyToken() {
        FirebaseTokenHolder tokenMock = mock(FirebaseTokenHolder.class);

        doReturn(tokenMock).when(parseService).parseToken("");

        service.parseToken("");
        verify(parseService, times(1)).parseToken(anyString());
    }

}