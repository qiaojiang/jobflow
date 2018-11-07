#!/bin/bash

basepath=$(dirname $0)

jarfile=$basepath"/jobflow.jar"

/usr/local/java/bin/java -cp $jarfile com.qj.schedule.service.ScheduleMasterV2


