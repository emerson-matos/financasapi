package br.com.controle.financeiro.configuration.security.auth.firebase

import br.com.controle.financeiro.service.FirebaseService
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FirebaseFilter(private val firebaseService: FirebaseService?) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val xAuth = request.getHeader(HEADER_NAME)
        xAuth?.let {
            if (xAuth.isNotEmpty()) {
                try {
                    val holder = firebaseService?.parseToken(xAuth)
                    holder?.uid.let {
                        val auth: Authentication = FirebaseAuthenticationToken(it!!, holder!!)
                        SecurityContextHolder.getContext().authentication = auth
                    }
                } catch (e: IllegalArgumentException) {
                    throw SecurityException(e)
                }
            }
            filterChain.doFilter(request, response)
        }
    }

    companion object {
        private const val HEADER_NAME = "X-Authorization-Firebase"
    }
}