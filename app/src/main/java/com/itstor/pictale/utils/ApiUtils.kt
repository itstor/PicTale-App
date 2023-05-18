package com.itstor.pictale.utils

class ApiUtils {
    companion object {
        fun formatToken(token: String) = "Bearer $token"
    }
}