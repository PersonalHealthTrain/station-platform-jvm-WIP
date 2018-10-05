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

    /**
     * Ensures that the train is present in the repository.
     * If the train does not exist, it will be created with the BEFORE State
     *
     */
    fun ensure(trainId: TrainId, trainTag: TrainTag) {

        val processedTrainId = ProcessedTrain.ProcessedTrainId(trainId, trainTag)

        if ( ! repo.findById(processedTrainId).isPresent) {

            repo.save(ProcessedTrain(processedTrainId, ProcessedTrain.TrainState.BEFORE))
        }
    }

    fun list() = repo.findAll()
}
