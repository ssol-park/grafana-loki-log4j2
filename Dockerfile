# Base image for Tomcat
FROM tomcat:9.0-jdk8

# Install basic tools including curl
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    vim \
    wget \
    && rm -rf /var/lib/apt/lists/* \
    && echo "Curl, Vim, and Wget installed successfully"


# Create log directory
RUN mkdir -p /data/log

# Set environment variables
ENV LOG_DIR=/data/log
ENV JAVA_OPTS="-Dspring.profiles.active=dev -Dservice.name=app-tz"

# Copy WAR file to Tomcat webapps directory and rename it to ROOT.war
COPY target/loki-log4j2.war /usr/local/tomcat/webapps/ROOT.war

# Set permissions for log directory
RUN chmod -R 755 /data/log

# Expose default Tomcat port
EXPOSE 8080

# Start Tomcat server
CMD ["catalina.sh", "run"]
