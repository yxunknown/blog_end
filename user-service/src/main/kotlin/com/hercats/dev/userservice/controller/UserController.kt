package com.hercats.dev.userservice.controller

import com.hercats.dev.commonbase.mapper.UserMapper
import com.hercats.dev.commonbase.model.Message
import com.hercats.dev.commonbase.model.Pagination
import com.hercats.dev.commonbase.model.User
import com.hercats.dev.commonbase.model.UserStatus
import com.hercats.dev.commonbase.tool.sha
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(@Autowired val userMapper: UserMapper) {

    @RequestMapping(value = ["/user", "/user/"], method = [RequestMethod.GET])
    fun getUser(pagination: Pagination): Message {
        val users =  userMapper.select(pagination)
        val msg = Message()
        msg.code = 200
        msg.info = "查询成功"
        msg.map("users", users)
        msg.map("pagination", pagination)
        return msg
    }

    @RequestMapping(value = ["/user", "/user/"], method = [RequestMethod.POST])
    fun addUser(user: User): Message {
        fun validate(user: User, message: Message): Boolean {
            var result = true
            if (!user.account.matches("""(\S)*@(\S)*\.(\S)*""".toRegex())) {
                message.code = 400
                message.info += "请填写正确的邮箱地址,"
                result = false
            }
            if (user.password.length !in 8..16) {
                message.code = 400
                message.info += "密码长度应该为8-16位"
                result = false
            }
            return result
        }
        val msg = Message()
        if (validate(user, msg)) {
            msg.code = 200
            user.password = sha(user.password)
            //set user status to be default
            user.status = UserStatus()
            try {
                if (userMapper.insert(user) == 1) {
                    msg.info = "注册成功，激活邮件已发送到您的注册邮箱，请前往激活"
                } else {
                    msg.code = 500
                    msg.info = "数据库操作失败"
                }
            } catch (e: Exception) {
                msg.code = 500
                msg.info = e.message ?: ""
            }
            user.password = "*******"
            msg.map("user", user)
        }
        return msg
    }

    @RequestMapping(value = ["/user", "/user/"], method = [RequestMethod.PUT])
    fun updateUser(user: User): Message {
        val msg = Message()
        if (validateAccount(user.account)) {
            msg.code = 400
            msg.info = "请提交用户邮箱"
        } else {
            try {
                if (userMapper.update(user) == 1) {
                    msg.code = 200
                    msg.info = "更新用户信息成功"
                    msg.map("user", userMapper.selectByPrimaryKey(
                            user.account).apply { password = "********" })
                } else {
                    msg.code = 500
                    msg.info = "更新用户信息失败"
                }
            } catch (e: Exception) {
                msg.code = 500
                msg.info = e.message ?: ""
            }
        }
        return msg
    }

    @RequestMapping(value = ["/user/{account}", "/user/{account}/"], method = [RequestMethod.GET])
    fun getUserByAccount(@PathVariable(name = "account") account: String): Message {
        val msg = Message()
        if (validateAccount(account)) {
            msg.code = 400
            msg.info = "请提交用户邮箱"
        } else {
            try {
                val user = userMapper.selectByPrimaryKey(account)
                msg.code = 200
                if (validateAccount(user.account)) {
                    msg.info = "用户不存在"
                } else {
                    msg.info = "查找成功"
                    msg.map("user", user.apply { password = "********" })
                }
            } catch (e: Exception) {
                msg.code = 500
                msg.info = e.message ?: "未知错误"
            }
        }
        return msg
    }

    fun validateAccount(account: String) = account.isBlank()

}