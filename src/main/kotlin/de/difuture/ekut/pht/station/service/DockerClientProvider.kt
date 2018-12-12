package de.difuture.ekut.pht.station.service

import de.difuture.ekut.pht.lib.runtime.api.docker.DockerRuntimeClient
import de.difuture.ekut.pht.lib.runtime.api.docker.data.DockerRunOptionalParameters
import de.difuture.ekut.pht.lib.runtime.api.docker.withDefaultRunParameters
import de.difuture.ekut.pht.lib.runtime.impl.SpotifyDockerClient
import de.difuture.ekut.pht.station.props.StationDockerProperties
import de.difuture.ekut.pht.station.props.StationProperties
import de.difuture.ekut.pht.station.props.StationRegistryProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DockerClientProvider
@Autowired constructor(
        private val stationProps: StationProperties,
        private val stationRegistryProps: StationRegistryProperties,
        private val stationDockerProperties: StationDockerProperties) {

    fun getDockerClient(): DockerRuntimeClient {

        val client = SpotifyDockerClient().withDefaultRunParameters(
                DockerRunOptionalParameters(
                        env = stationProps.resources,
                        network = stationDockerProperties.network)
        )
        val uri = stationRegistryProps.uri.toASCIIString()

        val success = client.login(
                stationRegistryProps.username,
                stationRegistryProps.password,
                host=uri)

        if (! success) {

            throw StationStartupException(
                    "Login to Docker Registry with URI $uri  with provided username and password failed")
        }
        return client
    }
}
