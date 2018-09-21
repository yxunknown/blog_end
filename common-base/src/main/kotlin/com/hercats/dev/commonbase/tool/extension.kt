package com.hercats.dev.commonbase.tool

import com.hercats.dev.commonbase.model.Message

object blank

infix fun String.be(x: blank): Boolean = this.isBlank()

infix fun Message.error_400(_info: String) {
    code = 400
    info = _info
}

infix fun Message.error_500(_info: String) {
    code = 500
    info = _info
}

infix fun Message.ok(_info: String) {
    code = 200
    info = _info
}

infix fun Message.error_401(_info: String) {
    code = 401
    info = _info
}