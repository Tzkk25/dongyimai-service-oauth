package com.offcn.search.dao;


// spring-data-elasticsearch  组件---》  接口 继承 指定的接口 ，默认就有一些功能方法

import com.offcn.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo, Long> {
}
