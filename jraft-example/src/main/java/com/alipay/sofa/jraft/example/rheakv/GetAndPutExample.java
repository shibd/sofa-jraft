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
package com.alipay.sofa.jraft.example.rheakv;

import java.util.concurrent.CompletableFuture;

import com.alipay.sofa.jraft.rhea.cmd.proto.RheakvRpc;
import com.alipay.sofa.jraft.rpc.impl.MarshallerHelper;
import com.alipay.sofa.jraft.util.RpcFactoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.sofa.jraft.rhea.client.FutureHelper;
import com.alipay.sofa.jraft.rhea.client.RheaKVStore;

import static com.alipay.sofa.jraft.util.BytesUtil.readUtf8;
import static com.alipay.sofa.jraft.util.BytesUtil.writeUtf8;

/**
 *
 * @author jiachun.fjc
 */
public class GetAndPutExample {

    private static final Logger LOG = LoggerFactory.getLogger(GetAndPutExample.class);

    public static void main(final String[] args) throws Exception {
        // 注册 request 和 response proto
        RpcFactoryHelper.rpcFactory().registerProtobufSerializer(RheakvRpc.BaseRequest.class.getName(),
                RheakvRpc.BaseRequest.getDefaultInstance());
        RpcFactoryHelper.rpcFactory().registerProtobufSerializer(RheakvRpc.BaseResponse.class.getName(),
                RheakvRpc.BaseResponse.getDefaultInstance());

        RpcFactoryHelper.rpcFactory().registerProtobufSerializer(RheakvRpc.GetRequest.class.getName(),
                RheakvRpc.GetRequest.getDefaultInstance());
        RpcFactoryHelper.rpcFactory().registerProtobufSerializer(RheakvRpc.GetResponse.class.getName(),
                RheakvRpc.GetResponse.getDefaultInstance());

        RpcFactoryHelper.rpcFactory().registerProtobufSerializer(RheakvRpc.GetAndPutRequest.class.getName(),
                RheakvRpc.GetAndPutRequest.getDefaultInstance());
        RpcFactoryHelper.rpcFactory().registerProtobufSerializer(RheakvRpc.GetAndPutResponse.class.getName(),
                RheakvRpc.GetAndPutResponse.getDefaultInstance());

        // 注册 request 和response 的映射关系
        MarshallerHelper.registerRespInstance(RheakvRpc.GetRequest.class.getName(),
                RheakvRpc.GetResponse.getDefaultInstance());
        MarshallerHelper.registerRespInstance(RheakvRpc.GetAndPutRequest.class.getName(),
                RheakvRpc.GetAndPutResponse.getDefaultInstance());

        MarshallerHelper.registerRespInstance(RheakvRpc.BaseRequest.class.getName(),
                RheakvRpc.BaseResponse.getDefaultInstance());


        final Client client = new Client();
        client.init();


        put(client.getRheaKVStore());
        client.shutdown();
    }

    public static void put(final RheaKVStore rheaKVStore) {
        final CompletableFuture<byte[]> f1 = rheaKVStore.getAndPut(writeUtf8("getAndPut"), writeUtf8("getAndPutValue"));
        LOG.info("Old value: {}", readUtf8(FutureHelper.get(f1)));

        final CompletableFuture<byte[]> f2 = rheaKVStore.getAndPut("getAndPut", writeUtf8("getAndPutValue2"));
        LOG.info("Old value: {}", readUtf8(FutureHelper.get(f2)));

        final byte[] b1 = rheaKVStore.bGetAndPut(writeUtf8("getAndPut1"), writeUtf8("getAndPutValue3"));
        LOG.info("Old value: {}", readUtf8(b1));

        final byte[] b2 = rheaKVStore.bGetAndPut(writeUtf8("getAndPut1"), writeUtf8("getAndPutValue4"));
        LOG.info("Old value: {}", readUtf8(b2));
    }
}
