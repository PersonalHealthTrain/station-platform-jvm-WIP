package de.difuture.ekut.pht.station.service

import de.difuture.ekut.pht.lib.train.TrainTag
import de.difuture.ekut.pht.lib.train.api.StationInfo
import de.difuture.ekut.pht.lib.train.api.execution.docker.RunAlgorithm
import de.difuture.ekut.pht.lib.train.api.interf.departure.DockerRegistryTrainDeparture
import de.difuture.ekut.pht.lib.train.registry.DefaultTrainRegistryClient
import de.difuture.ekut.pht.station.props.StationProperties
import de.difuture.ekut.pht.station.props.StationRegistryProperties
import jdregistry.client.api.DockerRegistryGetClient
import jdregistry.client.auth.Authenticate
import jdregistry.client.data.DockerRepositoryName
import jdregistry.client.impl.http.spring.SpringHttpGetClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
class TrainProcessor
@Autowired constructor(
        props: StationProperties,
        registryProps: StationRegistryProperties,
        private val service: LocalTrainService,
        clientProvider: DockerClientProvider) {


    /**
     * The Remote Registry that the Train Processor contacts
     */
    private val registry = DefaultTrainRegistryClient(
            dockerRegistryClient = DockerRegistryGetClient.of(
                    registryProps.uri,
                    SpringHttpGetClient(),
                    Authenticate.with(registryProps.username,  registryProps.password)),
            namespace = registryProps.namespace)

    /**
     * The Docker Client that the Docker Registy uses
     */
    private val docker = clientProvider.getDockerClient()


    /**
     * The station info that the station will communicate to the train.
     * TODO Currently the train mode IMMEDIATE is always assumed
     */
    private val stationInfo = StationInfo(
            props.id.toInt(), TrainTag.IMMEDIATE
    )

    /**
     * Expected LocalTrain Tag. This station will execute all the train arrivals with the
     * designated train tag
     */
    private val trainTag = TrainTag.of("station.${props.id}")


    /**
     * Synchronizes remote Train Arrivals
     *
     */
    @Scheduled(fixedDelay = 5000)
    fun syncTrainArrivals() {

        registry
                .listTrainArrivals {it.trainTag == this.trainTag}
                .forEach { arrival ->

                    service.ensure(arrival.trainId, arrival.trainTag)
                }
    }

    @Scheduled(fixedDelay = 1000)
    fun processTrain() {

        val nextTrain = service.getTrainForProcessing()
        if (nextTrain != null) {

            val id = nextTrain.id
            // Find the corresponding train arrival again in the registy
            val trainArrival = registry
                    .listTrainArrivals {  it.trainId == id.trainId && it.trainTag == id.trainTag }
                    .singleOrNull()

            // Execute the train arrival if exaclty one has been found
            if (trainArrival != null) {

                val dockerTrainOutput = RunAlgorithm.execArrival(trainArrival, docker, stationInfo)

                // If the response is successful, then create a new train Departure
                val trainResponse = dockerTrainOutput.response

                // If successful, use the Train Registry to publish the new image
                if (trainResponse != null && trainResponse.success) {

                    val containerId = dockerTrainOutput.containerOutput.containerId

                    // Generate a new image for the train
                    // val imageId = docker.commit(containerId, DockerRepositoryName() )

                }
            }
        }
    }
}
