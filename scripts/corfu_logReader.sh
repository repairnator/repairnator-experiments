#!/usr/bin/env bash

if [ "$JAVA_HOME" != "" ]; then
  JAVA="$JAVA_HOME/bin/java"
else
  JAVA=java
fi

CORFUDBBINDIR="${CORFUDBBINDIR:-/usr/bin}"
CORFUDB_PREFIX="${CORFUDBBINDIR}/.."

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

if ls "${DIR}"/../logReader/target/*.jar > /dev/null 2>&1; then
  CLASSPATH=("${DIR}"/../logReader/target/logReader-*-shaded.jar)
else
  CLASSPATH=("${CORFUDB_PREFIX}"/share/corfu/lib/logReader-*-shaded.jar)
fi

# Windows (cygwin) support
case "`uname`" in
    CYGWIN*) cygwin=true ;;
    *) cygwin=false ;;
esac

if $cygwin
then
    CLASSPATH=`cygpath -wp "$CLASSPATH"`
fi

RUN_AS=`basename $0`
exec "$JAVA" -cp "$CLASSPATH" $JVMFLAGS org.corfudb.logReader.logReader $*
