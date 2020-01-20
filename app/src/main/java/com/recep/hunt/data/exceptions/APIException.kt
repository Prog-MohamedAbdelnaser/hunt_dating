package com.recep.hunt.data.exceptions

class APIException(var code: String, override var message: String) : RuntimeException()