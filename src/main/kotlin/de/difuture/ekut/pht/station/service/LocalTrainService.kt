package de.difuture.ekut.pht.station.service

import de.difuture.ekut.pht.lib.train.api.data.TrainId
import de.difuture.ekut.pht.lib.train.api.data.TrainTag
import de.difuture.ekut.pht.station.domain.LocalTrain
import de.difuture.ekut.pht.station.repository.ProcessedTrainRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class LocalTrainService
@Autowired constructor(private val repo: ProcessedTrainRepository) {

    /**
     * Ensures that the train is present in the repository.
     * If the train does not exist, it will be created with the BEFORE State
     *
     */
    fun ensure(trainId: TrainId, trainTag: TrainTag) {

        val processedTrainId = LocalTrain.LocalTrainId(trainId, trainTag)

        if ( ! repo.findById(processedTrainId).isPresent) {

            repo.save(LocalTrain(processedTrainId, LocalTrain.TrainState.BEFORE, "BEFORE"))
        }
    }

    /**
     * Returns a train from the repository that can be processed
     *
     */
    fun getTrainForProcessing() =

            repo.findByState(LocalTrain.TrainState.BEFORE).firstOrNull()?.let { localTrain ->

                repo.save(localTrain.copy(state = LocalTrain.TrainState.PROCESSING))
            }

    fun success(train: LocalTrain) = repo.save(train.copy(state = LocalTrain.TrainState.SUCCESS))
}
