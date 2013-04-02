#!/bin/sh
mvn gae:stop
mvn resources:resources compiler:compile war:exploded
mvn gae:run -Denv=local
