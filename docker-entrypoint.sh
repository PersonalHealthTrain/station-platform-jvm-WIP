#!/bin/bash

# Check all the variables that need to be set for the station to work
if [ -z ${STATION_NAME+x} ]; then echo "STATION_NAME is unset!"; exit 1 ;fi
if [ -z ${STATION_ID+x} ]; then echo "STATION_ID is unset!"; exit 1 ;fi
if [ -z ${STATION_DOCKER_NETWORK+x} ]; then echo "STATION_DOCKER_NETWORK is unset!"; exit 1 ;fi
if [ -z ${STATION_REGISTRY_URI+x} ]; then echo "STATION_REGISTRY_URI is unset"; exit 1 ;fi
if [ -z ${STATION_REGISTRY_NAMESPACE+x} ]; then echo "STATION_REGISTRY_NAMESPACE is unset"; exit 1 ;fi
if [ -z ${STATION_REGISTRY_USERNAME+x} ]; then echo "STATION_REGISTRY_USERNAME is unset!"; exit 1 ;fi
if [ -z ${STATION_NAME+x} ]; then echo "var is unset"; exit 1 ;fi
if [ -z ${STATION_REGISTRY_PASSWORD+x} ]; then echo "STATION_REGISTRY_PASSWORD is unset"; exit 1 ;fi

exec gradle bootRun --no-daemon
