FROM openjdk:8-jdk as ar4k-builder
RUN apt-get update -y && apt-get install curl gnupg bash sed grep coreutils wget -y
COPY ar4k-activemq/ /ar4kAgent/ar4k-activemq/
COPY ar4k-ai/ /ar4kAgent/ar4k-ai/
COPY ar4k-ai-nlp/ /ar4kAgent/ar4k-ai-nlp/
COPY ar4k-api/ /ar4kAgent/ar4k-api/
COPY ar4k-core/ /ar4kAgent/ar4k-core/
COPY ar4k-hazelcast/ /ar4kAgent/ar4k-hazelcast/
COPY ar4k-industrial/ /ar4kAgent/ar4k-industrial/
COPY ar4k-kettle/ /ar4kAgent/ar4k-kettle/
COPY ar4k-pcap/ /ar4kAgent/ar4k-pcap/
COPY ar4k-seed/ /ar4kAgent/ar4k-seed/
COPY ar4k-serial-comm/ /ar4kAgent/ar4k-serial-comm/
COPY ar4k-terminal/ /ar4kAgent/ar4k-terminal/
COPY ar4k-vaadin/ /ar4kAgent/ar4k-vaadin/
COPY ar4k-video/ /ar4kAgent/ar4k-video/
COPY ar4k-watson/ /ar4kAgent/ar4k-watson/
COPY ar4k-agent-qa/ /ar4kAgent/ar4k-agent-qa/
COPY ar4k-agent-web/ /ar4kAgent/ar4k-agent-web/
COPY ar4k-agent-watson/ /ar4kAgent/ar4k-agent-watson/
COPY ar4k-agent-small/ /ar4kAgent/ar4k-agent-small/
COPY ar4k-agent-druido/ /ar4kAgent/ar4k-agent-druido/
COPY gradlew /ar4kAgent/gradlew
COPY gradle/ /ar4kAgent/gradle/
COPY build.gradle /ar4kAgent/build.gradle
COPY settings.gradle /ar4kAgent/settings.gradle
WORKDIR /ar4kAgent
ENV PATH="/ar4kAgent/.gradle/nodejs/node-v6.9.1-linux-x64/bin:${PATH}"
RUN chmod +x gradlew
RUN ./gradlew clean ar4kBootJarQa -Dorg.gradle.jvmargs="-Xms512M -Xmx4G" -x signArchives --info

FROM openjdk:8-jdk-alpine
RUN apk add --no-cache bash gawk sed grep bc coreutils wget binutils
COPY --from=ar4k-builder /ar4kAgent/ar4k-agent-qa/build/libs/ar4k-agent-qa-*.jar /ar4kAgent.jar
ENTRYPOINT ["java"]
CMD ["-XX:+UnlockExperimentalVMOptions","-Djava.net.preferIPv4Stack=true","-XX:+UseCGroupMemoryLimitForHeap","-XshowSettings:vm","-Djava.security.egd=file:/dev/./urandom","-jar","/ar4kAgent.jar"]
