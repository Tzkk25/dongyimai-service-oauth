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
//                skuEsMapper.saveAll(skuInfos);//es???????????????
//            }
//        }else{
//            throw new RuntimeException("??????????????????");
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
//        //1.?????????????????????
//        String keywords = searchMap.get("keywords");
//        if (StringUtils.isEmpty(keywords)){
//            keywords="??????";//???????????????????????????
//        }
//
//        //2.?????????????????????????????????
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//        //****** ?????????????????????  terms?????????????????????????????????
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("title",keywords));
//
//        //3.?????????????????????
//        //?????????QueryBuilders.matchQuery("title", keywords) ??????????????? ---> ???    ??? ???????????????????????????
//        //?????????QueryBuilders.matchPhraseQuery("title", keywords) ???????????????????????????
//        nativeSearchQueryBuilder.addAggregation(
//                AggregationBuilders.terms("skuCategorygroup").field("category"));
//        //??????????????????????????????????????????
//        nativeSearchQueryBuilder.addAggregation(
//                AggregationBuilders.terms("skuBrandgroup").field("brand"));
//        //????????????????????????????????????
//        nativeSearchQueryBuilder.addAggregation(
//                AggregationBuilders.terms("skuSpecgroup").field("spec.keyword").size(10));
//
//        //4.??????????????????
//        NativeSearchQuery query = nativeSearchQueryBuilder.build();
//        //5.????????????
//        SearchHits<SkuInfo> searchHits = elasticsearchRestTemplate.search(query,SkuInfo.class);
//
//        //?????????searchHits????????????????????????
//        SearchPage<SkuInfo> skuPage = SearchHitSupport.searchPageFor(searchHits,query.getPageable());//0,10
//
//        //?????????????????????????????????
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        //??????????????????????????????
//        if (!StringUtils.isEmpty(searchMap.get("brand"))){
//            boolQueryBuilder.filter(QueryBuilders.matchQuery("brand",searchMap.get("brand")));
//        }
////        Object brand = searchMap.get("brand");
////        if(brand!=null && !brand.equals("")){
////            boolQueryBuilder.filter(QueryBuilders.matchQuery("brand",brand));
////        }
//
//        //??????????????????????????????
//        if (!StringUtils.isEmpty(searchMap.get("category"))){
//            boolQueryBuilder.filter(QueryBuilders.matchQuery("category",searchMap.get("category")));
//        }
////        Object category = searchMap.get("category");
////        if(category!=null && !category.equals("")){
////            boolQueryBuilder.filter(QueryBuilders.matchQuery("category",category));
////        }
//
//        //*****????????????????????????
//        if (searchMap != null){
//            for (String key : searchMap.keySet()){
//                //???????????????????????????
//                //{ brand:"",category:"",spec_??????:"??????4G"}
//                if (key.startsWith("spec_")){
//                    //??????spec_?????????????????????
//                    boolQueryBuilder.filter(QueryBuilders.termQuery(
//                            "specMap."+key.substring(5)+".keyword",
//                            searchMap.get(key)));
//                    System.out.println(searchMap.get(key));
//                }
//            }
//        }
//        //******??????????????????????????????
//        String price = searchMap.get("price");// "10-299"
//        if (price != null && price.equals("")){
//            String[] split = price.split("-");
//            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
//            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(split[1]));
//        }
//        //?????????????????????????????????????????????
//        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);
//
//        //******** ??????????????????
//        Terms terms = searchHits.getAggregations().get("skuCategorygroup");
//
//        //******** ?????????????????? ?????????????????????????????????
//
//        Terms termBrand = searchHits.getAggregations().get("skuBrandgroup");
//        //******** ????????????????????????
//        Terms termSpec = searchHits.getAggregations().get("skuSpecgroup");
//
//        //****************************????????????**************************
//        //????????????
//        String pageNum = searchMap.get("pageNum");//???????????????pageNum???1??????
//        String pageSize = searchMap.get("pageSize");
//        if(!StringUtils.isEmpty(pageNum) && !StringUtils.isEmpty(pageSize)){
//            nativeSearchQueryBuilder.withPageable(PageRequest.of(Integer.parseInt(pageNum+"")-1,Integer.parseInt(pageSize+""))); //??????
//            //nativeSearchQueryBuilder.withPageable(new PageRequest(Integer.parseInt(pageNum),Integer.parseInt(pageSize))); //??????
//        }
//
//        //?????????????????????
////        Object sortRule = searchMap.get("sortRule");//DESC???ASC
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
//                            .fieldSort(String.valueOf(sortField)) // ??????
//                            .order(sortRule.equals("DESC")?SortOrder.DESC:SortOrder.ASC) //????????????
//            );
//        }
//
//        List<String> categoryList = this.getStringsCategoryList(terms);
//        List<String> brandList = this.getStringsBrandList(termBrand);
//        Map<String, Set> specMap = this.getStringSetMap(termSpec);
//
//        //****************??????????????????
//
//        //?????????????????????????????????
//        List<SkuInfo> skuList = new ArrayList<>();
//
//        List<Double> priceList = new ArrayList<>();
//
//        for (SearchHit<SkuInfo> searchHit:skuPage.getContent()){
//            //????????????????????????
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
//        //6.????????????
//        Map resultMap = new HashMap();
//        resultMap.put("rows",skuList);//????????????SkuInfo??????????????????(?????????????????????)
//        resultMap.put("total",searchHits.getTotalHits());//???????????????
//        resultMap.put("totalPages",skuPage.getTotalPages());//?????????
//        //****
//        resultMap.put("categoryList",categoryList);//????????????,?????????????????????????????????????????????
//        resultMap.put("brandList",brandList);
//        resultMap.put("specMap",specMap);
//        resultMap.put("priceRangeList", (priceList.size()>0)? this.priceRangeString(Collections.min(priceList),Collections.max(priceList),5):Arrays.asList("1-100"));
//        return resultMap;
//    }
//
//    /**
//     * ????????????
//     * @param terms
//     * @return
//     */
//    private List<String> getStringsCategoryList(Terms terms){
//        List<String> categoryList = new ArrayList<>();
//        if (terms != null){
//            for (Terms.Bucket bucket: terms.getBuckets()){
//                String keyAsString = bucket.getKeyAsString();//????????????
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
////        //?????????????????????Spec????????????
////        Set<String> specList = new HashSet<>();
////        if (termsSpec != null){
////            for (Terms.Bucket bucket: termsSpec.getBuckets()){
////                specList.add(bucket.getKeyAsString());//spec
////            }
////        }
////        for (String specjson : specList) {
////            Map<String,String> map = JSON.parseObject(specjson,Map.class);
////            for (Map.Entry<String,String> entry : map.entrySet()){
////                //???????????????
////                String key = entry.getKey();
////                //??????????????????
////                String value = entry.getValue();
////
////                //?????????????????????????????????,???????????????????????? ????????????Set?????????????????????
////                Set<String> specValues = specMap.get(key);
////                if (specValues == null){
////                    specValues = new HashSet<String>();
////                }
////                //????????????????????????????????????
////                specValues.add(value);
////                //??????????????????specMap???
////                specMap.put(key,specValues);
////            }
////        }
////        return specMap;
//        Map<String, Set> map = new HashMap<>();
//
//        List<? extends Terms.Bucket> buckets = termsSpec.getBuckets();
//        for (Terms.Bucket bucket : buckets) {
//            String spec = bucket.getKeyAsString();//{"????????????":"16G","??????":"??????3G"}
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
//     * ??????????????????
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
    ElasticsearchRestTemplate elasticsearchRestTemplate;// spring-data-es ????????????????????????????????????

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
                skuEsMapper.saveAll(skuInfos);//es???????????????
            }
        } else {
            throw new RuntimeException("??????????????????");
        }

    }

    @Override
    public void clearAll() {
        skuEsMapper.deleteAll();
    }

    @Override
    public Map search(Map searchMap) {

        // ????????????????????????+??????+??????..
        Map resultMap = new HashMap();

        //????????????????????????????????????????????????
        String keywords = String.valueOf(searchMap.get("keywords"));
        if (StringUtils.isEmpty(keywords)) {
            keywords = "??????";//?????????
        }

        // 1???????????????????????????builder??????
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        //2??? ???title????????????????????????
        builder.withQuery(QueryBuilders.matchQuery("title", keywords));

//        "sortField":"price",
//         "sortRule":"DESC

        Object sortField = searchMap.get("sortField");
        Object sortRule = searchMap.get("sortRule");// DESC

        if (!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRule)) {
            builder.withSort(
                    SortBuilders
                            .fieldSort(String.valueOf(sortField)) // ??????
                            .order(sortRule.equals("DESC") ? SortOrder.DESC : SortOrder.ASC) //????????????
            );
        }

        Object pageNum = searchMap.get("pageNum");//???????????????pageNum???1??????
        Object pageSize = searchMap.get("pageSize");
        if (!StringUtils.isEmpty(pageNum) && !StringUtils.isEmpty(pageSize)) {
            builder.withPageable(PageRequest.of(Integer.parseInt(pageNum + "") - 1, Integer.parseInt(pageSize + ""))); //??????
        }


