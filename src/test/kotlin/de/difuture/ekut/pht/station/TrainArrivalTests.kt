package de.difuture.ekut.pht.station

import com.spotify.docker.client.DefaultDockerClient
import de.difuture.ekut.pht.lib.registry.docker.DockerRegistryClient
import de.difuture.ekut.pht.lib.registry.train.TrainRegistryClient
import de.difuture.ekut.pht.lib.registry.train.arrival.TrainId
import de.difuture.ekut.pht.lib.registry.train.arrival.tag.TrainTag
import de.difuture.ekut.pht.test.lib.SingleExposedPortContainer
import de.difuture.ekut.pht.test.lib.TEST_TRAIN_REGISTRY_REPOSITORY
import org.junit.*


class TrainArrivalTests {

    companion object {

        // Container that is used for fetching Docker Registry Notifications
        @ClassRule @JvmField
        val REGISTRY : SingleExposedPortContainer =
                SingleExposedPortContainer(
                        TEST_TRAIN_REGISTRY_REPOSITORY,
                        5000)

        // The repositories and tags
        private val tag = TrainTag.of("test")
        private val repo1 = TrainId("train_test_print_summary_1")
        private val repo2 = TrainId("train_test_print_summary_2")
    }

    /////////////////////////  The registry trainRegistryClient  /////////////////////////////////////////////////////////////
    private lateinit var trainRegistryClient : TrainRegistryClient
    private lateinit var dockerClient: StationDockerClient

    @Before
    fun before() {

        this.trainRegistryClient = TrainRegistryClient(
                DockerRegistryClient(REGISTRY.getExternalURI(), GetRestClientImpl())
        )
        this.dockerClient = StationDockerClient(DefaultDockerClient.fromEnv().build())
    }

    @After
    fun after() {
        this.dockerClient.close()
    }

    /////////////////////////  Tests  /////////////////////////////////////////////////////////////

    @Test
    fun get_print_summary() {

        val arrivalPrintSummary1 = this.trainRegistryClient.getTrainArrival(repo1, tag)
        Assert.assertNotNull(arrivalPrintSummary1)

        val arrivalPrintSummary2 = this.trainRegistryClient.getTrainArrival(repo2, tag)
        Assert.assertNotNull(arrivalPrintSummary2)
    }


    // Test for executing print summary traisn
    @Test
    fun execute_print_summary() {

        val arrival1 = this.trainRegistryClient.getTrainArrival(repo1, tag)
        val arrival2 = this.trainRegistryClient.getTrainArrival(repo2, tag)


        // Print summary1
        if (arrival1 != null) {

            Assert.assertEquals("TEST_PRINT_SUMMARY_1\n",
                    arrival1.printSummary(this.dockerClient, 5))
        } else {
            Assert.fail("Arrival for print_summary_1 is null")
        }

        // Print summary2
        if (arrival2 != null) {

            Assert.assertEquals("TEST_PRINT_SUMMARY_2\n",
                    arrival2.printSummary(this.dockerClient, 5))
        } else {
            Assert.fail("Arrival for print_summary_2 is null")
        }
    }
}