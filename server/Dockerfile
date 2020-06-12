FROM openjdk:8-jre
ADD target/oxiles-server.jar oxiles-server.jar
ADD docker-scripts/start-oxiles.sh start-oxiles.sh
ENV CONF ""
EXPOSE 8060
CMD chmod +x start-oxiles.sh && ./start-oxiles.sh