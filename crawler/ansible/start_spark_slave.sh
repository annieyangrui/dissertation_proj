#!/bin/bash
~/spark/sbin/stop-slave.sh
~/spark/sbin/start-slave.sh spark://115.146.84.222:7077 -m 6G