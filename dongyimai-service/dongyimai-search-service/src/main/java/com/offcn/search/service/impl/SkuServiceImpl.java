//package com.offcn.search.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.offcn.entity.Result;
//import com.offcn.search.dao.SkuEsMapper;
//import com.offcn.search.pojo.SkuInfo;
//import com.offcn.search.service.SkuService;
//import com.offcn.sellergoods.feign.ItemFeign;
//import com.offcn.sellergoods.pojo.Item;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.aggregations.Aggregation;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.bucket.terms.Terms;
//import org.elasticsearch.search.sort.SortBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.elasticsearch.core.*;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.util.*;
//
//@Service
//public class SkuServiceImpl implements SkuService {
//
//    @Autowired
//    SkuEsMapper skuEsMapper;
//
//    @Autowired
//    ItemFeign itemFeign;
//
//    @Autowired
//    private ElasticsearchRestTemplate elasticsearchRestTemplate;
//
//    @Override
//    public void importSku() {
//        Result<List<Item>> result = itemFeign.findByStatus("1");
//
//        if(result!=null && result.getCode().intValue()==20000){
//            List<Item> list = result.getData();
//            if(list!=null && list.size()>0){
//                // List<Item> --> List<SkuInfo>---- skuEsMapper -->es
//                List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(list), SkuInfo.class);
//                for (SkuInfo skuInfo : skuInfos) {
//                    String spec = skuInfo.getSpec();
//                    Map map = JSON.parseObject(spec, Map.class);
//                    skuInfo.setSpecMap(map);
//                }
//                skuEsMapper.saveAll(skuInfos);//es中导入数据
//            }
//        }else{
//            throw new RuntimeException("导入失败！！");
//        }
//
//    }
//
//    @Override
//    public void clearAll() {
//        skuEsMapper.deleteAll();
//    }
//
//    @Override
//    public Map search(Map<String, String> searchMap) {
//        //1.获取关键字的值
//        String keywords = searchMap.get("keywords");
//        if (StringUtils.isEmpty(keywords)){
//            keywords="小米";//给一个默认的初始值
//        }
//
//        //2.创建查询对象的构建对象
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//        //****** 设置分组的条件  terms后表示分组查询后的列名
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("title",keywords));
//
//        //3.设置查询的条件
//        //使用：QueryBuilders.matchQuery("title", keywords) ，搜索华为 ---> 华    为 二字可以拆分查询，
//        //使用：QueryBuilders.matchPhraseQuery("title", keywords) 华为二字不拆分查询
//        nativeSearchQueryBuilder.addAggregation(
//                AggregationBuilders.terms("skuCategorygroup").field("category"));
//        //根据商品品牌添加一个分组条件
//        nativeSearchQueryBuilder.addAggregation(
//                AggregationBuilders.terms("skuBrandgroup").field("brand"));
//        //根据规格添加一个分组条件
//        nativeSearchQueryBuilder.addAggregation(
//                AggregationBuilders.terms("skuSpecgroup").field("spec.keyword").size(10));
//
//        //4.构建查询对象
//        NativeSearchQuery query = nativeSearchQueryBuilder.build();
//        //5.执行查询
//        SearchHits<SkuInfo> searchHits = elasticsearchRestTemplate.search(query,SkuInfo.class);
//
//        //对搜索searchHits集合进行封装分页
//        SearchPage<SkuInfo> skuPage = SearchHitSupport.searchPageFor(searchHits,query.getPageable());//0,10
//
//        //创建多条件组合查询对象
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        //设置品牌分类查询条件
//        if (!StringUtils.isEmpty(searchMap.get("brand"))){
//            boolQueryBuilder.filter(QueryBuilders.matchQuery("brand",searchMap.get("brand")));
//        }
////        Object brand = searchMap.get("brand");
////        if(brand!=null && !brand.equals("")){
////            boolQueryBuilder.filter(QueryBuilders.matchQuery("brand",brand));
////        }
//
//        //设置分类查询查询条件
//        if (!StringUtils.isEmpty(searchMap.get("category"))){
//            boolQueryBuilder.filter(QueryBuilders.matchQuery("category",searchMap.get("category")));
//        }
////        Object category = searchMap.get("category");
////        if(category!=null && !category.equals("")){
////            boolQueryBuilder.filter(QueryBuilders.matchQuery("category",category));
////        }
//
//        //*****设置规格过滤查询
//        if (searchMap != null){
//            for (String key : searchMap.keySet()){
//                //传过来数据模板如下
//                //{ brand:"",category:"",spec_网络:"电信4G"}
//                if (key.startsWith("spec_")){
//                    //满足spec_开头则开始截取
//                    boolQueryBuilder.filter(QueryBuilders.termQuery(
//                            "specMap."+key.substring(5)+".keyword",
//                            searchMap.get(key)));
//                    System.out.println(searchMap.get(key));
//                }
//            }
//        }
//        //******设置价格区间过滤查询
//        String price = searchMap.get("price");// "10-299"
//        if (price != null && price.equals("")){
//            String[] split = price.split("-");
//            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
//            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(split[1]));
//        }
//        //关联过滤查询对象到查询的结果集
//        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);
//
//        //******** 获取分组结果
//        Terms terms = searchHits.getAggregations().get("skuCategorygroup");
//
//        //******** 获取分组结果 根据商品的品牌进行分组
//
//        Terms termBrand = searchHits.getAggregations().get("skuBrandgroup");
//        //******** 获取规格分组结果
//        Terms termSpec = searchHits.getAggregations().get("skuSpecgroup");
//
//        //****************************过滤查询**************************
//        //分页实现
//        String pageNum = searchMap.get("pageNum");//前端传递的pageNum从1开始
//        String pageSize = searchMap.get("pageSize");
//        if(!StringUtils.isEmpty(pageNum) && !StringUtils.isEmpty(pageSize)){
//            nativeSearchQueryBuilder.withPageable(PageRequest.of(Integer.parseInt(pageNum+"")-1,Integer.parseInt(pageSize+""))); //分页
//            //nativeSearchQueryBuilder.withPageable(new PageRequest(Integer.parseInt(pageNum),Integer.parseInt(pageSize))); //分页
//        }
//
//        //排序功能的实现
////        Object sortRule = searchMap.get("sortRule");//DESC、ASC
////        Object sortField = searchMap.get("sortField");
////        if (!StringUtils.isEmpty(sortRule)&&!StringUtils.isEmpty(sortField)){
////            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(String.valueOf(sortField)).order(sortRule.equals("DESC")?SortOrder.DESC:SortOrder.ASC));
////        }
//        Object sortField = searchMap.get("sortField");
//        Object sortRule = searchMap.get("sortRule");// DESC
//
//        if(!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRule)){
//            nativeSearchQueryBuilder.withSort(
//                    SortBuilders
//                            .fieldSort(String.valueOf(sortField)) // 字段
//                            .order(sortRule.equals("DESC")?SortOrder.DESC:SortOrder.ASC) //排序方向
//            );
//        }
//
//        List<String> categoryList = this.getStringsCategoryList(terms);
//        List<String> brandList = this.getStringsBrandList(termBrand);
//        Map<String, Set> specMap = this.getStringSetMap(termSpec);
//
//        //****************过滤查询结束
//
//        //遍历取出查询的商品信息
//        List<SkuInfo> skuList = new ArrayList<>();
//
//        List<Double> priceList = new ArrayList<>();
//
//        for (SearchHit<SkuInfo> searchHit:skuPage.getContent()){
//            //获取搜索到的数据
//            SkuInfo skuInfo = searchHit.getContent();
//            skuList.add(skuInfo);
//
//            double v = skuInfo.getPrice().doubleValue();
//            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
//            List<String> titles = highlightFields.get("title");
//            if(titles!=null && titles.size()>0){
//                skuInfo.setTitle(titles.get(0));
//            }
////           categoryList.add(skuInfo.getCategory());
//            priceList.add(v);
//        }
//
//        //6.返回结果
//        Map resultMap = new HashMap();
//        resultMap.put("rows",skuList);//获取所需SkuInfo集合数据内容(当前页数据集合)
//        resultMap.put("total",searchHits.getTotalHits());//总的记录数
//        resultMap.put("totalPages",skuPage.getTotalPages());//总页数
//        //****
//        resultMap.put("categoryList",categoryList);//分组统计,按照指定的关键词检查来自那个类
//        resultMap.put("brandList",brandList);
//        resultMap.put("specMap",specMap);
//        resultMap.put("priceRangeList", (priceList.size()>0)? this.priceRangeString(Collections.min(priceList),Collections.max(priceList),5):Arrays.asList("1-100"));
//        return resultMap;
//    }
//
//    /**
//     * 简化代码
//     * @param terms
//     * @return
//     */
//    private List<String> getStringsCategoryList(Terms terms){
//        List<String> categoryList = new ArrayList<>();
//        if (terms != null){
//            for (Terms.Bucket bucket: terms.getBuckets()){
//                String keyAsString = bucket.getKeyAsString();//分组的值
//                categoryList.add(keyAsString);
//            }
//        }
//        return categoryList;
//    }
//
//    private List<String> getStringsBrandList(Terms termsBrand){
//        List<String> brandList = new ArrayList<>();
//        if (termsBrand != null){
//            for (Terms.Bucket bucket : termsBrand.getBuckets()){
//                String keyAsString = bucket.getKeyAsString();
//                brandList.add(keyAsString);
//            }
//        }
//        return brandList;
//    }
//
//    private Map<String, Set>getStringSetMap(Terms termsSpec){
////        Map<String,Set<String>>specMap = new HashMap<>();
////        //记录所有不同的Spec规格字段
////        Set<String> specList = new HashSet<>();
////        if (termsSpec != null){
////            for (Terms.Bucket bucket: termsSpec.getBuckets()){
////                specList.add(bucket.getKeyAsString());//spec
////            }
////        }
////        for (String specjson : specList) {
////            Map<String,String> map = JSON.parseObject(specjson,Map.class);
////            for (Map.Entry<String,String> entry : map.entrySet()){
////                //规格的名称
////                String key = entry.getKey();
////                //规格的选项值
////                String value = entry.getValue();
////
////                //将规格和选项值封装起来,但是结果不能重复 所有使用Set将他们封装起来
////                Set<String> specValues = specMap.get(key);
////                if (specValues == null){
////                    specValues = new HashSet<String>();
////                }
////                //将当前的规格加入到集合中
////                specValues.add(value);
////                //将数据存入到specMap中
////                specMap.put(key,specValues);
////            }
////        }
////        return specMap;
//        Map<String, Set> map = new HashMap<>();
//
//        List<? extends Terms.Bucket> buckets = termsSpec.getBuckets();
//        for (Terms.Bucket bucket : buckets) {
//            String spec = bucket.getKeyAsString();//{"机身内存":"16G","网络":"移动3G"}
//            Map specMap = JSON.parseObject(spec, Map.class);
//            for (Object key : specMap.keySet()) {
//                Object optionName = specMap.get(String.valueOf(key));
//                Set optionSet = map.get(String.valueOf(key));
//                if(optionSet==null){
//                    optionSet = new HashSet();
//                }
//                optionSet.add(optionName);
//                map.put(String.valueOf(key),optionSet);
//            }
//        }
//        return map;
//    }
//
//    /**
//     * 计算价格区间
//     * @param min
//     * @param max
//     * @param count
//     * @return
//     */
//    private List<String> priceRangeString(double min,double max , int count){
//        List<String> list = new ArrayList<>();
//        int range = (int) ( max - min ) / count;
//        for (int i = 0; i < count; i++) {
//            int begin = (int)(min + i * range) ;
//            int end =  (i!=count-1)?(int)(begin + range):(int)max;
//            list.add(begin+"-"+end);
//        }
//        return list;
//    }
//}
//
package com.offcn.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.entity.Result;
import com.offcn.search.dao.SkuEsMapper;
import com.offcn.search.pojo.SkuInfo;
import com.offcn.search.service.SkuService;
import com.offcn.sellergoods.feign.ItemFeign;
import com.offcn.sellergoods.pojo.Item;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    SkuEsMapper skuEsMapper;

    @Autowired
    ItemFeign itemFeign;

    //    StringRedisTemplate stringRedisTemplate; spring-data-redis
    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;// spring-data-es 提供的一个模板工具类对象

    @Override
    public void importSku() {
        Result<List<Item>> result = itemFeign.findByStatus("1");

        if (result != null && result.getCode().intValue() == 20000) {
            List<Item> list = result.getData();
            if (list != null && list.size() > 0) {
                // List<Item> --> List<SkuInfo>---- skuEsMapper -->es
                List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(list), SkuInfo.class);
                for (SkuInfo skuInfo : skuInfos) {
                    String spec = skuInfo.getSpec();
                    Map map = JSON.parseObject(spec, Map.class);
                    skuInfo.setSpecMap(map);
                }
                skuEsMapper.saveAll(skuInfos);//es中导入数据
            }
        } else {
            throw new RuntimeException("导入失败！！");
        }

    }

    @Override
    public void clearAll() {
        skuEsMapper.deleteAll();
    }

    @Override
    public Map search(Map searchMap) {

        // 返回值，商品集合+品牌+分类..
        Map resultMap = new HashMap();

        //关键词（搜索页面上输入的关键词）
        String keywords = String.valueOf(searchMap.get("keywords"));
        if (StringUtils.isEmpty(keywords)) {
            keywords = "华为";//默认值
        }

        // 1、开始构建查询基础builder对象
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        //2、 在title字段进行全文检索
        builder.withQuery(QueryBuilders.matchQuery("title", keywords));

//        "sortField":"price",
//         "sortRule":"DESC

        Object sortField = searchMap.get("sortField");
        Object sortRule = searchMap.get("sortRule");// DESC

        if (!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRule)) {
            builder.withSort(
                    SortBuilders
                            .fieldSort(String.valueOf(sortField)) // 字段
                            .order(sortRule.equals("DESC") ? SortOrder.DESC : SortOrder.ASC) //排序方向
            );
        }

        Object pageNum = searchMap.get("pageNum");//前端传递的pageNum从1开始
        Object pageSize = searchMap.get("pageSize");
        if (!StringUtils.isEmpty(pageNum) && !StringUtils.isEmpty(pageSize)) {
            builder.withPageable(PageRequest.of(Integer.parseInt(pageNum + "") - 1, Integer.parseInt(pageSize + ""))); //分页
        }


