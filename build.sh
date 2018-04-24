#!/bin/sh

# Build the app
mvn clean package

STATUS=$?

if [ ${STATUS} -eq 0 ]; then
    # Build the docker image and tag as latest
    docker build --tag example-v1:latest .

    # Run the platform image
    docker run -it --rm \
        --publish 8080:8080 \
        --env APP_ENV=test \
        --name example-v1 \
        example-v1:latest
else
    echo "Build failed"
fi
