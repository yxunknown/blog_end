package com.hercats.dev.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy


@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {


    override fun configure(http: HttpSecurity) {
        //任何请求都需要basic验证
        http.httpBasic().and().authorizeRequests().anyRequest().fullyAuthenticated()
        //设置成无状态的请求
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        super.configure(http)
    }

}