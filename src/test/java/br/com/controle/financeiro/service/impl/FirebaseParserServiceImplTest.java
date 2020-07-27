package br.com.controle.financeiro.service.impl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import br.com.controle.financeiro.service.exception.FirebaseTokenInvalidException;
import com.google.firebase.auth.AbstractFirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FirebaseParserServiceImplTest {

    @InjectMocks
    private FirebaseParserServiceImpl service;

    @Mock
    private AbstractFirebaseAuth firebaseAuth;

    @Test(expected = IllegalArgumentException.class)
    public void parseBlankToken() {
        service.parseToken("");
    }

    @Test(expected = FirebaseTokenInvalidException.class)
    public void parseNotBlankToken() throws FirebaseAuthException {
        doThrow(mock(FirebaseAuthException.class)).when(firebaseAuth).verifyIdToken(anyString());
        service.parseToken("123456");
    }

}