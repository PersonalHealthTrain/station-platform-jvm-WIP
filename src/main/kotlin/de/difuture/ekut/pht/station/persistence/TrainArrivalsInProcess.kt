package de.difuture.ekut.pht.station.persistence

import de.difuture.ekut.pht.lib.registry.train.id.ITrainId
import de.difuture.ekut.pht.station.persistence.TrainArrivalBeingProcessed
import org.springframework.data.jpa.repository.JpaRepository

interface TrainArrivalsBeingProcessedRepository : JpaRepository<TrainArrivalBeingProcessed, Int> {

    fun existsByTrainId(trainId: ITrainId): Boolean
}
