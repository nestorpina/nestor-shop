mvn gae:stop
mvn resources:resources compiler:compile war:exploded
mvn gae:debug -Denv=local
