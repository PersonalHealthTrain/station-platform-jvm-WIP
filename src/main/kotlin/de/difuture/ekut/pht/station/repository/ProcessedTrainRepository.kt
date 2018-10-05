package de.difuture.ekut.pht.station.repository

import de.difuture.ekut.pht.station.domain.ProcessedTrain
import org.springframework.data.jpa.repository.JpaRepository

interface ProcessedTrainRepository : JpaRepository<ProcessedTrain, ProcessedTrain.ProcessedTrainId>
