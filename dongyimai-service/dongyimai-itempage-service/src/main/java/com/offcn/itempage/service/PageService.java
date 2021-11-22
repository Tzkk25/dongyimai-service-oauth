package com.offcn.itempage.service;

public interface PageService {
    /**
     * 根据商品的ID 生成静态页
     *
     * @param spuId
     */
    void createPageHtml(Long spuId);

    /**
     * 删除静态页
     *
     * @param spuId
     */
    void deletePageHtml(Long spuId);
}
