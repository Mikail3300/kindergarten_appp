#!/usr/bin/env sh

DIR="$(cd "$(dirname "$0")" && pwd)"
APP_BASE_NAME=$(basename "$0")
CLASSPATH=$DIR/gradle/wrapper/gradle-wrapper.jar

if [ -n "$JAVA_HOME" ] ; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

exec "$JAVACMD" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
