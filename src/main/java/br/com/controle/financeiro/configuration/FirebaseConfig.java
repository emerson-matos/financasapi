package br.com.controle.financeiro.configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import br.com.controle.financeiro.spring.conditionals.FirebaseCondition;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AbstractFirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
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

    @Value("${br.com.controle.financeiro.firebase.credentials:}")
    private String credentials;

    @Bean
    public AbstractFirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Bean
    public DatabaseReference firebaseDatabse() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @PostConstruct
    public void init() {
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                LOG.debug("Starting Firebase");
                GoogleCredentials googleCredential = getGoogleCredentials();
                FirebaseOptions options = new FirebaseOptions.Builder()//
                                                                       .setCredentials(googleCredential)//
                                                                       .setDatabaseUrl(databaseUrl).build();
                FirebaseApp.initializeApp(options);
                LOG.debug("Started Firebase with {}", databaseUrl);
            } catch (IOException e) {
                LOG.error("Error stating Firebase", e);
                FirebaseApp.initializeApp();
                LOG.debug("Started Firebase after an Exception");
            }
        }
    }

    private GoogleCredentials getGoogleCredentials() throws IOException {
        try {
             if (!credentials.isEmpty()) {
                String json = new JSONParser(credentials).parse().toString();
                return GoogleCredentials.fromStream(new ByteArrayInputStream(json.getBytes()));
            }
        } catch (ParseException e) {
            LOG.error(
                    "Erro on parse google credentials, using default credentials based on env " +
                            "$GOOGLE_APPLICATION_CREDENTIALS={}",
                    System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
        }
        return GoogleCredentials.getApplicationDefault();
    }

}