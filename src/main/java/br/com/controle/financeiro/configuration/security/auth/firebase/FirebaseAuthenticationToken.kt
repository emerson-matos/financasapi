package br.com.controle.financeiro.configuration.security.auth.firebase

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class FirebaseAuthenticationToken : AbstractAuthenticationToken {
    private val principal: Any?
    private var credentials: Any?

    /**
     * This constructor can be safely used by any code that wishes to create a
     * `UsernamePasswordAuthenticationToken`, as the
     * [.isAuthenticated] will return `false`.
     *
     */
    constructor(principal: Any?, credentials: Any?) : super(null) {
        this.principal = principal
        this.credentials = credentials
        isAuthenticated = false
    }

    /**
     * This constructor should only be used by
     * `AuthenticationManager` or `AuthenticationProvider`
     * implementations that are satisfied with producing a trusted (i.e.
     * [.isAuthenticated] = `true`) authentication token.
     *
     */
    constructor(
        principal: Any?, credentials: Any?,
        authorities: Collection<GrantedAuthority?>?
    ) : super(authorities) {
        this.principal = principal
        this.credentials = credentials
        super.setAuthenticated(true) // must use super, as we override
    }

    // ~ Methods
    // ========================================================================================================
    override fun getCredentials(): Any? {
        return credentials
    }

    override fun getPrincipal(): Any? {
        return principal
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        require(!isAuthenticated) { "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead" }
        super.setAuthenticated(false)
    }

    override fun eraseCredentials() {
        super.eraseCredentials()
        credentials = null
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o !is FirebaseAuthenticationToken) {
            return false
        }
        return EqualsBuilder() //
            .appendSuper(super.equals(o)) //
            .append(getPrincipal(), o.getPrincipal()) //
            .append(getCredentials(), o.getCredentials()) //
            .isEquals
    }

    override fun hashCode(): Int {
        return HashCodeBuilder(17, 37) //
            .appendSuper(super.hashCode()) //
            .append(getPrincipal()) //
            .append(getCredentials()) //
            .toHashCode()
    }

    companion object {
        private const val serialVersionUID = -1869548136546750302L
    }
}