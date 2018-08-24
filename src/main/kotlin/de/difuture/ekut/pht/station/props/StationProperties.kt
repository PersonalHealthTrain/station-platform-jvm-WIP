package de.difuture.ekut.pht.station.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.net.URI


@Configuration
@ConfigurationProperties(prefix = "station")
class StationProperties {

    lateinit var id: String
    lateinit var name: String

    lateinit var resources: Map<String, String>

    lateinit var listeners: List<URI>
}
