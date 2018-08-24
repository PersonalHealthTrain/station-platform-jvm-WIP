package de.difuture.ekut.pht.station.service

import de.difuture.ekut.pht.lib.station.StationPing
import de.difuture.ekut.pht.lib.station.StationPingResponse
import de.difuture.ekut.pht.station.props.StationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate


/**
 * Service that regularly sends pings to the registered endpoints
 *
 */
@Service
class PingService
@Autowired constructor(props: StationProperties) {

    private val listeners = props.listeners

    // The REST Template
    private val template = RestTemplate()

    init{

        template.errorHandler = object: DefaultResponseErrorHandler() {

            override fun hasError(statusCode: HttpStatus) =
                    statusCode != HttpStatus.OK && statusCode != HttpStatus.NOT_FOUND
        }
    }

    // The Station Ping instance
    private val stationPing = StationPing(props.id.toInt(), props.name)

    @Scheduled(fixedDelay = 5000)
    fun ping() {

        listeners.forEach {

            this.template.postForObject(it, stationPing, StationPingResponse::class.java)
        }
    }
}
