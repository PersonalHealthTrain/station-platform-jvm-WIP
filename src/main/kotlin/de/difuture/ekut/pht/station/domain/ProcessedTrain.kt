package de.difuture.ekut.pht.station.domain

import de.difuture.ekut.pht.lib.train.TrainId
import de.difuture.ekut.pht.lib.train.TrainTag
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity


/**
 * For a station, a train is a tuple of TrainId and TrainTag
 *
 * @author Lukas Zimmermann
 */
@Entity(name = "ProcessedTrain")
data class ProcessedTrain(

        @EmbeddedId
        val id: ProcessedTrainId,

        val state: TrainState
) {

        @Embeddable
        data class ProcessedTrainId(

                @Column(name = "train_id", unique = false, nullable = false)
                val trainId: TrainId,

                @Column(name = "train_tag", unique = false, nullable = false)
                val trainTag: TrainTag
        ) : Serializable

        enum class TrainState {

                BEFORE,
                PROCESSING,
                AFTER
        }
}
