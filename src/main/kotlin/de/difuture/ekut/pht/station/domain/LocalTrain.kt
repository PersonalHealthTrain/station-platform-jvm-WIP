package de.difuture.ekut.pht.station.domain

import de.difuture.ekut.pht.lib.data.TrainName
import de.difuture.ekut.pht.lib.data.TrainTag
import de.difuture.ekut.pht.lib.jpa.TrainNameConverter
import de.difuture.ekut.pht.lib.jpa.TrainTagConverter
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity

/**
 * For a station, a train is a tuple of TrainId and TrainTag
 *
 * @author Lukas Zimmermann
 */
@Entity(name = "LocalTrain")
data class LocalTrain(

        @EmbeddedId
        val id: LocalTrainId,
        val state: TrainState,
        val reason: String
) {
        @Embeddable
        data class LocalTrainId(

                @Column(name = "train_id", unique = false, nullable = false)
                @Convert(converter = TrainNameConverter::class)
                val trainName: TrainName,

                @Column(name = "train_tag", unique = false, nullable = false)
                @Convert(converter = TrainTagConverter::class)
                val trainTag: TrainTag
        ) : Serializable

        enum class TrainState {

                BEFORE,
                PROCESSING,
                SUCCESS,
                FAILURE
        }
}
