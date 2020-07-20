package br.com.controle.financeiro.component;

import br.com.controle.financeiro.service.exception.FirebaseTokenInvalidException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import br.com.controle.financeiro.configuration.auth.firebase.FirebaseTokenHolder;

public class FirebaseParser {
	public FirebaseTokenHolder parseToken(String idToken) {
		if (idToken.isBlank()) {
			throw new IllegalArgumentException("FirebaseTokenBlank");
		}
		try {
			FirebaseToken customToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

			return new FirebaseTokenHolder(customToken);
		} catch (Exception e) {
			throw new FirebaseTokenInvalidException(e.getMessage());
		}
	}
}