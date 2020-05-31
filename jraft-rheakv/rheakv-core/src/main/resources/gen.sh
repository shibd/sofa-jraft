#!/bin/bash
protoc -I=./  --java_out=../java/ rheakv.proto rheakv-t1.proto
