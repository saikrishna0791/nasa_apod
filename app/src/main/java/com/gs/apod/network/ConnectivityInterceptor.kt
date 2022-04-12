package com.gs.apod.network

import com.gs.apod.ApodApplication
import com.gs.apod.utils.Utils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class ConnectivityInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!Utils.isOnline(ApodApplication.instance)) {
            throw IOException("No internet connection")
        } else {
            return chain.proceed(chain.request())
        }
    }
}