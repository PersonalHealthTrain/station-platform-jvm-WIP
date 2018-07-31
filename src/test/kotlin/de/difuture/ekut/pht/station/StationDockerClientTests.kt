package de.difuture.ekut.pht.station

import com.spotify.docker.client.DefaultDockerClient
import de.difuture.ekut.pht.lib.common.docker.DockerRepositoryName
import de.difuture.ekut.pht.lib.common.docker.DockerTag
import de.difuture.ekut.pht.lib.common.docker.HostPort
import de.difuture.ekut.pht.test.lib.SingleExposedPortContainer
import de.difuture.ekut.pht.test.lib.TEST_TRAIN_REGISTRY_REPOSITORY
import org.junit.*


class StationDockerClientTests {

    companion object {

        // Container that is used for fetching Docker Registry Notifications
        @ClassRule @JvmField
        val REGISTRY  = SingleExposedPortContainer(TEST_TRAIN_REGISTRY_REPOSITORY, 5000)

        // Print summary commands for the container
        val printSummary = listOf("print_summary")

        // Timeout
        const val timeout = 5
    }


    private lateinit var client : StationDockerClient

    // Repositories in the test registry
    private lateinit var hostPort : HostPort
    private lateinit var repo1 : DockerRepositoryName
    private lateinit var repo2 : DockerRepositoryName
    private lateinit var tag : DockerTag


    @Before
    fun before() {

        this.hostPort = HostPort("localhost", REGISTRY.mappedPort)
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

    //////////////////////////////////////// HELPER FUNCTION  ////////////////////////////////////////////////

    // Pulls image from remote repository, applies the block and deletes the image again
    private fun <T> withRemoteImage(repo : DockerRepositoryName,  tag : DockerTag,  block : (String) -> T ) : T {

        val imageId = this.client.pull(repo, tag)
        val result = block(imageId)
        this.client.rmi(imageId, true)
        return result
    }


    //////////////////////////////////////// PULL ////////////////////////////////////////////////////////////


    /// Pull some trains from local registry, and remove
    @Test
    fun pull_rm() {

        withRemoteImage(repo1, tag) {}
        withRemoteImage(repo2, tag) {}
    }


    @Test
    fun run_rm() {


        withRemoteImage(repo1, tag) {

            val output1 = this.client.run(it, printSummary, true, timeout)
            Assert.assertEquals("TEST_PRINT_SUMMARY_1\n", output1.stdout)
        }

        withRemoteImage(repo2, tag) {

            val output2 = this.client.run(it, printSummary, true, timeout)
            Assert.assertEquals("TEST_PRINT_SUMMARY_2\n", output2.stdout)
        }
    }


    //////////////////////////////////////// TAG  ////////////////////////////////////////////////////////////
    @Test
    fun tag_push() {

        val pushTag = DockerTag("latest")

        withRemoteImage(this.repo1, this.tag) {

            val name = DockerRepositoryName("testtag", hostPort = this.hostPort)
            this.client.tag(it, name, pushTag)
            this.client.push(name, pushTag)
        }

        withRemoteImage(this.repo2, this.tag) {

            val name = DockerRepositoryName("testtag", hostPort = this.hostPort)
            this.client.tag(it, name, pushTag)
            this.client.push(name, pushTag)
        }

    }



}
