package br.com.controle.financeiro.controller

import br.com.controle.financeiro.ControleFinanceiroApplication

@SpringBootTest(classes = [ControleFinanceiroApplication::class])
@AutoConfigureMockMvc
@ActiveProfiles(profiles = ["test"])
class IndexControllerTest {
    @Autowired
    private val wac: WebApplicationContext? = null

    @Autowired
    private var mockMvc: MockMvc? = null
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac!!).build()
    }

    @Test
    @Throws(Exception::class)
    fun greetingsOnBaseUrl() {
        mockMvc!!.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("index"))
    }
}
