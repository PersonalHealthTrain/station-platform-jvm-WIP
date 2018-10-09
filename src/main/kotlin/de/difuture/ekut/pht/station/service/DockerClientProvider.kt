package de.difuture.ekut.pht.station.service

import de.difuture.ekut.pht.lib.data.DockerNetworkId
import de.difuture.ekut.pht.lib.runtime.docker.params.DockerRunOptionalParameters
import de.difuture.ekut.pht.lib.runtime.docker.spotify.SpotifyDockerClient
import de.difuture.ekut.pht.lib.runtime.docker.withDefaultRunParameters
import de.difuture.ekut.pht.station.props.StationDockerProperties
import de.difuture.ekut.pht.station.props.StationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DockerClientProvider
@Autowired constructor(
        private val stationProps: StationProperties,
        private val stationDockerProperties: StationDockerProperties) {

    fun getDockerClient() = SpotifyDockerClient().withDefaultRunParameters(
            DockerRunOptionalParameters(
                    env = stationProps.resources,
                    networkId = DockerNetworkId(stationDockerProperties.networkId))
    )
}
