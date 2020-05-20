#!/bin/bash
protoc -I=./  --descriptor_set_out=raft.desc --java_out=../java/ counter.proto
