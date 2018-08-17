package de.difuture.ekut.pht.station.trainarrival

import com.spotify.docker.client.DefaultDockerClient
import de.difuture.ekut.pht.lib.registry.docker.DockerRegistryClient
import de.difuture.ekut.pht.lib.registry.train.TrainId
import de.difuture.ekut.pht.lib.registry.train.TrainRegistryClient
import de.difuture.ekut.pht.lib.registry.train.arrival.ITrainArrival
import de.difuture.ekut.pht.lib.runtime.IDockerClient
import de.difuture.ekut.pht.station.HttpGetClientImpl
import de.difuture.ekut.pht.station.arrivalFromClient
import de.difuture.ekut.pht.test.lib.SingleExposedPortContainer
import de.difuture.ekut.pht.test.lib.TEST_TRAIN_REGISTRY_REPOSITORY
import org.junit.*

/**
 *  Performs tests for the check_requirements function of trains
 *
 */
class CheckRequirementsTests {

    companion object {

        // Container that is used for fetching Docker Registry Notifications
        @ClassRule
        @JvmField
        val REGISTRY = SingleExposedPortContainer(TEST_TRAIN_REGISTRY_REPOSITORY, 5000)

        private const val trainPrefix = "train_test_check_requirements_status_"

        private val repo1 = TrainId("${trainPrefix}0")
        private val repo2 = TrainId("${trainPrefix}1")
        private val repo3 = TrainId("${trainPrefix}2")

        // Count Rows Train
        private val repo4 = TrainId("train_test_count_rows")
    }

    /////////////////////////  The registry trainRegistryClient  /////////////////////////////////////////////////////////////
    private lateinit var trainRegistryClient : TrainRegistryClient
    private lateinit var dockerClient: StationDockerClient

    @Before fun before() {

        this.trainRegistryClient = TrainRegistryClient(
                DockerRegistryClient(REGISTRY.getExternalURI(), HttpGetClientImpl())
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
    fun get_check_requirements() {

        listOf(
                arrivalOf(repo1),
                arrivalOf(repo2),
                arrivalOf(repo3),
                arrivalOf(repo4)
        ).forEach(Assert::assertNotNull)
    }

    @Test
    fun execute_check_requirements() {

        // maps the check requirements train to the expected status code
        mapOf(
                arrivalOf(repo1) to true,
                arrivalOf(repo2) to false,
                arrivalOf(repo3) to false,
                arrivalOf(repo4) to true

        ).forEach { (arrival, expected) ->

            arrival?.let { Assert.assertEquals(expected, it.checkRequirements(dockerClient, 5)) }
                    ?: Assert.fail("Arrival is null")
        }
    }
}
