#!/usr/bin/env bash
# 封装 HBase 原始 entrypoint，并在所有 HBase 服务启动后自动启动 Phoenix QueryServer

set -euo pipefail

PHOENIX_HOME="${PHOENIX_HOME:-/opt/phoenix}"
PHOENIX_LOG_DIR="/hbase/logs"

start_phoenix_queryserver() {
    echo "Starting Phoenix QueryServer on port 8765..."
    mkdir -p "$PHOENIX_LOG_DIR"

    # Classpath: Phoenix QueryServer JAR + all Phoenix deps + HBase conf/libs
    PHOENIX_CP="${PHOENIX_HOME}/*:${PHOENIX_HOME}/lib/*:/hbase/conf:/hbase/lib/*:/hbase/lib/client-facing-thirdparty/*"

    nohup /usr/bin/java \
        -cp "${PHOENIX_CP}" \
        -Dproc_phoenixserver \
        -Dlog4j.configuration=file:${PHOENIX_HOME}/bin/log4j.properties \
        -Dpsql.root.logger=INFO,DRFA \
        -Dpsql.log.dir=${PHOENIX_LOG_DIR} \
        -Dpsql.log.file=queryserver.log \
        org.apache.phoenix.queryserver.server.QueryServer \
        > ${PHOENIX_LOG_DIR}/queryserver_stdout.log 2>&1 &
    echo "Phoenix QueryServer started (PID $!)"
}

# 先用原脚本启动 ZooKeeper/Master/RegionServer/REST/Thrift
export HBASE_HOME="/hbase"
export JAVA_HOME="${JAVA_HOME:-/usr}"
mkdir -pv "$HBASE_HOME/logs"

sed -i 's/zookeeper:2181/localhost:2181/' "$HBASE_HOME/conf/hbase-site.xml" 2>/dev/null || true

echo "Starting Zookeeper..."
"$HBASE_HOME/bin/hbase" zookeeper >"$HBASE_HOME/logs/zookeeper.log" 2>&1 &
sleep 5

echo "Starting HBase Master..."
"$HBASE_HOME/bin/hbase-daemon.sh" start master
sleep 5

echo "Starting HBase RegionServer..."
"$HBASE_HOME/bin/hbase-daemon.sh" start regionserver
sleep 3

echo "Starting HBase REST (Stargate)..."
"$HBASE_HOME/bin/hbase-daemon.sh" start rest 2>/dev/null || true

echo "Starting HBase Thrift..."
"$HBASE_HOME/bin/hbase-daemon.sh" start thrift 2>/dev/null || true

# 等 HBase 完全就绪后再启动 Phoenix QueryServer
sleep 10
start_phoenix_queryserver

echo "All services started."
tail -f /dev/null "$HBASE_HOME/logs/"* &
wait
