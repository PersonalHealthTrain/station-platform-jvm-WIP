package de.difuture.ekut.pht.station

import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.messages.ContainerConfig
import de.difuture.ekut.pht.lib.common.docker.DockerContainerOutput
import de.difuture.ekut.pht.lib.common.docker.DockerRepositoryName
import de.difuture.ekut.pht.lib.common.docker.DockerTag
import de.difuture.ekut.pht.lib.runtime.DockerClientException
import de.difuture.ekut.pht.lib.runtime.IDockerClient


class StationDockerClient(private val client : DockerClient) : IDockerClient {

    override fun log(containerId: String): String  {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        this.client.close()
    }

    override fun pull(repo: DockerRepositoryName, tag: DockerTag): String {

        // Resolve the Docker Repository name against the tag
        val resolved = repo.resolveTag(tag)

        // Pull the image
        this.client.pull(resolved)

        // Figure  out the image ID
        val images = this.client.listImages().filter {

            it.repoTags()?.contains( resolved ) ?: false
        }

        // The tuple of repository and tag should be unique in the image list
        if (images.size != 1) {

            throw IllegalStateException("Expected only one image, but found: ${images.size}")
        }
        return images.first().id()
    }


    override fun rm(containerId: String) {

        this.client.removeContainer(containerId)
    }

    override fun run(imageId: String, commands: List<String>, rm : Boolean, timeout : Int): DockerContainerOutput {

        // Create container
        val config = ContainerConfig.builder()
                .image(imageId)
                .cmd(commands)
                .build()
        val containerId = this.client.createContainer(config).id()?.let { it } ?:
                throw DockerClientException("Failed to run Docker image with id: $imageId")

        // start the container
        this.client.startContainer(containerId)

        // Wait for the container to exit, throw exception of the status code is not zero
        val exitCode = this.client.waitContainer(containerId).statusCode()
        val stdout = this.client.logs(containerId, DockerClient.LogsParam.stdout()).readFully()
        val stderr = this.client.logs(containerId, DockerClient.LogsParam.stderr()).readFully()

        if (rm) {
            this.rm(containerId)
        }
        return DockerContainerOutput(containerId,  exitCode, stdout, stderr)
    }

    override fun rmi(imageId: String, force: Boolean) {

        val prefix = "sha256:"
        val imgID = if(imageId.startsWith(prefix)) {imageId.removePrefix(prefix)} else {imageId}
        this.client.removeImage(imgID, true, false)
    }


    override fun push(repo: DockerRepositoryName, tag: DockerTag) {

        this.client.push(repo.resolveTag(tag))
    }

    override fun tag(sourceImageId: String, targetRepo: DockerRepositoryName, targetTag: DockerTag) {

        this.client.tag(
                sourceImageId,
                targetRepo.resolveTag(targetTag))
    }
}