//        builder.withHighlightBuilder() 高亮
        builder.withHighlightFields(new HighlightBuilder.Field("title"));//title字段进行高亮
        builder.withHighlightBuilder(new HighlightBuilder().preTags("<font color='red'>").postTags("</font>"));//pre前缀  post后缀


        // 设置过滤查询，品牌，分类，规格，价格..

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        String brand = String.valueOf(searchMap.get("brand"));// "null"   null
        Object brand = searchMap.get("brand");
        if (brand != null && !brand.equals("")) {
            boolQueryBuilder.filter(QueryBuilders.matchQuery("brand", brand));
        }

        Object category = searchMap.get("category");
        if (category != null && !category.equals("")) {
            boolQueryBuilder.filter(QueryBuilders.matchQuery("category", category));
        }
//        {
//                  "keywords":"小米联想",
//                  "spec_网络":"移动4G",
//                  "spec_机身内存":"24G"
//        }
        for (Object key : searchMap.keySet()) {
            if (String.valueOf(key).startsWith("spec_")) {
                String specName = String.valueOf(key).substring(5);//网络
                Object optionValue = searchMap.get(key);
                // specMap.网络=移动3G
                boolQueryBuilder.filter(QueryBuilders.matchQuery("specMap." + specName + ".keyword", optionValue));
            }
        }


        //价格过滤

