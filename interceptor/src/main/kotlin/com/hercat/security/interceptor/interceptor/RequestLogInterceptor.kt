package com.hercat.security.interceptor.interceptor

import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.logging.Logger
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RequestLogInterceptor: HandlerInterceptor {
    val logger = Logger.getLogger(this::class.simpleName)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val requestMethod = request.method
        val requestUri = request.requestURI
        val headers = request.headerNames.toList().map { headerName ->
            headerName to request.getHeader(headerName)
        }
        logger.info("request to $requestUri with ${requestMethod.toUpperCase()} method, headers: $headers")
        return super.preHandle(request, response, handler)
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        super.postHandle(request, response, handler, modelAndView)
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        super.afterCompletion(request, response, handler, ex)
    }
}