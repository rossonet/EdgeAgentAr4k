FROM openjdk:8-jdk as ar4k-builder
RUN apt-get update -yq \
    && apt-get install curl gnupg bash sed grep coreutils wget -yq
COPY gradle/ /ar4kAgent/gradle/
COPY gradlew /ar4kAgent/gradlew
COPY LICENSE /ar4kAgent/LICENSE
COPY build.gradle /ar4kAgent/build.gradle
COPY settings.gradle /ar4kAgent/settings.gradle
COPY core/ /ar4kAgent/core/
COPY flow/ /ar4kAgent/flow/
COPY flow-full/ /ar4kAgent/flow-full/
COPY iot/ /ar4kAgent/iot/
COPY blockchain/ /ar4kAgent/blockchain/
COPY onion/ /ar4kAgent/onion/
COPY reactor-web/ /ar4kAgent/reactor-web/
COPY terminal/ /ar4kAgent/terminal/
COPY agent-small/ /ar4kAgent/agent-small/
COPY agent-full/ /ar4kAgent/agent-full/
COPY agent/ /ar4kAgent/agent/
WORKDIR /ar4kAgent
ENV PATH="/ar4kAgent/.gradle/nodejs/node-v6.9.1-linux-x64/bin:${PATH}"
RUN chmod +x gradlew
RUN ./gradlew clean ar4kBootJar --info

FROM openjdk:8-jdk-alpine
RUN apk add --no-cache bash gawk sed grep bc coreutils wget binutils
COPY --from=ar4k-builder /ar4kAgent/agent/build/libs/agent.jar /ar4kAgent.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/ar4kAgent.jar"]
