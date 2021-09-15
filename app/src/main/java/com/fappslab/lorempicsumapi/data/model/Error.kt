package com.fappslab.lorempicsumapi.data.model


import com.google.gson.Gson
import com.google.gson.annotations.Expose
import okhttp3.ResponseBody

class Error(
    @Expose
    var code: String? = null,
    @Expose
    var detail: String? = null,
    @Expose
    var title: String? = null,
    @Expose
    var text: String? = null,
    @Expose
    var httpCode: Int = 0
) {
    companion object {
        @JvmStatic
        val exceptionError: Error
            get() {
                val error = Error()
                error.title = "Error inesperado"
                error.code = "333"
                error.httpCode = 500
                error.text = "Ops! Tivemos um problema.\nQue tal tentar de novo daqui a pouco?"
                return error
            }

        @JvmStatic
        fun getError(
            responseBody: ResponseBody?,
            httpCode: Int
        ): Error {
            Gson().fromJson(responseBody?.string(), Error::class.java)?.let { error ->
                error.text = error.text ?: ""
                error.title = error.title ?: ""
                error.detail = error.detail ?: ""
                error.httpCode = httpCode

                return error
            }
            return exceptionError
        }
    }
}
