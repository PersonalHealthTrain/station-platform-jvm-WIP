package de.difuture.ekut.pht.station.repository

import de.difuture.ekut.pht.station.domain.LocalTrain
import org.springframework.data.jpa.repository.JpaRepository

interface ProcessedTrainRepository : JpaRepository<LocalTrain, LocalTrain.LocalTrainId> {

    fun findByState(state: LocalTrain.TrainState): LocalTrain?
}
