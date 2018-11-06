package com.hercats.dev.commonbase.tool

import jodd.mail.Email
import jodd.mail.MailServer
import org.springframework.stereotype.Component
import java.net.Authenticator
import java.util.logging.Logger

const val FROM = "hercats@qq.com"

@Component
object mail {
    private var to = ""
    private var subject = ""
    private var text = ""
    private var html = ""
    private val smtpServer = MailServer.create()
            .ssl(true)
            .host("smtp.qq.com")
            .port(465)
            .auth("hercats@qq.com", "ebgqzepicxhdbbji")
            .buildSmtpMailServer()


    private val logger = Logger.getLogger("com.hercats.dev.commonbase.tool.mail")

    fun to(to: String): mail {
        this.to = to
        return this
    }

    fun subject(subject: String): mail {
        this.subject = subject
        return this
    }

    fun text(text: String): mail {
        this.text = text
        return this
    }

    fun html(html: String): mail {
        this.html = html
        return this
    }

    fun send(): Boolean {
        fun validate(): Boolean {
            var result = this.to be blank || this.subject be blank || (this.text be blank && this.html be blank)
            if (this.to be blank) {
                logger.warning("the receiver of mail is blank.")
            }
            if (this.subject be blank) {
                logger.warning("the subject of mail is blank.")
            }
            if (this.text be blank && this.html be blank) {
                logger.warning("the text content or html content can not be both blank.")
            }
            return !result
        }
        return if (validate()) {
            val mail = Email.create()
                    .from(FROM)
                    .to(this.to)
                    .subject(this.subject)
            if (!(text be blank)) {
                mail.textMessage(text)
            }
            if (!(html be blank)) {
                mail.htmlMessage(html)
            }
            try {
                logger.info("start sending mail...")
                val sendSession = smtpServer.createSession()
                sendSession.open()
                sendSession.sendMail(mail)
                sendSession.close()
                logger.info("send mail success.")
                true
            } catch (e: Exception) {
                e.printStackTrace()
                logger.warning("send mail failed.")
                false
            }
        } else {
            false
        }
    }
}
