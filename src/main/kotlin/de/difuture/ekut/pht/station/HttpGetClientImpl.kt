package de.difuture.ekut.pht.station

import de.difuture.ekut.pht.lib.http.HttpHeader
import de.difuture.ekut.pht.lib.http.HttpStatusCode
import de.difuture.ekut.pht.lib.http.IHttpGetClient
import de.difuture.ekut.pht.lib.http.IHttpResponse
import org.springframework.http.*
import org.springframework.web.client.RestTemplate
import java.net.URI
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler


/**
 * Station specific implementation of the GetRestClient from the PHT library.
 *
 * @author Lukas Zimmermann
 *
 */
class HttpGetClientImpl : IHttpGetClient {

    private val restTemplate = RestTemplate()

    init {

        restTemplate.errorHandler = object : DefaultResponseErrorHandler() {

            override fun hasError(response: ClientHttpResponse): Boolean {

                // We expect the status Codes of either 200, when everything went fine, or 401,
                // if an authentication challenge needs to be solved
                val statusCode = response.statusCode
                return statusCode != HttpStatus.OK && statusCode != HttpStatus.UNAUTHORIZED
            }
        }
    }


    override fun get(uri: URI, httpHeaders: Map<HttpHeader, String>?): IHttpResponse {

        val headers = HttpHeaders()

        // If HttpHeaders have been passed, then they need to be set on the headers
        httpHeaders?.let {

            headers.setAll(it.mapKeys { x -> x.key.repr })
        }

        val response = this.restTemplate.exchange(
                uri,
                HttpMethod.GET,
                HttpEntity<String>(headers),
                String::class.java)

        val statusCode = HttpStatusCode.of(response.statusCode.value())
                ?: throw IllegalStateException("Unknown Status Code")

        return object : IHttpResponse {

            override val headers = response.headers
            override val statusCode = statusCode
            override val body = response.body?.let { it } ?: ""
        }
    }
}

