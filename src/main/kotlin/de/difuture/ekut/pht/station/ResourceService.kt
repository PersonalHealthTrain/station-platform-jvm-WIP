package de.difuture.ekut.pht.station

import de.difuture.ekut.pht.lib.common.PHT_RESOURCE_PREFIX
import org.springframework.stereotype.Service


@Service
class ResourceService {

    /**
     * Fetches the environment variables that point
     *
     */
    fun getResources() : Map<String, String>
            = System.getenv().filterKeys { it.startsWith(PHT_RESOURCE_PREFIX) }
}
