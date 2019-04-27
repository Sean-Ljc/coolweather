package com.example.coolweather.util

import okhttp3.OkHttpClient
import okhttp3.Request

class HttpUtil {
    companion object {
        fun sendOkHttpRequest(address: String, callback: okhttp3.Callback) {
            var client = OkHttpClient()
            var request = Request.Builder().url(address).build()
            client.newCall(request).enqueue(callback)
        }
    }
}