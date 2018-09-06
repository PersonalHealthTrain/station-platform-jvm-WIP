package de.difuture.ekut.pht.station.persistence

import de.difuture.ekut.pht.lib.train.id.ITrainId
import org.springframework.data.jpa.repository.JpaRepository

interface TrainArrivalsBeingProcessedRepository : JpaRepository<TrainArrivalBeingProcessed, Int> {

    fun existsByTrainId(trainId: ITrainId): Boolean
}
