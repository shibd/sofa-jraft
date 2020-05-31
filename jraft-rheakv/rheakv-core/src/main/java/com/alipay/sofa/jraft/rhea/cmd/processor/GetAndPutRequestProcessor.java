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
///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.alipay.sofa.jraft.rhea.cmd.processor;
//
//import com.alipay.sofa.jraft.rhea.RegionKVService;
//import com.alipay.sofa.jraft.rhea.RequestProcessClosure;
//import com.alipay.sofa.jraft.rhea.StoreEngine;
//import com.alipay.sofa.jraft.rhea.cmd.proto.RheakvRpc;
//import com.alipay.sofa.jraft.rpc.RpcContext;
//import com.alipay.sofa.jraft.rpc.RpcProcessor;
//import com.alipay.sofa.jraft.util.Requires;
//
//import java.util.concurrent.Executor;
//
///**
// * Rhea KV store RPC request processing service.
// *
// * @author jiachun.fjc
// */
//public class GetAndPutRequestProcessor implements RpcProcessor<RheakvRpc.GetAndPutRequest> {
//
//    private final StoreEngine storeEngine;
//
//    public GetAndPutRequestProcessor(StoreEngine storeEngine) {
//        this.storeEngine = Requires.requireNonNull(storeEngine, "storeEngine");
//    }
//
//    @Override
//    public void handleRequest(RpcContext rpcCtx, RheakvRpc.GetAndPutRequest request) {
//        Requires.requireNonNull(request, "request");
//        final RequestProcessClosure<RheakvRpc.GetAndPutRequest, RheakvRpc.GetAndPutResponse> closure = new RequestProcessClosure<>(
//            request, rpcCtx);
//
//        final RegionKVService regionKVService = this.storeEngine.getRegionKVService(request.getBaseRequest()
//            .getRegionId());
//        if (regionKVService == null) {
//            //            final NoRegionFoundResponse noRegion = new NoRegionFoundResponse();
//            //            noRegion.setRegionId(request.getBaseRequest().getRegionId());
//            //            noRegion.setError(Errors.NO_REGION_FOUND);
//            //            noRegion.setValue(false);
//            //            // todo 怎么处理?
//            //            closure.sendResponse(noRegion);
//
//            RheakvRpc.GetAndPutResponse noRegion = RheakvRpc.GetAndPutResponse
//                .newBuilder()
//                .setBaseResponse(
//                    RheakvRpc.BaseResponse.newBuilder().setRegionId(request.getBaseRequest().getRegionId())
//                    // todo 设置error和value
//                        .setError("todo")
//                        //                    .setValue()
//                        .build()).build();
//            closure.sendResponse(noRegion);
//            return;
//        }
//
//        regionKVService.handleGetAndPutRequest(request, closure);
//
//    }
//
//    @Override
//    public String interest() {
//        return RheakvRpc.GetRequest.class.getName();
//    }
//
//    @Override
//    public Executor executor() {
//        return this.storeEngine.getKvRpcExecutor();
//    }
//}
