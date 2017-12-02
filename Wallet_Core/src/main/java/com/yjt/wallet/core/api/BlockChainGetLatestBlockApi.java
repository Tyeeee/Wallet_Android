package com.yjt.wallet.core.api;

import com.yjt.wallet.core.Block;
import com.yjt.wallet.core.api.http.BitherUrl;
import com.yjt.wallet.core.api.http.HttpsGetResponse;
import com.yjt.wallet.core.utils.BlockUtil;

import org.json.JSONObject;

public class BlockChainGetLatestBlockApi extends HttpsGetResponse<Block> {
    public BlockChainGetLatestBlockApi() {
        setUrl(BitherUrl.BLOCKCHAIN_INFO_GET_LASTST_BLOCK);
    }

    @Override
    public void setResult(String response) throws Exception {
        JSONObject jsonObject = new JSONObject(response);
        this.result = BlockUtil.getLatestBlockHeight(jsonObject);
    }
}
