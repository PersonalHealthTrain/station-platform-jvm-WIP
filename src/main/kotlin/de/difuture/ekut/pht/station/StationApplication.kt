package de.difuture.ekut.pht.station

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class StationApplication

fun main(args: Array<String>) {
    runApplication<StationApplication>(*args)
}
