package com.ayz4sci.boilerplate.data.repository.base

import com.ayz4sci.boilerplate.ui.viewHelpers.common.SharedPreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by @ayz4sci on 03/07/2018.
 */
class HttpInterceptor(private var sharedPreferencesHelper: SharedPreferencesHelper) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val mJwt: String? = sharedPreferencesHelper.getStringPreference(SharedPreferencesHelper.AUTH_TOKEN)

        val request = chain.request()

        // Add Cookie Header
        if (!mJwt.isNullOrBlank()) {
            return chain.proceed(request.newBuilder()
                    .addHeader("Cookie", "jwt=$mJwt")
                    .build())
        } else {
            return chain.proceed(request)
        }

    }
}
