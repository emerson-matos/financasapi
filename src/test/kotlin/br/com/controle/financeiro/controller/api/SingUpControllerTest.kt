package br.com.controle.financeiro.controller.api

import br.com.controle.financeiro.model.repository.UserRepository
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class SingUpControllerTest(@Autowired private val mockMvc: MockMvc) {
    @MockkBean private lateinit var userRepository: UserRepository

    @Test
    fun signUpWithBlackToken() {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/open/signup"))
                .andExpect(MockMvcResultMatchers.status().isNoContent)
    }
}
