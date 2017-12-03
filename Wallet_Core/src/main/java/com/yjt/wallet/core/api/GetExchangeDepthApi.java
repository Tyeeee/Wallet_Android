/*
 * Copyright 2014 http://Bither.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yjt.wallet.core.api;

import com.yjt.wallet.core.api.http.BitherUrl;
import com.yjt.wallet.core.api.http.HttpGetResponse;
import com.yjt.wallet.core.contant.BitherjSettings;
import com.yjt.wallet.core.utils.Utils;

public class GetExchangeDepthApi extends HttpGetResponse<String> {


    public GetExchangeDepthApi(BitherjSettings.MarketType marketType) {

        String url = Utils.format(BitherUrl.BITHER_DEPTH_URL,
                                  BitherjSettings.getMarketValue(marketType));
        setUrl(url);
    }

    @Override
    public void setResult(String response) throws Exception {
        this.result = response;
    }

}
