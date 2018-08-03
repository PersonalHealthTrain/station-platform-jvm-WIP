package de.difuture.ekut.pht.station.trainarrival

import com.spotify.docker.client.DefaultDockerClient
import de.difuture.ekut.pht.lib.registry.docker.DockerRegistryClient
import de.difuture.ekut.pht.lib.registry.train.TrainId
import de.difuture.ekut.pht.lib.registry.train.TrainRegistryClient
import de.difuture.ekut.pht.lib.registry.train.arrival.ITrainArrival
import de.difuture.ekut.pht.lib.runtime.IDockerClient
import de.difuture.ekut.pht.station.GetRestClientImpl
import de.difuture.ekut.pht.station.StationDockerClient
import de.difuture.ekut.pht.station.arrivalFromClient
import de.difuture.ekut.pht.test.lib.SingleExposedPortContainer
import de.difuture.ekut.pht.test.lib.TEST_TRAIN_REGISTRY_REPOSITORY
import org.junit.*

/**
 *  Performs tests for the check_requirements function of trains
 *
 */
class RunAlgorithmTests {

    companion object {

        // Container that is used for fetching Docker Registry Notifications
        @ClassRule
        @JvmField
        val REGISTRY = SingleExposedPortContainer(TEST_TRAIN_REGISTRY_REPOSITORY, 5000)

        // Count Rows Train
        private val repo1 = TrainId("train_test_count_rows")
    }

    /////////////////////////  The registry trainRegistryClient  /////////////////////////////////////////////////////////////
    private lateinit var trainRegistryClient : TrainRegistryClient
    private lateinit var dockerClient: StationDockerClient

    @Before fun before() {

        this.trainRegistryClient = TrainRegistryClient(
                DockerRegistryClient(REGISTRY.getExternalURI(), GetRestClientImpl())
        )
        this.dockerClient = StationDockerClient(DefaultDockerClient.fromEnv().build())
        this.arrivalOf = arrivalFromClient(this.trainRegistryClient)
    }

    @After fun after() {
        this.dockerClient.close()
    }

    // Function for fetching arrivals from the trainRegistry
    private lateinit var arrivalOf : (TrainId) -> ITrainArrival<IDockerClient>?

    /////////////////////////  Tests  /////////////////////////////////////////////////////////////

    /**
     * Checks whether the trains under test can actually be retrieved from the train registry
     */
    @Test
    fun get_run_algorithm() {

        listOf(
                arrivalOf(repo1)
        ).forEach(Assert::assertNotNull)
    }
}
