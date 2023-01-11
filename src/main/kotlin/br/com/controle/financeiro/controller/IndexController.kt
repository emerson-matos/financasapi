package br.com.controle.financeiro.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.stereotype.Controller

@Controller
class IndexController {
    @GetMapping("/")
    fun greetings(): ModelAndView {
        return ModelAndView("index")
    }
}
