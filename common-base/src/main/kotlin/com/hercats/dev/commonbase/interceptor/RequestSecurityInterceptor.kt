package com.hercats.dev.commonbase.interceptor

import com.hercats.dev.commonbase.tool.be
import com.hercats.dev.commonbase.tool.blank
import com.hercats.dev.commonbase.tool.deBase64
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RequestSecurityInterceptor: HandlerInterceptor {
    private val AUTH_METHOD = "Basic"
    private val AUTH_USER = "servicecall:servicecall"

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authorization = request.getHeader("authorization")
        println("authorization: $authorization")
        if (authorization be blank) {
            sendAuthFailed(response)
            return false
        } else {
            val authInfo = authorization.split(" ")
            if (authInfo.size != 2) {
                sendAuthFailed(response)
                return false
            } else {
                val authMethod = authInfo[0]
                val authContent = authInfo[1]
                if (authMethod != AUTH_METHOD || deBase64(authContent) != AUTH_USER) {
                    sendAuthFailed(response)
                    return false
                }
            }
        }
        return super.preHandle(request, response, handler)
    }

    fun sendAuthFailed(response: HttpServletResponse) {
        response.status = 401
        response.sendError(401, "Not Authorized")
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        super.postHandle(request, response, handler, modelAndView)
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        super.afterCompletion(request, response, handler, ex)
    }
}