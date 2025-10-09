# Usa la imagen base de Jenkins LTS
FROM jenkins/jenkins:lts

# Cambia a usuario root para instalar dependencias
USER root

# Instala paquetes necesarios y Docker CLI dentro del contenedor
RUN apt-get update && \
    apt-get install -y \
        apt-transport-https \
        ca-certificates \
        curl \
        gnupg-agent \
        software-properties-common \
        wget && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - && \
    echo "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list && \
    apt-get update && \
    apt-get install -y docker-ce-cli && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Instala Maven (desde el repositorio de archivos antiguos de Apache)
ARG MAVEN_VERSION=3.9.8
RUN wget --no-verbose https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -P /tmp/ && \
    tar xzf /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt/ && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/local/bin/mvn && \
    rm /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz

# Variables de entorno para Maven
ENV MAVEN_HOME=/opt/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

# ✅ Crea el grupo 'docker' si no existe y agrega el usuario Jenkins
RUN groupadd -f -r docker && usermod -aG docker jenkins

# Cambia de nuevo al usuario Jenkins
USER jenkins
