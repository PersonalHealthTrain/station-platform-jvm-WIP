package de.difuture.ekut.pht.station

import com.spotify.docker.client.DefaultDockerClient
import de.difuture.ekut.pht.lib.common.docker.DockerRepositoryName
import de.difuture.ekut.pht.lib.common.docker.DockerTag
import de.difuture.ekut.pht.lib.common.docker.HostPort
import de.difuture.ekut.pht.test.lib.SingleExposedPortContainer
import org.junit.*


class StationDockerClientTests {

    companion object {

        // Container that is used for fetching Docker Registry Notifications
        @ClassRule @JvmField
        val REGISTRY : SingleExposedPortContainer =
                SingleExposedPortContainer(
                        "lukaszimmermann/pht-test-train-registry:latest",
                        5000)

        // Print summary commands for the container
        val printSummary = listOf("print_summary")

        // Timeout
        const val timeout = 5
    }


    private lateinit var client : StationDockerClient

    // Repositories in the test registry
    private lateinit var repo1 : DockerRepositoryName
    private lateinit var repo2 : DockerRepositoryName
    private lateinit var tag : DockerTag


    @Before
    fun before() {

        val hostPort = HostPort("localhost", REGISTRY.mappedPort)
        this.client = StationDockerClient(DefaultDockerClient.fromEnv().build())
        this.repo1 = DockerRepositoryName("train_test_print_summary_1",
                hostPort = hostPort)
        this.repo2 = DockerRepositoryName("train_test_print_summary_2",
                hostPort = hostPort)
        this.tag = DockerTag("test")
    }

    @After
    fun after() {

        this.client.close()
    }


    //////////////////////////////////////// PULL ////////////////////////////////////////////////////////////


    /// Pull some trains from local registry, and remove
    @Test
    fun pull_rm() {

        // Images
        val image1 = this.client.pull(repo1, this.tag)
        val image2 = this.client.pull(repo2, this.tag)

        this.client.rmi(image1, force = true)
        this.client.rmi(image2, force = true)
    }


    @Test
    fun run_rm() {

        // Images
        val image1 = this.client.pull(repo1, this.tag)
        val image2 = this.client.pull(repo2, this.tag)

        val output1 = this.client.run(image1, printSummary, true, timeout)
        val output2 = this.client.run(image2, printSummary, true, timeout)

        // Assert that the test summary trains have produced the correct output
        Assert.assertEquals("TEST_PRINT_SUMMARY_1\n", output1.stdout)
        Assert.assertEquals("TEST_PRINT_SUMMARY_2\n", output2.stdout)

        // Assert that the test Train
        this.client.rmi(image1, force = true)
        this.client.rmi(image2, force = true)
    }

}
