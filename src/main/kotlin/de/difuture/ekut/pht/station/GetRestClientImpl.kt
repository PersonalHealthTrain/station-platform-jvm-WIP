package de.difuture.ekut.pht.station

import de.difuture.ekut.pht.lib.http.GetRestClient
import de.difuture.ekut.pht.lib.http.RestHttpResponse
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.net.URI


/**
 * Station specific implementation of the GetRestClient from the PHT library.
 *
 * @author Lukas Zimmermann
 *
 */
class GetRestClientImpl : GetRestClient {

    override fun get(uri: URI): RestHttpResponse {

        val response = RestTemplate().getForEntity<String>(uri)
        return object :RestHttpResponse {

            override val body = response.body?.let { it } ?: ""
        }
    }
}
