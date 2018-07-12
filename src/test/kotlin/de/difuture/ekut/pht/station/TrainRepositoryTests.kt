package de.difuture.ekut.pht.station

import de.difuture.ekut.pht.station.domain.Train
import de.difuture.ekut.pht.station.repository.TrainRepository
import io.github.benas.randombeans.api.EnhancedRandom
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class TrainRepositoryTests {


    @Autowired
    private lateinit var trainRepository: TrainRepository

    @Test
    fun createRandomTrains_saveToRepository() {

        val trains = EnhancedRandom.randomListOf(20, Train::class.java)
        trains.forEach { this.trainRepository.save(it) }
    }
}
