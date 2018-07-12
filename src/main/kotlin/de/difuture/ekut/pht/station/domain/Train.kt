package de.difuture.ekut.pht.station.domain

import de.difuture.ekut.pht.station.model.TrainState
import javax.persistence.*

@Entity(name = "Train")
@Table(name = "train")
data class Train(

        @EmbeddedId
        val id : TrainId,
        val state : TrainState
)
