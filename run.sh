#!/bin/sh

java \
    -XX:+PrintFlagsFinal \
    -XX:+UnlockExperimentalVMOptions \
    -XX:+UseCGroupMemoryLimitForHeap \
    -XX:MaxMetaspaceSize=128M \
    -XX:+CMSClassUnloadingEnabled \
    -XX:SharedCacheHardLimit=64M \
    -XX:+IdleTuningGcOnIdle \
    -Xtune:virtualized \
    -Xshareclasses:cacheDir=/opt/shareclasses \
    -jar \
    /opt/app/app.jar