//        builder.withHighlightBuilder() ??????
        builder.withHighlightFields(new HighlightBuilder.Field("title"));//title??????????????????
        builder.withHighlightBuilder(new HighlightBuilder().preTags("<font color='red'>").postTags("</font>"));//pre??????  post??????


        // ??????????????????????????????????????????????????????..

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
//                  "keywords":"????????????",
//                  "spec_??????":"??????4G",
//                  "spec_????????????":"24G"
//        }
        for (Object key : searchMap.keySet()) {
            if (String.valueOf(key).startsWith("spec_")) {
                String specName = String.valueOf(key).substring(5);//??????
                Object optionValue = searchMap.get(key);
                // specMap.??????=??????3G
                boolQueryBuilder.filter(QueryBuilders.matchQuery("specMap." + specName + ".keyword", optionValue));
            }
        }


        //????????????

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

        //3?????????category???????????????????????????????????????skuCategorygroup???????????????
        //???????????????????????? SELECT category FROM `tb_item` GROUP BY category
        // skuCategorygroup  ?????????????????????
        builder.addAggregation(
                AggregationBuilders.terms("skuCategorygroup").field("category") // @Field(type = FieldType.Keyword)
        );

        builder.addAggregation(
                AggregationBuilders.terms("skuBrandgroup").field("brand")// @Field(type = FieldType.Keyword)
        );

        //
        builder.addAggregation(
                AggregationBuilders.terms("skuSpecgroup").field("spec.keyword").size(100) // ??????spec???????????????????????????
        );

        //4?????????Query??????
        NativeSearchQuery searchQuery = builder.build();


        // 5?????????template??????????????????
        SearchHits<SkuInfo> searchHits = elasticsearchRestTemplate.search(searchQuery, SkuInfo.class);


        //6?????????????????????searchHits?????????????????????
        SearchPage<SkuInfo> searchPage = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());//0, 10


        //7?????????searchHits?????????????????????
        Terms terms = searchHits.getAggregations().get("skuCategorygroup");
        List<String> categoryList = getStringsCategoryList(terms);

        Terms termsBrand = searchHits.getAggregations().get("skuBrandgroup");
        List<String> brandList = getStringsBrandList(termsBrand);

        Terms termsSpec = searchHits.getAggregations().get("skuSpecgroup");
        Map<String, Set> specMap = getSpecMap(termsSpec);// [ string:set,string:set ]

        // 8?????????????????????
        List<SearchHit<SkuInfo>> list = searchPage.getContent();
        List<SkuInfo> skuInfos = new ArrayList<>();

        List<Double> priceList = new ArrayList<>();

