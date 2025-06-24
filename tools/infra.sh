#!/usr/bin/env bash

set -e
SUFFIX=${2:-dev}
NAME="eletrohub-${SUFFIX}"

start=false
stop=false
resetdb=false
status=false

usage()
{
    echo "Usage: ./tools/infra.sh [OPTION]"
    echo "Runs EletroHub's basic infrastructure in development mode"
    echo "Examples:"
    echo "          infra start [NAME]"
    echo "          infra stop [NAME]"
    echo "          infra resetdb [NAME]"
    echo "          infra status [NAME]"
    echo ""
}

while [ "$1" != "" ]; do
    case $1 in
        start )                                       start=true
                                                      ;;
        stop )                                        stop=true
                                                      ;;
        resetdb )                                     resetdb=true
                                                      ;;
        status )                                      status=true
                                                      ;;
        -h | --help )                                 usage
                                                      exit 2
                                                      ;;
        * )                                       echo "Error: Unknown command '$1'"
                                                  usage
                                                  exit 1
    esac
    shift
done

cd $(readlink -f $(dirname $0))

if [ "$status" = true ]; then
    docker container ls --filter "name=eletrohub-${SUFFIX}"
    exit 0
fi

if [ "$start" = true ]; then
    docker compose --project-name $NAME up -d --build
    exit 0
fi

if [ "$stop" = true ]; then
    docker compose --project-name $NAME down || :
    exit 0
fi


if [ "$resetdb" = true ]; then
    docker compose --project-name "$NAME" rm -f -s -v postgres
    docker volume rm "${NAME}_pgdata"
    docker compose --project-name "$NAME" up -d postgres
    exit 0
fi

usage
