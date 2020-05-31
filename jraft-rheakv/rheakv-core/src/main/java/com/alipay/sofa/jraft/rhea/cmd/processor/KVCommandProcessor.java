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
package com.alipay.sofa.jraft.rhea.cmd.processor;

import com.alipay.sofa.jraft.rhea.RegionKVService;
import com.alipay.sofa.jraft.rhea.RequestProcessClosure;
import com.alipay.sofa.jraft.rhea.StoreEngine;
import com.alipay.sofa.jraft.rhea.cmd.proto.RheakvRpc;
import com.alipay.sofa.jraft.rhea.cmd.store.NoRegionFoundResponse;
import com.alipay.sofa.jraft.rhea.errors.Errors;
import com.alipay.sofa.jraft.rhea.errors.RheaRuntimeException;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.alipay.sofa.jraft.util.Requires;

import java.util.concurrent.Executor;

/**
 * Rhea KV store RPC request processing service.
 *
 * @author baozi
 */
public class KVCommandProcessor implements RpcProcessor<RheakvRpc.BaseRequest> {

    private final Class<?>    reqClazz;
    private final StoreEngine storeEngine;

    public KVCommandProcessor(Class<?> reqClazz, StoreEngine storeEngine) {
        this.reqClazz = reqClazz;
        this.storeEngine = Requires.requireNonNull(storeEngine, "storeEngine");
    }

    @Override
    public void handleRequest(RpcContext rpcCtx, RheakvRpc.BaseRequest request) {

        Requires.requireNonNull(request, "request");
        final RequestProcessClosure<RheakvRpc.BaseRequest, RheakvRpc.BaseResponse> closure = new RequestProcessClosure<>(
            request, rpcCtx);
        final RegionKVService regionKVService = this.storeEngine.getRegionKVService(request.getRegionId());
        if (regionKVService == null) {
            RheakvRpc.BaseResponse response = RheakvRpc.BaseResponse
                .newBuilder()
                .setRegionId(request.getRegionId())
                .setError(Errors.NO_REGION_FOUND.message())
                .setResponseType(RheakvRpc.BaseResponse.ResponseType.noRegionFund)
                // todo 设置啥?
                //                    .setValue()
                .setExtension(RheakvRpc.NoRegionFoundResponse.body,
                    RheakvRpc.NoRegionFoundResponse.newBuilder().build()).build();
            closure.sendResponse(response);
            return;
        }
        RheakvRpc.BaseRequest.RequestType requestType = request.getRequestType();
        switch (requestType) {
            case get:
                RheakvRpc.GetRequest getRequest = request.getExtension(RheakvRpc.GetRequest.body);
                regionKVService.handleGetRequest(request, getRequest, closure);
                break;
            case getAndPut:
                RheakvRpc.GetAndPutRequest getAndPutRequest = request.getExtension(RheakvRpc.GetAndPutRequest.body);
                regionKVService.handleGetAndPutRequest(request, getAndPutRequest, closure);
                break;
            default:
                throw new RheaRuntimeException("Unsupported request type: " + requestType.name());
        }
    }

    @Override
    public String interest() {
        return this.reqClazz.getName();
    }

    @Override
    public Executor executor() {
        return this.storeEngine.getKvRpcExecutor();
    }
}
