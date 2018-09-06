# Station
Very simple station implementation for the JVM.

# Introduction

Very simple, prototypical, station implementation. It currently performs the following steps:

1. Scans the registered Docker Registry and Namespace for all trains with the tag `immediate`.
If a Docker Registry repository starts with the string `train_`, it will be considered a train.

2. For any image with the tag `immediate`, checks whether the station has already processed the train.
For this, the station checks whether there already is an image with the station ID as a tag.

3. If the station has not already processed the train, the station pulls the image with the tag `immediate`.

4. The station then tries to run the train via the train API using the endpoint `run_algorithm`.

5. The station then looks at the exit code of the exited container:
    * If 0: Then a new image is created from the exited container, tagged with the Station ID and pushed back to the
      registry.
    * If not 0: The station deletes the failed container and forgets about it.



A graphic version of the flow can be seen here:
```
https://drive.google.com/file/d/1i_z3DhwUW5ea-9FlSvARpiL9rAEhCzhx/view?usp=sharing
```

# Installation

The Station can be installed either from source or as an pre-built artifact.


# Prerequisites

This is Spring Boot Application for the JVM. So you need a Java Runtime Environment installed on your host.
The artifact is tested with this Java version here:

```
openjdk version "1.8.0_181"
```

If you want to build the project from source, you will also need to install a fairly recent version of Gradle.
The built process was tested with Gradle 4.9. 

## Pre-built

The pre-built artifact is available here:

```
https://artifactory.difuture.de/webapp/#/artifacts/browse/tree/General/pht/de/difuture/ekut/pht/station
```

## From Source
This is recommended if you want to develop this application. Just clone the source tree:
```
git clone https://github.com/PersonalHealthTrain/station
```
and build the artifact:
```
gradle assemble
```
This will produce the jar file in the `build/libs` directory.


# Configuration

TODO