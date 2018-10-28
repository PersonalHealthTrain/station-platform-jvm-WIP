package de.difuture.ekut.pht.station.service

import de.difuture.ekut.pht.lib.train.api.data.TrainTag
import de.difuture.ekut.pht.lib.train.registry.DefaultTrainRegistryClient
import de.difuture.ekut.pht.lib.train.station.DockerTrainStation
import de.difuture.ekut.pht.lib.train.station.StationInfo
import de.difuture.ekut.pht.station.props.StationProperties
import de.difuture.ekut.pht.station.props.StationRegistryProperties
import jdregistry.client.api.DockerRegistryGetClient
import jdregistry.client.api.auth.Authenticate
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

    private val client = clientProvider.getDockerClient()
    private val stationInfo = StationInfo(props.id.toInt(), TrainTag.IMMEDIATE)

    private val station = DockerTrainStation(
            client = this.client,
            stationInfo = this.stationInfo)

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

        // We are interested in all train arrivals tag ends with the train tag
        // that this station is interested in
        registry
                .listTrainArrivals {it.trainTag.repr.endsWith(this.trainTag.repr)}
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
            val trainArrival = registry.getTrainArrival(id.trainId, id.trainTag)

            if (trainArrival != null) {

                // Create the Train Departure in the most straightforward way possible
                // TODO Exception handling
                val trainDeparture = station.departWithAlgorithm(trainArrival)

                // Submit the TrainDeparture to the Train Registry
                val submitOk = this.registry.submitTrainDeparture(trainDeparture)

                // If the submission is ok, then we set the train to done
                if (submitOk) {

                    this.service.success(nextTrain)
                }
            }
        }
    }
}
