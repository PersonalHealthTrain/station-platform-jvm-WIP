package de.difuture.ekut.pht.station.trainarrival

import com.spotify.docker.client.DefaultDockerClient
import de.difuture.ekut.pht.lib.registry.docker.DockerRegistryClient
import de.difuture.ekut.pht.lib.registry.train.TrainRegistryClient
import de.difuture.ekut.pht.lib.registry.train.arrival.ITrainArrival
import de.difuture.ekut.pht.lib.runtime.IDockerClient
import de.difuture.ekut.pht.station.HttpGetClientImpl
import de.difuture.ekut.pht.station.arrivalFromClient
import de.difuture.ekut.pht.lib.registry.train.TrainId
import de.difuture.ekut.pht.test.lib.SingleExposedPortContainer
import de.difuture.ekut.pht.test.lib.TEST_TRAIN_REGISTRY_REPOSITORY
import org.junit.*


class PrintSummaryTests {

    companion object {

        // Container that is used for fetching Docker Registry Notifications
        @ClassRule @JvmField
        val REGISTRY : SingleExposedPortContainer =
                SingleExposedPortContainer(
                        TEST_TRAIN_REGISTRY_REPOSITORY,
                        5000)

        // The repositories and tags
        private val repo1 = TrainId("train_test_print_summary_1")
        private val repo2 = TrainId("train_test_print_summary_2")
    }

    /////////////////////////  The registry trainRegistryClient  ////////////////////////////////////////////////////
    private lateinit var trainRegistryClient : TrainRegistryClient
    private lateinit var dockerClient: StationDockerClient

    // Function for fetching arrivals from the trainRegistry
    private lateinit var arrivalOf : (TrainId) -> ITrainArrival<IDockerClient>?

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
    /////////////////////////  Helper functions  /////////////////////////////////////////////////////////////


    /////////////////////////  Tests  /////////////////////////////////////////////////////////////
    /**
     * Checks that the trains under tests can actually be retrieved
     */
    @Test
    fun get_print_summary() {

        listOf(
                arrivalOf(repo1), arrivalOf(repo2)
        ).forEach(Assert::assertNotNull)
    }


    // Test for executing print summary traisn
    @Test
    fun execute_print_summary() {

        mapOf(
                arrivalOf(repo1) to "TEST_PRINT_SUMMARY_1\n",
                arrivalOf(repo2) to "TEST_PRINT_SUMMARY_2\n"

        ).forEach { (arrival, expected) ->

            arrival?.let { Assert.assertEquals(expected, it.printSummary(dockerClient, 5)) }
                    ?: Assert.fail("Arrival is null")
        }
    }
}