//        Set<String> categoryList = new HashSet<>();//??????????????????
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


        // 9??????????????????
        resultMap.put("rows", skuInfos);
        resultMap.put("total", searchHits.getTotalHits());//????????????
        resultMap.put("totalPages", searchPage.getTotalPages());//?????????

//        resultMap.put("categoryList",categoryList);
        resultMap.put("categoryList", categoryList);//???????????????????????????????????????????????????????????????????????????
        resultMap.put("brandList", brandList);

        resultMap.put("specMap", specMap);//??????keywords?????????????????????????????????????????????????????????


        resultMap.put("priceRangeList", (priceList.size() > 0) ? this.priceRangeString(Collections.min(priceList), Collections.max(priceList), 5) : Arrays.asList("1-100"));

        return resultMap;
    }

    private Map<String, Set> getSpecMap(Terms termsSpec) {

        Map<String, Set> map = new HashMap<>();

        List<? extends Terms.Bucket> buckets = termsSpec.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            String spec = bucket.getKeyAsString();//{"????????????":"16G","??????":"??????3G"}
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

        List<String> specList = Arrays.asList("{\"????????????\":\"16G\",\"??????\":\"2000\"}", "{\"????????????\":\"16G\",\"??????\":\"??????3G\"}", "{\"????????????\":\"16G\",\"??????\":\"??????4G\"}", "{\"????????????\":\"24G\",\"??????\":\"??????3G\"}", "{\"????????????\":\"24G\",\"??????\":\"??????4G\"}");

        // ????????????-16G 24G
        // ??????-??????3G ??????4G

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
            String brandName = bucket.getKeyAsString();//????????????????????????
            brandList.add(brandName);
        }
        return brandList;
    }


    private List<String> getStringsCategoryList(Terms terms) {
        List<String> categoryList = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            String categoryName = bucket.getKeyAsString();//????????????????????????
            categoryList.add(categoryName);
        }
        return categoryList;
    }

    //a-b

    /**
     * ??????????????????
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

