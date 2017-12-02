package com.yjt.wallet.core.api;

import com.yjt.wallet.core.api.http.BitherUrl;
import com.yjt.wallet.core.api.http.HttpsGetResponse;
import com.yjt.wallet.core.utils.Utils;

/**
 * Created by zhangbo on 16/1/9.
 */
public class BlockChainMytransactionsApi extends HttpsGetResponse<String> {

    @Override
    public void setResult(String response) throws Exception {
        this.result = response;
    }

    public BlockChainMytransactionsApi(String address) {
        String url = Utils.format(BitherUrl.BITHER_BC_GET_BY_ADDRESS, address);
        setUrl(url);
    }

    public BlockChainMytransactionsApi() {
        setUrl(BitherUrl.BITHER_BC_LATEST_BLOCK);
    }

    public BlockChainMytransactionsApi(int txIndex) {
        String url = String.format(BitherUrl.BITHER_BC_TX_INDEX, txIndex);
        setUrl(url);
    }
}
