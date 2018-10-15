package com.hercat.security.interceptor.interceptor

import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

// define auth type
const val AUTH_TYPE = "Basic"

// define a user: username is request and password is admin123
const val AUTH_USER = "request:admin123"

class SecurityInterceptor: HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authorization = request.getHeader("authorization")
        return if (!verifyAuthorization(authorization)) {
            // authorization recognize failed
            response.sendError(401, "Not Authorized")
            false
        } else {
            super.preHandle(request, response, handler)
        }
    }

    private fun verifyAuthorization(authorization: String): Boolean {
        val wellAuthInfo = Base64.getEncoder().encodeToString(AUTH_USER.toByteArray())
        return when {
            (authorization.isBlank()) -> false // authorization is blank
            (authorization.split(" ").size != 2) -> false // authorization is invalid
            (authorization.split(" ")[0] != AUTH_TYPE) -> false // auth type is not accepted
            (authorization.split(" ")[1] != wellAuthInfo) -> false // auth is not well
            else -> true
        }
    }
    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        super.postHandle(request, response, handler, modelAndView)
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        super.afterCompletion(request, response, handler, ex)
    }
}