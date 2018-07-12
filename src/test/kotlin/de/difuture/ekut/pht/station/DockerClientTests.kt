package de.difuture.ekut.pht.station

import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import org.junit.After
import org.junit.Before
import org.junit.Test

class DockerClientTests {

    private lateinit var client : DockerClient

    @Before
    fun before() {

        this.client =  DefaultDockerClient.fromEnv().build()
    }

    @After
    fun after() {

        this.client.close()
    }


    @Test
    fun testPullImageAndRemoveAfterwards() {

        this.client.pull("alpine:latest")
        this.client.removeImage("alpine:latest")
    }
}
