# Git Education Game Server

 此Server為Git Education Game之後端伺服器，負責與MongoDb溝通，處理使用者互動如：登入、註冊、使用者事件紀錄等等。
 
# 專案建置方法

```
mvn clean package
```

# 執行環境

 請使用支援jar(war)之環境運行此spring boot專案，如：OpenJDK、Apache Tomcat等等。
 
# Docker建置範例（Dockerfile）

```
FROM maven:3-jdk-8 as builder

RUN mkdir /workdir

WORKDIR /workdir

COPY . .

RUN apt-get update \
    && apt-get -y install gettext-base \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
	
RUN mvn clean package

FROM tomcat:8-jdk8-adoptopenjdk-hotspot

ARG DEBUG=false
ENV DEBUG=$DEBUG
ENV LOG_LEVEL=$LOG_LEVEL
ENV JAVA_OPTS="-Xmx512m"

RUN echo $DEBUG

RUN mkdir /usr/local/tomcat/images  && \
    mkdir /usr/local/tomcat/temp_images  && \
    if [ ${DEBUG} = true ]; then \
    rm -rf /usr/local/tomcat/webapps/ROOT /usr/local/tomcat/webapps/ROOT.war \
    ;else \
    rm -rf /usr/local/tomcat/webapps/ && mkdir /usr/local/tomcat/webapps/ \
    ;fi

COPY --from=builder /workdir/target/GEG-Server.war /usr/local/tomcat/webapps/ROOT.war

```