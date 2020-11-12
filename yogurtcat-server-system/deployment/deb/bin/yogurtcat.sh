#!/bin/bash

# Copyright 1999-2018 Alibaba Group Holding Ltd.
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at

#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
linux=false
case "`uname`" in
Linux*) linux=true;;
esac
error_exit ()
{
    echo "ERROR: $1 !!"
    exit 1
}
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=$HOME/jdk/java
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/java
[ ! -e "$JAVA_HOME/bin/java" ] && unset JAVA_HOME

if [ -z "$JAVA_HOME" ]; then
  JAVA_PATH=`dirname $(readlink -f $(which java))`
  export JAVA_HOME=`dirname $JAVA_PATH 2>/dev/null`
  if [ -z "$JAVA_HOME" ]; then
        error_exit "Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better!"
  fi
fi

export SERVER="yogurtcat-server-system"
export VERSION="0.0.1-SNAPSHOT"
export JAVA_HOME
export JAVA="$JAVA_HOME/bin/java"
export BASE_DIR=`cd $(dirname $0)/..; pwd`
export CONFIG_SEARCH_LOCATIONS="/opt/${SERVER}/etc"
export DEBUG_PORT="0.0.0.0:8000"

#===========================================================================================
# JVM Configuration
#===========================================================================================
JAVA_OPT="${JAVA_OPT} -server -Xms2g -Xmx2g -Xmn1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/${SERVER}/log/java_heapdump.hprof"
JAVA_OPT="${JAVA_OPT} -XX:-UseLargePages"

JAVA_MAJOR_VERSION=$($JAVA -version 2>&1 | sed -E -n 's/.* version "([0-9]*).*$/\1/p')
if [[ "$JAVA_MAJOR_VERSION" -ge "9" ]] ; then
  JAVA_OPT="${JAVA_OPT} -Xlog:gc*:file=/opt/${SERVER}/log/yogurtcat_server_user_gc.log:time,tags:filecount=10,filesize=102400"
else
  JAVA_OPT="${JAVA_OPT} -Djava.ext.dirs=${JAVA_HOME}/jre/lib/ext:${JAVA_HOME}/lib/ext"
  JAVA_OPT="${JAVA_OPT} -Xloggc:/opt/${SERVER}/log/yogurtcat_server_user_gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
fi
JAVA_OPT="${JAVA_OPT} -jar /opt/${SERVER}/lib/${SERVER}-${VERSION}.jar"
JAVA_OPT="${JAVA_OPT} --spring.config.location=${CONFIG_SEARCH_LOCATIONS}/"
JAVA_OPT="${JAVA_OPT} --server.max-http-header-size=524288"

echo "$JAVA ${JAVA_OPT}"

# check the start.out log output file
if [ ! -f "/opt/${SERVER}/log/start.out" ]; then
  touch "/opt/${SERVER}/log/start.out"
fi


function start() {
  echo "$JAVA ${JAVA_OPT}" > /opt/${SERVER}/log/start.out 2>&1 &
  $JAVA ${JAVA_OPT}  >> /opt/${SERVER}/log/start.out 2>&1  < /dev/null
  echo "yogurtcat_server_user is starting，you can check the /opt/${SERVER}/log/start.out"
}

function stop() {
    pid=`ps ax | grep -i ${SERVER} | grep java | grep -v grep | awk '{print $1}'`
    if [ -z "$pid" ] ; then
            echo "No ${SERVER} running."
            exit -1;
    fi
    echo "The ${SERVER}(${pid}) is running..."
    kill -9 ${pid}
    echo "Send shutdown request to ${SERVER}(${pid}) OK" > /opt/${SERVER}/log/start.out 2>&1 &
}

function restart() {
    stop
    sleep 5
    start
}

function debug() {
  JAVA_OPT="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=${DEBUG_PORT},suspend=n ${JAVA_OPT}"
  echo "$JAVA ${JAVA_OPT}" > /opt/${SERVER}/log/start.out 2>&1 &
  nohup $JAVA ${JAVA_OPT}  >> /opt/${SERVER}/log/start.out 2>&1 &
  echo "yogurtcat_server_user is starting，you can check the /opt/${SERVER}/log/start.out"
}

function help() {
  echo "Usage: yogurtcat.sh ( commands ... )"
  echo "commands:"
  echo "  start             Start Yogurtcat"
  echo "  stop              Stop Yogurtcat"
  echo "  restart           Restart Yogurtcat"
  echo "  debug             Start Yogurtcat under JDWP debugger"
  echo "Note: Waiting for the process to end and use of the -force option require that \$CATALINA_PID is defined"

}

function main() {
  case $1 in
    start)
      start
      ;;
     stop)
      stop
      ;;
    restart)
      restart
      ;;
    debug)
      debug
      ;;
    *)
        help
        ;;
        esac
}

main "$@"
