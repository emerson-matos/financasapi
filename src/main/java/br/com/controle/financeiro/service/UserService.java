package br.com.controle.financeiro.service;

import br.com.controle.financeiro.model.entity.UserEntity;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserEntity registerUser(RegisterUserInit init);

    UserEntity getAuthenticatedUser();

    class RegisterUserInit {
        private final String userName;
        private final String email;
        private final String name;

        public RegisterUserInit(String userName, String email, String name) {
            super();
            this.userName = userName;
            this.email = email;
            this.name = name;
        }

        public String getUserName() {
            return userName;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

    }

}