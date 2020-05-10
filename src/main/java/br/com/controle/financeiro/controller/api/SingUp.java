package br.com.controle.financeiro.controller.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SingUp {
    private static final Logger LOG = LoggerFactory.getLogger(SingUp.class);

    @PostMapping("/signup")
    public String signUp(@RequestHeader String token) throws FirebaseAuthException {
        LOG.info("SignUp request for {}", token);
        // Load the service account key JSON file
        // FileInputStream serviceAccount = new
        // FileInputStream("path/to/serviceAccountKey.json");
        String uid = null;
        // Authenticate a Google credential with the service account
        GoogleCredential googleCred;
        try {
            // Add the required scopes to the Google credential
            googleCred = GoogleCredential.getApplicationDefault();
            // Use the Google credential to generate an access token
            GoogleCredential scoped = googleCred
                    .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.database",
                            "https://www.googleapis.com/auth/userinfo.email"));
            scoped.refreshToken();
            uid = scoped.getAccessToken();
            LOG.info("TOKEN: {}", uid);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        // .createCustomToken(uid));
        // TODO(register)

        return uid;
    }
}