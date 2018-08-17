package de.difuture.ekut.pht.station

import de.difuture.ekut.pht.lib.http.IHttpGetClient
import de.difuture.ekut.pht.lib.http.IHttpResponse
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.net.URI


/**
 * Station specific implementation of the GetRestClient from the PHT library.
 *
 * @author Lukas Zimmermann
 *
 */
class HttpGetClientImpl : IHttpGetClient {

    override fun get(uri: URI): IHttpResponse {

        val response = RestTemplate().getForEntity<String>(uri)

        return object :IHttpResponse {

            override val statusCode = response.statusCode.value()
            override val body = response.body?.let { it } ?: ""
        }
    }
}
