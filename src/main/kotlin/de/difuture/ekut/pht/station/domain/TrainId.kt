package de.difuture.ekut.pht.station.domain

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class TrainId(

    @Column(name="repository")
    val repository : String,

    @Column(name = "tag")
    val tag : String
) : Serializable
