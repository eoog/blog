#!/bin/bash

docker build -f msa-course-service/Dockerfile -t msa-course-service .
docker build -f msa-discovery/Dockerfile -t msa-discovery .
docker build -f msa-enrollment-service/Dockerfile -t msa-enrollment-service .
docker build -f msa-file-manage-service/Dockerfile -t msa-file-manage-service .
docker build -f msa-gateway/Dockerfile -t msa-gateway .
docker build -f msa-graphql/Dockerfile -t msa-graphql .
docker build -f msa-playback-service/Dockerfile -t msa-playback-service .
docker build -f msa-user-service/Dockerfile -t msa-user-service .