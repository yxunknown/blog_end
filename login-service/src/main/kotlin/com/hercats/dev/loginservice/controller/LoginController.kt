package com.hercats.dev.loginservice.controller

import com.hercats.dev.commonbase.mapper.UserMapper
import com.hercats.dev.commonbase.model.Message
import com.hercats.dev.commonbase.model.Token
import com.hercats.dev.commonbase.model.User
import com.hercats.dev.commonbase.redis.Redis
import com.hercats.dev.commonbase.tool.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(@Autowired val userMapper: UserMapper,
                      @Autowired val redis: Redis) {

    @RequestMapping(value = ["/hello", "/hello/"], method = [RequestMethod.GET])
    fun hello(): Message {
        val msg = Message()
        msg.code = 200
        msg.info = "hello"
        return msg
    }

    @RequestMapping(value = ["login/token", "login/token/"], method = [RequestMethod.POST])
    fun login(user: User): Message {
        val msg = Message()
        when {
            (user.account.isBlank()) -> {
                msg.code = 400
                msg.info = "缺少用户账户参数"
            }
            (user.password.isBlank()) -> {
                msg.code = 400
                msg.info = "缺少用户账户参数"
            }
            (userMapper.selectByPrimaryKey(user.account) == null) -> {
                msg.code = 400
                msg.info = "用户不存在"
            }
            (userMapper.selectByPrimaryKey(user.account)?.status?.id != 2) -> {
                msg.code = 400
                msg.info = "用户被限制登录"
            }
            (userMapper.selectByPrimaryKey(user.account)?.password != sha(user.password)) -> {
                msg.code = 400
                msg.info = "用户密码错误"
            }
            else -> {
                val token = token(username = user.account)
                val refreshToken = token(username = user.account)
                val userInfo = userMapper.selectByPrimaryKey(user.account)?.apply { password = "********" }
                val tokenInfo = Token(username = user.account,
                        token = token,
                        refreshToken = refreshToken)
                if (setToken(tokenInfo)) {
                    msg.code = 200
                    msg.info = "登录成功"
                    msg.map("token", tokenInfo)
                    msg.map("user", userInfo ?: "")
                } else {
                    msg.code = 500
                    msg.info = "登录失败"
                }
            }
        }
        return msg
    }

    @RequestMapping(value = ["/login/refresh", "/login/refresh/"], method = [RequestMethod.POST])
    fun refreshToken(refreshToken: String): Message {
        val msg = Message()
        val account = redis get refreshToken
        when {
            (refreshToken.isBlank()) -> {
                msg error_400 "缺少refreshToken参数"
            }
            (account be blank) -> {
                msg error_400 "refresh token失效"
            }
            (userMapper.selectByPrimaryKey(account) == null) -> {
                msg error_400 "用户不存在"
            }
            (userMapper.selectByPrimaryKey(account)?.status?.id != 2) -> {
                msg error_400 "用户被限制登录"
            }
            else -> {
                val user = userMapper.selectByPrimaryKey(account)?.apply { password = "********" }
                val token = token(username = account)
                val newRefreshToken = token(username = account)
                val tokenInfo = Token(username = account,
                        token = token,
                        refreshToken = newRefreshToken)
                if (setToken(tokenInfo)) {
                    msg ok "刷新token成功"
                    msg.map("token", tokenInfo)
                    msg.map("user", user ?: "")
                } else {
                    msg error_500 "刷新token失败"
                }
            }
        }
        return msg
    }

    @RequestMapping(value = ["/login/out", "/login/out"], method = [RequestMethod.POST])
    fun logout(token: String): Message {
        val msg = Message()
        val account = redis get token
        val refreshToken = redis get "$account.refresh_token"
        when {
            (token be blank) -> {
                msg error_401 "未授权"
            }
            (account be blank) -> {
                msg error_400 "查找用户信息失败"
            }
            (redis del token &&
                    redis del refreshToken &&
                    redis del "$account.token" &&
                    redis del "$account.refresh_token") -> {
                msg ok "退出登录成功"
            }
            else -> {
                msg error_500 "退出登录失败"
            }
        }
        return msg
    }

    fun setToken(token: Token): Boolean {
        redis del (redis get "${token.username}.token")
        redis del (redis get "${token.username}.refresh_token")
        return redis.set(key = token.token, value = token.username, expire = token.tokenExpire) &&
                redis.set(key = token.refreshToken, value = token.username, expire = token.refreshTokenExpire) &&
                redis.set(key = "${token.username}.token", value = token.token, expire = token.tokenExpire) &&
                redis.set(key = "${token.username}.refresh_token", value = token.refreshToken, expire = token.refreshTokenExpire)
    }


}