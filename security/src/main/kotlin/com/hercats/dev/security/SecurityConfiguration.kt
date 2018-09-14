package com.hercats.dev.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.NoOpPasswordEncoder


@Configuration
@EnableWebSecurity
class SecurityConfiguration: WebSecurityConfigurerAdapter() {

    @Autowired
    override fun configure(auth: AuthenticationManagerBuilder?) {
        if (auth is AuthenticationManagerBuilder) {
            auth.inMemoryAuthentication()
                    .passwordEncoder(NoOpPasswordEncoder.getInstance())
                    .withUser("blog").password("blog").roles("SERVICE_CALL")
                    .and()
                    .withUser("test").password("test").roles("TEST")
        }
    }
    override fun configure(http: HttpSecurity?) {
        super.configure(http)
        if (http is HttpSecurity) {
            //任何请求都需要basic验证
            http.httpBasic().and().authorizeRequests().anyRequest().fullyAuthenticated()
            //设置成无状态的请求
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
    }

}