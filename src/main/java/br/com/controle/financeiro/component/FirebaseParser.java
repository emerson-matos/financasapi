package br.com.controle.financeiro.component;

import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import br.com.controle.financeiro.configuration.auth.firebase.FirebaseTokenHolder;

public class FirebaseParser {
	public FirebaseTokenHolder parseToken(String idToken) {
		if (idToken.isEmpty()) {
			throw new IllegalArgumentException("FirebaseTokenBlank");
		}
		try {
			FirebaseToken customToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

			return new FirebaseTokenHolder(customToken);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
}