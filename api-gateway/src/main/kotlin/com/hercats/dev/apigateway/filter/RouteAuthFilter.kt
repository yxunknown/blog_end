package com.hercats.dev.apigateway.filter

import com.hercats.dev.commonbase.tool.base64
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class RouteAuthFilter: ZuulFilter() {
    val logger = Logger.getLogger("${this::class.java.`package`.name}.${this::class.java.simpleName}")

    override fun run(): Any {
        val ctx = RequestContext.getCurrentContext()
        ctx.addZuulRequestHeader("Authorization", "Basic ${base64("request:admin123")}")
        logger.info("add basic info success")
        return Any()
    }

    override fun shouldFilter(): Boolean = true

    override fun filterType(): String = FilterConstants.ROUTE_TYPE

    override fun filterOrder(): Int = 10
}