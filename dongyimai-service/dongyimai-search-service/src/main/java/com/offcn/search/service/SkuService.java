package com.offcn.search.service;

import java.util.Map;

public interface SkuService {

    /**
     * 批量导入sku列表
     */
    void importSku();

    /**
     * 清空es
     */
    void clearAll();

    /**
     * 搜索
     *
     * @param searchMap
     * @return
     */
    Map search(Map<String, String> searchMap);
}
