#!/bin/bash

# ------------------------------------------------------------
# Copyright (c) Microsoft Corporation.  All rights reserved.
# Licensed under the MIT License (MIT). See License.txt in the repo root for license information.
# ------------------------------------------------------------


CREATE_TABLE_FILE=false

log() {
  logger "SF-DCA $1"
  echo "SF-DCA $1"
}

#create a new session with the given session name
createSession() {
  fabricLogRoot=${FabricLogRoot}

  # folder name contains service fabric version so correct manifest can be used for decoding.
  logPath="${fabricLogRoot}/lttng-traces/fabric_traces_${2}_$(date '+%Y%m%d-%H%M%S')"

  log "Creating session $1 with log output path ${logPath}"

  lttng create $1 --output=${logPath}

  # Calculating number of traces files so that we have a disk buffer of traces of 500MB (TODO - this needs to be configurable in the future)
  # Forcing each stream to have 64MB

  # tracefileSize in bytes
  tracefileSize=67108864
  tracefileCount=$(( 524288000 / $tracefileSize / `nproc` ))

  # flush period of trace events is 60s (60000000 us)
  log "WARN: Inside Docker container, using smaller subbuf size to workaround a bug"
  lttng enable-channel --session ${1} --userspace --switch-timer 60000000 --tracefile-size ${tracefileSize} --tracefile-count ${tracefileCount} --subbuf-size 8192 channel0
  lttng enable-channel --session ${1} --userspace --switch-timer 60000000 --tracefile-size ${tracefileSize} --tracefile-count ${tracefileCount} --subbuf-size 8192 metadata

  lttng enable-event --channel channel0 --userspace "service_fabric:*"
  lttng add-context -u -t vtid
  lttng add-context -u -t vpid

  log "Starting session $1"
  lttng start
}

# Looks for all ServiceFabric_[0-9] and WindowsFabric_[0-9] sessions and destroys them.
destroyExistingSFSessions() {
  log "Destroying all existing Service Fabric Sessions"
  echo "$(lttng list | grep -Eo "(ServiceFabric|WindowsFabric)_[0-9]\s" | sed -n -e  's/\(\(ServiceFabric\|WindowsFabric\)_[0-9]\)\s/\1/p' | xargs -I {} lttng destroy {})"
}

# Start tracing
echo "Starting tracing session"
if command -v lttng >/dev/null 2>&1; then
  log "LTTng start $(date)"

  sessionName="ServiceFabric_0"
  serviceFabricVersion=$(cat ${FabricCodePath}/ClusterVersion)

  # Destroys all existing Service Fabric Lttng sessions
  log "$(destroyExistingSFSessions)"

  # Creates new Session
  log "$(createSession $sessionName $serviceFabricVersion)"

  log "LTTng end $(date)"
else
    echo "LTTng is not installed." >&2;
fi