package de.difuture.ekut.pht.station

import de.difuture.ekut.pht.lib.registry.train.TrainId
import de.difuture.ekut.pht.lib.registry.train.TrainRegistryClient
import de.difuture.ekut.pht.lib.registry.train.tag.TrainTag

private val TEST_TAG = TrainTag.of("test")

fun arrivalFromClient(client : TrainRegistryClient) =  { id : TrainId ->

 client.getTrainArrival(id, TEST_TAG)
}
