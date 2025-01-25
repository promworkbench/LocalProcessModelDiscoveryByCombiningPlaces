# Use a base image that has Java and Ant installed
FROM openjdk:11-jdk-slim AS builder

# Set environment variables for ANT and IVY versions
ENV ANT_VERSION=1.10.12
ENV IVY_VERSION=2.5.0

# Install Apache Ant
RUN apt-get update && \
    apt-get install -y ant && \
    apt-get clean

# Set ANT_HOME environment variable
ENV ANT_HOME=/opt/ant
ENV IVY_HOME=/opt/ant/lib
ENV PATH="${PATH}:${ANT_HOME}/bin"

# Set the working directory inside the container
WORKDIR /app

# Copy the build.xml and ivy.xml files into the container
COPY build-docker.xml ./build.xml
COPY ivy.xml .
COPY ivysettings.xml .

# Copy the source code into the container
COPY src ./src

# Run Ant to resolve dependencies and build the JAR file
RUN ant -buildfile build.xml init-ivy
RUN ant -buildfile build.xml make


# Use a smaller OpenJDK image for running the application
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy built JAR file from builder stage (adjust path as necessary)
COPY --from=builder /app/dist/lib ./lib
COPY --from=builder /app/dist/lpm-clustering.jar .
COPY --from=builder /app/dist/lpm-discovery.jar .

# Command for welcome
# run a script for welcome message