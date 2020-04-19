/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.jraft.rhea.storage;

import com.alipay.sofa.jraft.rhea.metadata.Region;
import com.alipay.sofa.jraft.rhea.options.MemoryDBOptions;
import com.alipay.sofa.jraft.rhea.storage.MemoryKVStoreSnapshotFile.*;
import com.alipay.sofa.jraft.rhea.util.*;
import com.alipay.sofa.jraft.rhea.util.concurrent.DistributedLock;
import com.alipay.sofa.jraft.util.BytesUtil;
import com.codahale.metrics.Timer;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.alipay.sofa.jraft.rhea.storage.MemoryKVStoreSnapshotFile.*;
import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;

/**
 * @author jiachun.fjc
 */
public class MemoryRawKVStoreTest {

    public static void main(String[] args) {
        final ConcurrentNavigableMap<byte[], byte[]> defaultDB = new ConcurrentSkipListMap<>(
            BytesUtil.getDefaultByteArrayComparator());
        // put list
        final byte[] value = writeUtf8("put_example_value");

        defaultDB.put(writeUtf8("00"), value);
        defaultDB.put(writeUtf8("10"), value);
        defaultDB.put(writeUtf8("101"), value);
        defaultDB.put(writeUtf8("102"), value);
        defaultDB.put(writeUtf8("111"), value);
        defaultDB.put(writeUtf8("223"), value);
        defaultDB.put(writeUtf8("323"), value);

        // 3, 10
        byte[] startKey = writeUtf8("3");
        byte[] toKey = writeUtf8("10");
        //        ConcurrentNavigableMap<byte[], byte[]> concurrentNavigableMap = defaultDB.subMap(startKey, toKey);
        ConcurrentNavigableMap<byte[], byte[]> concurrentNavigableMap = defaultDB.descendingMap().subMap(startKey,
            toKey);
        for (byte[] bytes : concurrentNavigableMap.keySet()) {
            System.out.println(readUtf8(bytes));
        }
    }

}