//        "price":"1200-1400"

        Object price = searchMap.get("price");
        if (price != null && !price.equals("")) {
            String[] split = String.valueOf(price).split("-");
            System.out.println(split[0]);
            System.out.println(split[1]);
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(split[1]));
        }

        builder.withFilter(boolQueryBuilder);

        //3、使用category字段分组查询，分组结果名称skuCategorygroup（自定义）
        //构建一个分组查询 SELECT category FROM `tb_item` GROUP BY category
        // skuCategorygroup  分组结果的名称
        builder.addAggregation(
                AggregationBuilders.terms("skuCategorygroup").field("category") // @Field(type = FieldType.Keyword)
        );

        builder.addAggregation(
                AggregationBuilders.terms("skuBrandgroup").field("brand")// @Field(type = FieldType.Keyword)
        );

        //
        builder.addAggregation(
                AggregationBuilders.terms("skuSpecgroup").field("spec.keyword").size(100) // 如果spec字段没有添加注解，
        );

        //4、生成Query对象
        NativeSearchQuery searchQuery = builder.build();


        // 5、使用template对象进行查询
        SearchHits<SkuInfo> searchHits = elasticsearchRestTemplate.search(searchQuery, SkuInfo.class);


        //6、解析返回结果searchHits，指定分页参数
        SearchPage<SkuInfo> searchPage = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());//0, 10


        //7、解析searchHits，获取分组结果
        Terms terms = searchHits.getAggregations().get("skuCategorygroup");
        List<String> categoryList = getStringsCategoryList(terms);

        Terms termsBrand = searchHits.getAggregations().get("skuBrandgroup");
        List<String> brandList = getStringsBrandList(termsBrand);

        Terms termsSpec = searchHits.getAggregations().get("skuSpecgroup");
        Map<String, Set> specMap = getSpecMap(termsSpec);// [ string:set,string:set ]

        // 8、分页数据集合
        List<SearchHit<SkuInfo>> list = searchPage.getContent();
        List<SkuInfo> skuInfos = new ArrayList<>();

        List<Double> priceList = new ArrayList<>();

