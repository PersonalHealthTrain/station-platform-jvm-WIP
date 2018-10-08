package de.difuture.ekut.pht.station.service

import de.difuture.ekut.pht.lib.runtime.docker.params.DockerRunOptionalParameters
import de.difuture.ekut.pht.lib.runtime.docker.spotify.SpotifyDockerClient
import de.difuture.ekut.pht.lib.runtime.docker.withDefaultRunParameters
import de.difuture.ekut.pht.station.props.StationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DockerClientProvider
@Autowired constructor(private val stationProps: StationProperties) {

    fun getDockerClient() = SpotifyDockerClient().withDefaultRunParameters(
            DockerRunOptionalParameters(env=stationProps.resources)
    )
}
