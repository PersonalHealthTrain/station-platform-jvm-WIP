package de.difuture.ekut.pht.station.service

import de.difuture.ekut.pht.lib.train.TrainId
import de.difuture.ekut.pht.lib.train.TrainTag
import de.difuture.ekut.pht.station.domain.ProcessedTrain
import de.difuture.ekut.pht.station.repository.ProcessedTrainRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ProcessedTrainService
@Autowired constructor(private val repo: ProcessedTrainRepository) {

    fun getState(trainId: TrainId, trainTag: TrainTag): ProcessedTrain.TrainState? {

        val train = ProcessedTrain.ProcessedTrainId(trainId, trainTag)
        return repo.findById(train).orElse(null)?.state
    }
}
