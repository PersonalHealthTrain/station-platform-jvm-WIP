package de.difuture.ekut.pht.station.service

import de.difuture.ekut.pht.lib.train.TrainTag
import de.difuture.ekut.pht.lib.train.registry.DefaultTrainRegistryClient
import de.difuture.ekut.pht.station.props.StationProperties
import de.difuture.ekut.pht.station.props.StationRegistryProperties
import jdregistry.client.api.DockerRegistryGetClient
import jdregistry.client.auth.Authenticate
import jdregistry.client.impl.http.spring.SpringHttpGetClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
class TrainProcessor
@Autowired constructor(
        props: StationProperties,
        registryProps: StationRegistryProperties,
        private val service: ProcessedTrainService) {


    private val registry = DefaultTrainRegistryClient(
            DockerRegistryGetClient.of(
                    registryProps.uri,
                    SpringHttpGetClient(),
                    Authenticate.with(registryProps.username,  registryProps.password)), registryProps.namespace)


    /**
     * Expected ProcessedTrain Tag. This station will execute all the train arrivals with the
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
    fun scheduleTrainExecution() {

        println(service.list())
    }

}
