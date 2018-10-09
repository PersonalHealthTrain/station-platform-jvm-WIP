package de.difuture.ekut.pht.station.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "station.docker")
class StationDockerProperties {

    lateinit var networkId: String
}
