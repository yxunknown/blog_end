package com.hercats.dev.apigateway.filter

import com.hercats.dev.commonbase.redis.Redis
import com.hercats.dev.commonbase.tool.be
import com.hercats.dev.commonbase.tool.blank
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.logging.Logger
import javax.servlet.http.HttpServletResponse

@Component
class TokenVerifyFilter(@Autowired val redis: Redis): ZuulFilter() {
    private val logger = Logger.getLogger("${this::class.java.`package`.name}.${this::class.java.simpleName}")

    override fun run(): Any {
        val requestContext = RequestContext.getCurrentContext()
        // from query parameters
        val parameters = requestContext.requestQueryParams
        val headers = requestContext.request.headerNames.toList()
        val token = when {
            parameters.containsKey("token") -> {
                parameters["token"]?.first() ?: ""
            }
            headers.contains("authorization") -> {
                val auth = requestContext.request.getHeader("authorization")
                if(auth.startsWith("Bearer ")) {
                    auth.substringAfterLast(" ")
                } else ""
            }
            else ->  ""
        }
        if (token be blank ||
                !(redis exists token) ||
                (redis get token) be blank) {
            requestContext.set("error.status_code", HttpServletResponse.SC_UNAUTHORIZED)
            requestContext.set("error.exception", "Not Authorized")
            throw Exception("Not Authorized")
        }
        return Any()
    }

    override fun shouldFilter(): Boolean {
        val requestContext = RequestContext.getCurrentContext()
        val uri = requestContext.request.requestURI.toString()
        logger.info(uri)
        return false
//        return !uri.startsWith("api/login-service/token") && !uri.startsWith("api/login-service/hello")
    }

    override fun filterType(): String = "pre"

    override fun filterOrder(): Int = 7 //after all default zuul pre type filters
}