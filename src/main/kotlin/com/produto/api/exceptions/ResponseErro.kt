package com.produto.api.exceptions

import java.sql.Timestamp

class ResponseErro(
    var timestamp: Timestamp,
    var message: String?,
    var type: String,
    var codigo: String,
    var error: String,) {

    var containsError: Boolean = true
}