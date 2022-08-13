package com.kanastruk.sample.common.rest

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*

data class TypedResponse<T,E> (val successBody:T?=null, val errorBody:E?=null, val response:HttpResponse) {
    val isSuccessful: Boolean
        get() = response.status.isSuccess()
}

// NOTE: In 'real life', this could result in runtime casting crashes.
// SLIDE: Worthy? Ktor vs Retrofit basically
suspend inline fun <reified T, reified E> HttpResponse.buildTypedResponse(): TypedResponse<T, E> {
    return if( status.isSuccess() ) {
        // I don't like this.
        val successBody = if (bodyAsText() != "null") body<T>() else null
        TypedResponse(successBody = successBody,response=this)
    }
    else TypedResponse(errorBody = body(),response=this)
}
