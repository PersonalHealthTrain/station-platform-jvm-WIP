package de.difuture.ekut.pht.station.persistence

import de.difuture.ekut.pht.lib.train.id.ITrainId
import javax.persistence.*


@Entity
data class TrainArrivalBeingProcessed(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,

        @Column(nullable = false)
        @Convert(converter = ITrainIdConverter::class)
        val trainId : ITrainId
)