//        Set<String> categoryList = new HashSet<>();//统计分类集合
        for (SearchHit<SkuInfo> hit : list) {
            SkuInfo skuInfo = hit.getContent();
            skuInfos.add(skuInfo);

            double v = skuInfo.getPrice().doubleValue();
            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            List<String> titles = highlightFields.get("title");
            if (titles != null && titles.size() > 0) {
                skuInfo.setTitle(titles.get(0));
            }
//            categoryList.add(skuInfo.getCategory());
            priceList.add(v);
        }


        // 9、封装返回值
        resultMap.put("rows", skuInfos);
        resultMap.put("total", searchHits.getTotalHits());//总记录数
        resultMap.put("totalPages", searchPage.getTotalPages());//总页数

//        resultMap.put("categoryList",categoryList);
        resultMap.put("categoryList", categoryList);//分组统计按照指定的关键词检索的商品都来自于哪些分类
        resultMap.put("brandList", brandList);

        resultMap.put("specMap", specMap);//按照keywords检索到的商品都使用了哪些规格和规格选项


        resultMap.put("priceRangeList", (priceList.size() > 0) ? this.priceRangeString(Collections.min(priceList), Collections.max(priceList), 5) : Arrays.asList("1-100"));

        return resultMap;
    }

    private Map<String, Set> getSpecMap(Terms termsSpec) {

        Map<String, Set> map = new HashMap<>();

        List<? extends Terms.Bucket> buckets = termsSpec.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            String spec = bucket.getKeyAsString();//{"机身内存":"16G","网络":"移动3G"}
            Map specMap = JSON.parseObject(spec, Map.class);
            for (Object key : specMap.keySet()) {
                Object optionName = specMap.get(String.valueOf(key));
                Set optionSet = map.get(String.valueOf(key));
                if (optionSet == null) {
                    optionSet = new HashSet();
                }
                optionSet.add(optionName);
                map.put(String.valueOf(key), optionSet);
            }
        }

        return map;
    }

    public static void main1(String[] args) {
        Map<String, Set> map = new HashMap<>();

        List<String> specList = Arrays.asList("{\"机身内存\":\"16G\",\"像素\":\"2000\"}", "{\"机身内存\":\"16G\",\"网络\":\"移动3G\"}", "{\"机身内存\":\"16G\",\"网络\":\"移动4G\"}", "{\"机身内存\":\"24G\",\"网络\":\"移动3G\"}", "{\"机身内存\":\"24G\",\"网络\":\"移动4G\"}");

        // 机身内存-16G 24G
        // 网络-移动3G 移动4G

        for (String spec : specList) {
            Map specMap = JSON.parseObject(spec, Map.class);
            for (Object key : specMap.keySet()) {
                Object optionName = specMap.get(String.valueOf(key));
                Set optionSet = map.get(String.valueOf(key));
                if (optionSet == null) {
                    optionSet = new HashSet();
                }
                optionSet.add(optionName);
                map.put(String.valueOf(key), optionSet);
            }
        }
        System.out.println(map);
    }

    private List<String> getStringsBrandList(Terms termsBrand) {
        List<String> brandList = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = termsBrand.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            String brandName = bucket.getKeyAsString();//具体一个品牌名称
            brandList.add(brandName);
        }
        return brandList;
    }


    private List<String> getStringsCategoryList(Terms terms) {
        List<String> categoryList = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            String categoryName = bucket.getKeyAsString();//具体一个分类名称
            categoryList.add(categoryName);
        }
        return categoryList;
    }

    //a-b

    /**
     * 计算价格区间
     *
     * @param min
     * @param max
     * @param count
     * @return
     */
    private List<String> priceRangeString(double min, double max, int count) {

        int range = (int) (max - min) / count;

        List<String> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int begin = (int) (min + i * range);
            int end = (i != count - 1) ? (int) (begin + range) : (int) max;
            list.add(begin + "-" + end);
        }
        return list;
    }

//    public static void main(String[] args) {
////        List<String> strings = priceRangeString(135, 1889, 5);
////        System.out.println(strings);
//
//        List<Double> doubles = Arrays.asList(Double.valueOf(10),Double.valueOf(20),Double.valueOf(9),Double.valueOf(1));
//        Double max = Collections.max(doubles);
//        System.out.println(max);
//        System.out.println(Collections.min(doubles));
//    }

}

