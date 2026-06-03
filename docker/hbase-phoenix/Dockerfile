FROM harisekhon/hbase:2.1

ARG PHOENIX_VERSION=5.1.3
ARG PHOENIX_QS_VERSION=6.0.0

# Phoenix 5.1.3 provides HBase 2.1-compatible coprocessors.
# Phoenix QueryServer was moved to a separate project (phoenix-queryserver) in 5.1.x.
# phoenix-queryserver-6.0.0 is the compatible server for Phoenix 5.1.x / HBase 2.1.
RUN mkdir -p /opt && \
    wget -q "https://archive.apache.org/dist/phoenix/phoenix-${PHOENIX_VERSION}/phoenix-hbase-2.1-${PHOENIX_VERSION}-bin.tar.gz" \
        -O /tmp/phoenix.tar.gz && \
    tar -xzf /tmp/phoenix.tar.gz -C /opt/ && \
    mv /opt/phoenix-hbase-2.1-${PHOENIX_VERSION}-bin /opt/phoenix && \
    rm /tmp/phoenix.tar.gz && \
    # Copy Phoenix server-side coprocessor JAR into HBase lib so coprocessors load on RegionServer startup
    find /opt/phoenix -maxdepth 1 -name "phoenix-server*.jar" -exec cp {} /hbase/lib/ \; && \
    # Download Phoenix QueryServer 6.0.0 (fat JAR, includes Avatica HTTP server)
    wget -q "https://archive.apache.org/dist/phoenix/phoenix-queryserver-${PHOENIX_QS_VERSION}/phoenix-queryserver-${PHOENIX_QS_VERSION}-bin.tar.gz" \
        -O /tmp/phoenix-qs.tar.gz && \
    tar -xzf /tmp/phoenix-qs.tar.gz -C /tmp/ && \
    cp /tmp/phoenix-queryserver-${PHOENIX_QS_VERSION}/phoenix-queryserver-${PHOENIX_QS_VERSION}.jar /opt/phoenix/ && \
    cp /tmp/phoenix-queryserver-${PHOENIX_QS_VERSION}/lib/*.jar /opt/phoenix/lib/ && \
    rm -rf /tmp/phoenix-qs.tar.gz /tmp/phoenix-queryserver-${PHOENIX_QS_VERSION} && \
    echo "Phoenix ${PHOENIX_VERSION} + QueryServer ${PHOENIX_QS_VERSION} installed at /opt/phoenix"

ENV PHOENIX_HOME=/opt/phoenix

COPY entrypoint-wrapper.sh /entrypoint-wrapper.sh
RUN chmod +x /entrypoint-wrapper.sh

ENTRYPOINT ["/entrypoint-wrapper.sh"]
