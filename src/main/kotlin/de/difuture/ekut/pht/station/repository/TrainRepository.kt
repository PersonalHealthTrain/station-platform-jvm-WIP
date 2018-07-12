package de.difuture.ekut.pht.station.repository

import de.difuture.ekut.pht.station.domain.Train
import org.springframework.data.jpa.repository.JpaRepository

interface TrainRepository : JpaRepository<Train, Long>
