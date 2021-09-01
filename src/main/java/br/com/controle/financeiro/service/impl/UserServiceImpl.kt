package br.com.controle.financeiro.service.impl

import br.com.controle.financeiro.configuration.security.SecurityConfig.Roles
import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseAuthenticationToken
import br.com.controle.financeiro.model.entity.Role
import br.com.controle.financeiro.model.entity.UserEntity
import br.com.controle.financeiro.model.repository.RoleRepository
import br.com.controle.financeiro.model.repository.UserRepository
import br.com.controle.financeiro.service.UserService
import br.com.controle.financeiro.service.UserService.RegisterUserInit
import br.com.controle.financeiro.service.impl.UserServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service(value = UserServiceImpl.NAME)
class UserServiceImpl : UserService {
    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val roleRepository: RoleRepository? = null
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository!!.findById(username)
        if (user.isEmpty) {
            throw UsernameNotFoundException("Bad credentials")
        }
        val userDetails = user.get()
        val grantedAuthorities: MutableSet<GrantedAuthority> = HashSet()
        for (role in userDetails.authorities) {
            grantedAuthorities.add(SimpleGrantedAuthority(role!!.authority))
        }
        return User(userDetails.username, userDetails.password, userDetails.authorities)
    }

    @Transactional
    @Secured(value = [Roles.ROLE_ANONYMOUS])
    override fun registerUser(init: RegisterUserInit): UserEntity? {
        val userLoaded = init.userName?.let { userRepository!!.findById(it) }
        return userLoaded?.let {
            if (userLoaded.isEmpty) {
                val userEntity = UserEntity()
                userEntity.name = init.name
                userEntity.email = init.email
                userEntity.id = init.userName
                userEntity.setAuthorities(userRoles)
                // TODO firebase users should not be able to login via username and
                // password so for now generation of password is OK
                userEntity.setPassword(UUID.randomUUID().toString())
                userRepository?.save(userEntity)
                logger.info("registerUser -> user created")
                userEntity
            } else {
                logger.info("registerUser -> user exists")
                userLoaded.get()
            }
        }
    }

    override val authenticatedUser: UserEntity?
        get() {
            val user: String
            val auth = SecurityContextHolder.getContext().authentication
            if (auth is FirebaseAuthenticationToken) {
                user = (auth.principal as UserDetails).username.toString()
                return userRepository!!.findById(user).orElseThrow()
            }
            throw UsernameNotFoundException("Bad credentials")
        }
    private val userRoles: List<Role>
        private get() = listOf(getRole(Roles.ROLE_USER))

    private fun getRole(authority: String): Role {
        val adminRole = roleRepository?.findByAuthority(authority)
        return adminRole ?: Role(authority)
    }

    companion object {
        const val NAME = "UserService"
        private val logger = LoggerFactory.getLogger(UserServiceImpl::class.java)
    }
}