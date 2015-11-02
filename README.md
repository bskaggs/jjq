jjq: jq for Java
================

jjq let's you use [jq](https://stedolan.github.io/jq/) to slice and dice JSON in Java.  This library primarily exists to let you use jq with Hadoop MapReduce as part of [hjq](https://github.com/bskaggs/hjq).

jjq is heavily inspired by [jq.py](https://github.com/mwilliamson/jq.py) and [ruby-jq](https://github.com/winebarrel/ruby-jq), but any imperfections are my own.

jjq uses [Java Native Access](https://github.com/java-native-access/jna) (JNA) to call the libjq native library.  Some Linux distributions like Ubuntu don't package the library with jq, so you will need to be sure that jq is compiled from source and that libjq is installed in a place from which JNA can load it.
