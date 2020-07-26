package br.com.controle.financeiro.configuration;

import java.io.IOException;

import javax.annotation.PostConstruct;

import br.com.controle.financeiro.spring.conditionals.FirebaseCondition;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FirebaseCondition
public class FirebaseConfig {

    private static final Logger LOG = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${br.com.controle.financeiro.firebase.database.url}")
    private String databaseUrl;

    @Bean
    public DatabaseReference firebaseDatabse() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @PostConstruct
    public void init() {
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                LOG.debug("Starting Firebase");
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.getApplicationDefault()).setDatabaseUrl(databaseUrl).build();
                FirebaseApp.initializeApp(options);
                LOG.debug("Started Firebase with ${}", databaseUrl);
            } catch (IOException e) {
                LOG.error("Error stating Firebase", e);
                FirebaseApp.initializeApp();
                LOG.debug("Started Firebase after an Exception");
            }
        }
    }
}