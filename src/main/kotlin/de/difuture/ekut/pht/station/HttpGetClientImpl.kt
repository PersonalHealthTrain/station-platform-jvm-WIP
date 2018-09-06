package de.difuture.ekut.pht.station

import jdregistry.client.http.IHttpGetClient
import jdregistry.client.http.IHttpResponse
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

    override fun get(uri: URI, authorization: String?): IHttpResponse {

        val headers = HttpHeaders()

        if (authorization != null) {

            headers.set("Authorization", authorization)
        }

        val response = this.restTemplate.exchange(
                uri,
                HttpMethod.GET,
                HttpEntity<String>(headers),
                String::class.java)

        return object : IHttpResponse {

            override val authenticate = response.headers.get("WWW-Authenticate")
            override val statusCode = response.statusCode.value()
            override val body = response.body?.let { it } ?: ""
        }
    }
}

