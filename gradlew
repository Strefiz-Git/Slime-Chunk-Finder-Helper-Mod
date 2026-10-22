#!/usr/bin/env sh

APP_HOME=cd "\${0%/*}" 2>/dev/null; pwd "\" || echo .

APP_NAME="Gradle"
CLASSPATH=\/gradle/wrapper/gradle-wrapper.jar

if [ -n "\" ]; then
  JAVA=\/bin/java
else
  java=which java 2>/dev/null || echo java
  JAVA=\
fi

\ -Dorg.gradle.appname=\ -classpath \ org.gradle.wrapper.GradleWrapperMain "\$@"
