package com.yjt.wallet.core.api;

import com.yjt.wallet.core.Block;
import com.yjt.wallet.core.api.http.BitherUrl;
import com.yjt.wallet.core.api.http.HttpsGetResponse;
import com.yjt.wallet.core.utils.BlockUtil;
import com.yjt.wallet.core.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;


public class BlockChainDownloadSpvApi extends HttpsGetResponse<Block> {
    public BlockChainDownloadSpvApi(int height) {
        String url = Utils.format(BitherUrl.BLOCKCHAIN_INFO_GET_SPVBLOCK_API, height);
        setUrl(url);
    }

    @Override
    public void setResult(String response) throws Exception {
        JSONObject jsonObject  = new JSONObject(response);
        JSONArray  jsonArray   = jsonObject.getJSONArray("blocks");
        JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
        this.result = BlockUtil.formatStoreBlockFromBlockChainInfo(jsonObject1);
    }
}
