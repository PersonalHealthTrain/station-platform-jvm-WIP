package de.difuture.ekut.pht.station

import de.difuture.ekut.pht.lib.registry.train.TrainRegistryClient
import de.difuture.ekut.pht.lib.registry.train.arrival.TrainId

interface ITrainRegistryTests {

    var trainRegistry : TrainRegistryClient

    fun arrivalOf(repo : TrainId) = {


    }

}