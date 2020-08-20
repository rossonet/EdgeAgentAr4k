FROM openjdk:8-jdk as ar4k-builder
RUN apt-get update -y && apt-get install curl gnupg bash sed grep coreutils wget -y
COPY ar4k-seed/ /ar4kAgent/ar4k-seed/
COPY ar4k-activemq/ /ar4kAgent/ar4k-activemq/
COPY ar4k-iec61499/ /ar4kAgent/ar4k-iec61499/
COPY ar4k-serial-comm/ /ar4kAgent/ar4k-serial-comm/
COPY ar4k-agent-qa/ /ar4kAgent/ar4k-agent-qa/
COPY ar4k-core/ /ar4kAgent/ar4k-core/
COPY ar4k-pcap/ /ar4kAgent/ar4k-pcap/
COPY ar4k-terminal/ /ar4kAgent/ar4k-terminal/
COPY ar4k-hazelcast/ /ar4kAgent/ar4k-hazelcast/
COPY ar4k-siddhi/ /ar4kAgent/ar4k-siddhi/
COPY ar4k-video/ /ar4kAgent/ar4k-video/
COPY gradlew /ar4kAgent/gradlew
COPY gradle/ /ar4kAgent/gradle/
COPY build.gradle /ar4kAgent/build.gradle
COPY settings.gradle /ar4kAgent/settings.gradle
WORKDIR /ar4kAgent
ENV PATH="/ar4kAgent/.gradle/nodejs/node-v6.9.1-linux-x64/bin:${PATH}"
RUN chmod +x gradlew
RUN ./gradlew clean ar4kBootJarQa --info

FROM openjdk:8-jdk-alpine
RUN apk add --no-cache bash gawk sed grep bc coreutils wget binutils
COPY --from=ar4k-builder /ar4kAgent/ar4k-agent-qa/build/libs/ar4k-agent-qa-*.jar /ar4kAgent.jar
ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions","-Djava.net.preferIPv4Stack=true","-XX:+UseCGroupMemoryLimitForHeap","-XshowSettings:vm","-Djava.security.egd=file:/dev/./urandom","-jar","/ar4kAgent.jar"]
