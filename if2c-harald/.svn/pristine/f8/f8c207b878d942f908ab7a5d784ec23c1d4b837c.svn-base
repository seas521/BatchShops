package com.if2c.harald.migration;

import com.alibaba.fastjson.JSON;
import com.if2c.harald.beans.*;
import com.if2c.harald.beans.ExtAttrNameValue;
import com.if2c.harald.beans.query.*;
import com.if2c.harald.dao.DataSourceContextHolder;
import com.if2c.harald.service.AttributeCategoryValueRelationService;
import com.if2c.harald.service.ExtAttrNameValueService;
import com.if2c.harald.service.GoodsSeriesService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * Description:商品类目迁移
 * dao层方法规则：线上查询用qry*，目标库是get*
 * User: Courser
 * Date: 14-4-28
 * Time: 下午4:35
 */
@Component
public class GoodsSeriesMigration {
    private static final Logger logger = Logger.getLogger(GoodsSeriesMigration.class);
    /**
     *  进出口 {1：进口，2：出口}
     */
    private static final Integer trade=2 ;
    /**
     * 业务方提供文件
     */
    private static  final String readFileName ="d:\\123.txt";
    /**
     * 归档文件
     */
    private static  final String archiveFileName="d:\\qianyi\\archive.data";

    /**
     * 旧系列_旧类目，新类目
     */
    private static Map<String,String> seriesCategoryIdsMap = new HashMap<String, String>();
    /**
     * 存储业务方提供的原始数据，SPUID,oldCategoryiD,newCateGoryId
     */
    private static List<S2cr> s2crList = new ArrayList<S2cr>();

    /**
     * 目标库属性类目值关系{"新的类目ID_商品属性名字_商品属性值","新的属性类目值关系ID"}
     */
    private static Map<String, AttributeCategoryValueRelation> targetacvrMap = new HashMap<String, AttributeCategoryValueRelation>();

    /**
     * 目标库属性类目值关系{"新的类目ID_扩展属性名字_扩展属性值",扩展属性对象}
     */
    private static Map<String, ExtAttrNameValue> targeExtAttrNameValueMap = new HashMap<String, ExtAttrNameValue>();
    @Resource
    private GoodsSeriesService goodsSeriesService;

    @Resource
    private AttributeCategoryValueRelationService attributeCategoryValueRelationService;
    @Resource
    private ExtAttrNameValueService extAttrNameValueService;

    @PostConstruct
    private void init() {
        DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TARGET);
        logger.warn("=============读取业务方数据开始==========");
        initCategoryMap();
        logger.warn("=============读取业务方数据结束==========");

//        logger.warn("=============新旧类目翻转开始==========");
//        initCategoryInversionMap();
//        logger.warn("=============新旧类目翻转结束==========");
//
        logger.warn("=============获取目标所有商品属性开始==========");
        initAcvrList();
        logger.warn("=============获取目标所有商品属性结束==========");

        logger.warn("=============获取目标所有扩展属性开始==========");
        initExtAttrValueMap();
        logger.warn("=============获取目标所有扩展属性结束==========");
        DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_ONLINE);
    }


    /**
     * 初始化新旧类目 左旧右新
     */
    private void initCategoryMap() {
//        categoryMap.put("205002009", "243006001");
//        categoryMap.put("205003003", "216005002005");
//        categoryMap.put("205003016", "216002018");
//        categoryMap.put("205001005", "216007010004");
//        categoryMap.put("206008001", "223009");
        readCategoryFile();
        validateCategory(seriesCategoryIdsMap);
        //categoryMap.put("105003005","212001004");

    }
    private void  validateCategory(Map<String,String> map){
        for(S2cr s2cr:s2crList){
//            throw  new RuntimeException("aaaaaa");
            if(map.containsKey(s2cr.getSeriesId().toString().trim()+"_"+s2cr.getOldCategoryId().toString().trim()) && map.containsValue(s2cr.getNewCategoryId().trim().toLowerCase())){
                continue;
            }else {
                logger.error("****业务方错误的数据【"+JSON.toJSONString(s2cr)+"】");
               throw  new RuntimeException("业务方给的数据错误");
            }
        }

    }
    private void readCategoryFile(){
        /**
         * 以行为单位读取文件，常用于读面向行的格式化文件
         */
            File file = new File(readFileName);
            BufferedReader reader = null;
            try {
//                System.out.println("以行为单位读取文件内容，一次读一整行：");
                reader = new BufferedReader(new FileReader(file));
                String tempString = null;
                int line = 1;
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    // 显示行号
//                    System.out.println("line " + line + ": " + tempString);
                    String[] arrays = tempString.split(",");
                    if(3>arrays.length){
                        continue;
                    }
//                    if(null ==arrays[2] || "".equals((arrays[2].toString().trim())))
//                            continue ;
                    if("-1".equals(arrays[2])){
                        logger.error("业务方没有给出映射关系：【"+tempString+"】");
                    }else{
//                        categoryMap.put(arrays[1].toString(),arrays[2].toString());//key:旧类目，value:新类目
//                        seriesCategoryIdsList.add(arrays[0].toString()+"_"+arrays[1].toString());//系列_类目
                        seriesCategoryIdsMap.put(arrays[0].toString().trim()+"_"+arrays[1].toString().trim(),arrays[2].toString().trim());//系列_类目,新类目
                        s2crList.add(new S2cr(arrays[0].toString().trim(),arrays[1].toString().trim(),arrays[2].toString().trim()));
                    }
//System.out.println(line);
                    line++;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }

                logger.warn("业务方提供的数据大小：【"+s2crList.size() +"】【"+JSON.toJSONString(s2crList)+"】");
                logger.warn("业务方提供的数据 旧系列_旧类目，新类目 大小：【"+seriesCategoryIdsMap.size()+"】【"+JSON.toJSONString(seriesCategoryIdsMap)+"】");

            }



    }
    /**
     * 反转 categoryMap 正的key 和value
     */
    private void initCategoryInversionMap() {
//        Iterator<Map.Entry<String, String>> i = categoryMap.entrySet().iterator();
//
//        while (i.hasNext()) {//只遍历一次,速度快
//            Map.Entry<String, String> e = (Map.Entry) i.next();
//            categoryInversionMap.put(e.getValue(), e.getKey());//旧新翻转
//        }
//       logger.warn("旧新类目翻转前大小【"+categoryMap.size()+"】，翻转后大小【"+categoryInversionMap.size()+"】。翻转后新旧类目映射关系【"+ JSON.toJSONString(categoryInversionMap)+"】");

    }

    /**
     * 初始化新的属性类目值关系
     * key：旧的类目ID_商品属性名字_商品属性值
     * value:新的ID
     */
    private void initAcvrList() {
        AttributeCategoryValueRelationQuery query = new AttributeCategoryValueRelationQuery();
        List<AttributeCategoryValueRelation> attrNameValueList = attributeCategoryValueRelationService.getAll(query);
        //格式化属性值
        formatAttrs(attrNameValueList);
        logger.warn("--------目标所有商品属性大小：【"+attrNameValueList.size()+"】------------");
//        logger.warn("--------目标所有商品属性【"+JSON.toJSONString(attrNameValueList)+"】");
//可能有问题
        for (AttributeCategoryValueRelation acvr : attrNameValueList) {
            if (seriesCategoryIdsMap.containsValue(acvr.getCategoryId().toString()))
//                setTargetacvrMap(acvr)
                targetacvrMap.put(acvr.getCategoryId().toString() + "_" + acvr.getAttributeNameName() + "_" + acvr.getAttributeValueValue(), acvr);
            else
                logger.info("*****目标库此商品属性被过滤掉【" + JSON.toJSONString(acvr) + "】");
        }
        logger.warn("-------目标库商品属性与有效的旧系列类目映射表比较后，有效的目标库属性（新类目_属性_名字）集合大小【"+targetacvrMap.size()+"】");
//        logger.warn("----------目标库新类目属性关系数据【"+JSON.toJSONString(targetacvrMap)+"】");
    }
    private void setTargetacvrMap(AttributeCategoryValueRelation acvr){
//        Set<Map.Entry<String,String>> set = categoryMap.entrySet();
    }

    /**
     * 初始化新的扩展属性与类目的关系
     */
    private void initExtAttrValueMap() {
        List<ExtAttrNameValue> extAttrNameValueList = extAttrNameValueService.getAll(new ExtAttrNameValueQuery());
        //格式化扩展属性
        formatExtAttrs(extAttrNameValueList);
        logger.warn("------目标库所有扩展属性集合大小【"+extAttrNameValueList.size()+"】");
        for (ExtAttrNameValue extAttrNameValue : extAttrNameValueList) {

            if (seriesCategoryIdsMap.containsValue(extAttrNameValue.getCategoryId().toString()))//类目ID相同，则有对应关系
                targeExtAttrNameValueMap.put(extAttrNameValue.getCategoryId().toString()+ "_" + extAttrNameValue.getExtendsAttrName() + "_" + extAttrNameValue.getExtendsAttrValue(), extAttrNameValue);
            else
                logger.info("****目标库此扩展属性被过滤掉【"+JSON.toJSONString(extAttrNameValue)+"】");
        }
        logger.warn("------目标库扩展属性与有效的{旧系列_旧类目， 新类目}关系对比后 大小【"+targeExtAttrNameValueMap.size()+"】");

//        logger.warn("--------目标库与有效的{旧系列_旧类目， 新类目}关系对比后 有效数据（新类目_扩展属性_值）【" + JSON.toJSON(targeExstAttrNameValueMap) + "】");
//        logger.warn("--------目标库与有效的{旧系列_旧类目， 新类目}关系对比后 有效数据（新类目_扩展属性_值）【" + JSON.toJSON(targeExstAttrNameValueMap) + "】");
    }

    /**
     * 批量格式化属性
     *
     * @param list
     */
    private void formatExtAttrs(List<ExtAttrNameValue> list) {
        for (ExtAttrNameValue extAttrNameValue : list) {
            formatAttr(extAttrNameValue);
        }
    }

    /**
     * 批量格式化属性
     *
     * @param list
     */
    private void formatAttrs(List<AttributeCategoryValueRelation> list) {
        for (AttributeCategoryValueRelation acvr : list) {
            formatAttr(acvr);
        }
    }

    /**
     * 格式化一个属性
     *
     * @param bean
     */
    private void formatAttr(AttributeCategoryValueRelation bean) {
        bean.setAttributeNameName(subString(bean.getAttributeNameName()));//格式化属性名称
        bean.setAttributeValueValue(subString(bean.getAttributeValueValue()));//格式化名称对应的属性值
    }

    /**
     * 格式化一个属性
     *
     * @param bean
     */
    private void formatAttr(ExtAttrNameValue bean) {
        bean.setExtendsAttrName(subString(bean.getExtendsAttrName()));//格式化属性名称
        bean.setExtendsAttrValue(subString(bean.getExtendsAttrValue()));//格式化名称对应的属性值
    }

    /**
     * 取出内层括号内的内容
     * 如传入color（颜色）返回颜色
     *
     * @param str
     * @return
     */
    private String subString(String str) {
        String result = null;
        int beginIndex = str.lastIndexOf("(");
        int endIndex;
        if (beginIndex >= 0) {
            beginIndex++;
            String tmp = str.substring(beginIndex);
            endIndex = tmp.indexOf(")");
            if (endIndex >= 0) {
                result = tmp.substring(0, endIndex);
            }
        }
        return null != result ? result : str;
    }

    /**
     * 设置类目下的商品的属性和属性值
     * 如上商品001 颜色：黑色，尺寸s
     *
     * @param goodsSeriesList
     */
    private void processOnlineGoodsSeries(List<GoodsSeries> goodsSeriesList) {
        for (int i = 0; i < goodsSeriesList.size(); i++) {
            //设置商品属性
            processOnlineOneGoodsSeries(goodsSeriesList.get(i));
            //设置spu扩展属性
            processOnlineGoodsSeriesExtNameValues(goodsSeriesList.get(i));
        }
    }

    /**
     * 通过 spu ID 设置SPU 扩展属性
     *
     * @param goodsSeries
     */
    private void processOnlineGoodsSeriesExtNameValues(GoodsSeries goodsSeries) {
        goodsSeries.setExtAttrNameValueList(extAttrNameValueService.qryByGoodsSeriesId(goodsSeries.getId()));
    }

    /**
     * 处理单个类目，设置这个类目下的所有商品的属性值，如：颜色和尺寸值等
     *
     * @param goodsSeries
     */
    private void processOnlineOneGoodsSeries(GoodsSeries goodsSeries) {
        processOnlineGoodsList(goodsSeries.getGoodsList());

    }

    /**
     * 批量处理商品
     *
     * @param list
     */
    private void processOnlineGoodsList(List<Goods> list) {
        for (Goods goods : list) {
            processOnlineOneGoods(goods);
            if(null ==goods.getGoodsAttrNameValueList() || 0==goods.getGoodsAttrNameValueList().size()){
                logger.info("源数据 商品SKU:" + goods.getId() + "无对应商品属性，执行SQL【" + "SELECT g.id goods_Id,ga.id goods_Attr_Id,ga.name goods_Attr_Name,gav.value goods_attr_value_value,gav.id goods_attr_value_id\n" +
                        "FROM goods g\n" +
                        "JOIN goods_goods_attr_value_relation ggavr ON ggavr.goods_id=g.id\n" +
                        "JOIN goods_attr ga ON ggavr.goods_attr_id=ga.id\n" +
                        "JOIN goods_attr_value gav ON gav.id=ggavr.goods_attr_value_id\n" +
                        "WHERE g.id=" + goods.getId());
            }
        }
    }

    /**
     * 通过商品ID查询出商品的属性值集合，并填充到goods的对应属性中
     *
     * @param goods
     */
    private void processOnlineOneGoods(Goods goods) {
        goods.setGoodsAttrNameValueList(goodsSeriesService.getAttrNameValueByGoodsId(goods.getId()));
    }

    /**
     * 验证类目是否在类目映射表中存在
     *
     * @param list
     */
    private void validateGoodsSeries(List<GoodsSeries> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!validateOneGoodsSeries(list.get(i))) {
                logger.error("****类目不存在对应关系中【"+JSON.toJSONString(list.get(i))+"】");
                list.remove(i);
                i--;
            }
        }
    }

    /**
     * 验证一个类目在类目映射表中是否存在
     * 用的是旧的类目ID
     *
     * @param goodsSeries
     * @return
     */
    private boolean validateOneGoodsSeries(GoodsSeries goodsSeries) {
        if (!seriesCategoryIdsMap.containsKey(String.valueOf(goodsSeries.getId()+"_"+goodsSeries.getCategoryId().toString()))) {//源数据中的类目ID不在map中则日志输出
            logger.error("no old category id in data ==>" + JSON.toJSONString(goodsSeries));
            return false;
        }
        //验证扩展属性
        validateExtAttr(goodsSeries.getExtAttrNameValueList()) ;
        return true;
    }

    /**
     * 验证源数据扩展属性在目标扩展属性是否存在（批量）
     * 如果不存在则对应的源数据扩展属性移除
     * @param list
     */
    private void validateExtAttr(List<ExtAttrNameValue> list){
        if(null !=list && 0<list.size()){
            for(int i=0 ;i< list.size() ;i++){
                if(!validateExtAttr(list.get(i))){
                    list.remove(i);
                    i-- ;
                }
            }
        }
    }

    /**
     * 验证源数据扩展属性在目标扩展属性是否存在（单）
     * 如果验证成功，就更新内存中扩展属性值得关系ID
     * @param extAttrNameValue
     * @return 如果目标库存在这个扩展属性返回true，否则返回false，
     */
 private boolean validateExtAttr(ExtAttrNameValue extAttrNameValue){
      String key  = concatTargetacvrMapKey(seriesCategoryIdsMap.get(extAttrNameValue.getGoodsSeriesId().toString()+"_"+extAttrNameValue.getCategoryId().toString()),extAttrNameValue.getExtendsAttrName(),extAttrNameValue.getExtendsAttrValue());
      if(targeExtAttrNameValueMap.containsKey(key)){
          extAttrNameValue.setEavrId(targeExtAttrNameValueMap.get(key).getEavrId());
          return true ;
      }else{
          logger.error("源库：categoryId="+extAttrNameValue.getCategoryId().toString()+",扩展属性 【extendsAttrId:"+extAttrNameValue.getExtendsAttrId()+",extendsAttrName:"+extAttrNameValue.getExtendsAttrName()+",extendsAttrValueId:"+extAttrNameValue.getExtendsAttrValueId()+",extendsAttrValue:"+extAttrNameValue.getExtendsAttrValue()+" 】在目标库类目【"+seriesCategoryIdsMap.get(extAttrNameValue.getGoodsSeriesId().toString()+"_"+extAttrNameValue.getCategoryId().toString())+"】无对应");
          return false ;
      }

 }
    /**
     * 设置所有的类目下的所有商品下的所有属性的ID
     *
     * @param goodsSeriesList
     */
    private void processTargetGoodsSeries(List<GoodsSeries> goodsSeriesList) {

        for (GoodsSeries goodsSeries : goodsSeriesList) {
            processTargetOneGoodsSeries(goodsSeries);
        }
    }

    /**
     * 设置一个类目下所有的商品的所有属性的ID
     *
     * @param goodsSeries
     */
    private void processTargetOneGoodsSeries(GoodsSeries goodsSeries) {
        processTargetGoods(goodsSeries.getGoodsList());
        //更新类目为新的类目ID
        updSeriesField(Long.valueOf(seriesCategoryIdsMap.get(goodsSeries.getId()+"_"+goodsSeries.getCategoryId().toString())), goodsSeries);
        //保存数据

        try {
//            goodsSeriesService.save(goodsSeries);
            print(goodsSeries);
        } catch (Exception e) {
            logger.error("save target date is error ==>" + e);
            logger.error("this data is ==>" + JSON.toJSONString(goodsSeries));
//            e.printStackTrace();
        }

    }

    /**
     * 设置一个类目下的所有商品的所有属性的ID
     *
     * @param goodsList
     */
    private void processTargetGoods(List<Goods> goodsList) {
        for (Goods goods : goodsList) {
            processTargetOneGoods(goods);
        }
    }

    /**
     * 设置一个类目下的所有商品中的一个商品的所有属性ID
     *
     * @param goods
     */
    private void processTargetOneGoods(Goods goods) {
        processTargetAcvr(goods.getGoodsAttrNameValueList(),goods.getSeriesId() ,goods.getCategoryId().toString());
    }

    /**
     * 设置一个类目下一个商品的所有商品属性值得ID
     *
     * @param goodsAttrNameValueList
     * @param seriesId 系列ID
     * @param  categoryId 旧类目ID
     */
    private void processTargetAcvr(List<GoodsAttrNameValue> goodsAttrNameValueList,Long seriesId, String categoryId) {
        for (int i = 0; i < goodsAttrNameValueList.size(); i++) {
            if (!processTargetOneAcvr(goodsAttrNameValueList.get(i),seriesId, categoryId)) {
                goodsAttrNameValueList.remove(i);
                i--;
            }
        }
    }

    /**
     * 设置一个商品一个商品属性值对应关系的ID
     * 设置成功返回true，设置失败返回false
     *
     * @param nameValue 旧库属性
     * @param categoryId 旧类目
     * @return
     */
    private boolean processTargetOneAcvr(GoodsAttrNameValue nameValue, Long seriesId,String categoryId) {
        String emp;
        String acvrMapKey = concatTargetacvrMapKey(seriesCategoryIdsMap.get(String.valueOf(seriesId)+"_"+categoryId), nameValue.getGoodsAttrName(), nameValue.getGoodsAttrValueValue());
        if (targetacvrMap.containsKey(acvrMapKey)) {//存在acvrID
            nameValue.setAcvrId(targetacvrMap.get(acvrMapKey).getId());//设置的ACVRID
            return true;
        } else {
            if ("尺码".equals(nameValue.getGoodsAttrName())) {
                acvrMapKey = concatTargetacvrMapKey(seriesCategoryIdsMap.get(String.valueOf(seriesId)+"_"+categoryId).toString(), "尺寸", nameValue.getGoodsAttrValueValue());
                if (targetacvrMap.containsKey(acvrMapKey)) {//存在acvrID
                    printAdd("尺码", "尺寸", nameValue, categoryId, targetacvrMap.get(acvrMapKey));
                   nameValue.setAcvrId(targetacvrMap.get(acvrMapKey).getId());//设置设置的ACVRID
                    return true;
                }
            } else if ("尺寸".equals(nameValue.getGoodsAttrName())) {
                acvrMapKey = concatTargetacvrMapKey(seriesCategoryIdsMap.get(String.valueOf(seriesId)+"_"+categoryId).toString(), "尺码", nameValue.getGoodsAttrValueValue());
                if (targetacvrMap.containsKey(acvrMapKey)) {//存在acvrID
                    printAdd("尺寸", "尺码", nameValue, categoryId, targetacvrMap.get(acvrMapKey));
                    nameValue.setAcvrId(targetacvrMap.get(acvrMapKey).getId());//设置设置的ACVRID
                    return true;
                }
            }
            //异常数据
            logger.error("目标库attribute_category_value_relation没有对应数据，目标库类目ID:" +seriesCategoryIdsMap.get(String.valueOf(seriesId)+"_"+categoryId).toString());
            logger.error("源数据 商品属性值 在目标库attribute_category_value_relation表无对应关系,源数据==>" + "【categoryId=" + categoryId + ",nameValueJson=" + JSON.toJSONString(nameValue) + "】");
//           throw new RuntimeException("商品的属性和值不存在");
            return false;
        }
    }

    /**
     * 拼接TargetacvrMapKey
     *
     * @param categoryId
     * @param name
     * @param value
     * @return
     */
    private String concatTargetacvrMapKey(String categoryId, String name, String value) {
        String splitStr = "_";
        return categoryId.concat(splitStr).concat(name).concat(splitStr).concat(value);
    }

    private void run() {
        GoodsSeriesQuery queryBean = new GoodsSeriesQuery();
        queryBean.setTrade(trade);//出口

        //获取线上类目数据
        List<GoodsSeries> goodsSeriesList = goodsSeriesService.qryBatchGoodSeries(queryBean);

        logger.warn("=========过滤源数据系列（业务方提供了系列） 开始================");
        logger.warn("----源数据系列过滤前大小【" + goodsSeriesList.size() + "】");
//        logger.warn("----源数据系列过滤前数据【"+JSON.toJSONString(goodsSeriesList)+"】");

        //过滤掉没有在已经给出的系列类目关系的数据
        filterSeriesList(goodsSeriesList);
        //第一次验证业务方数据的合法性
        validateSeriesCategoryIdsMap(goodsSeriesList);
        logger.warn("----源数据系列过滤后大小【"+goodsSeriesList.size()+"】");
//        logger.warn("----源数据系列过滤后数据【"+JSON.toJSONString(goodsSeriesList)+"】");
        logger.warn("=========过滤源数据系列（业务方提供了系列） 结束================");

        //设置源数据：商品的属性和属性值 ,设置spu扩展属性
        processOnlineGoodsSeries(goodsSeriesList);
//         exportExcel(goodsSeriesList);

        //数据类目验证
        validateGoodsSeries(goodsSeriesList);

        //数据库切换到目标库
        DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TARGET);

        //设置目标库对应的属性
       processTargetGoodsSeries(goodsSeriesList);
    }

    private void validateSeriesCategoryIdsMap(  List<GoodsSeries> goodsSeriesList){
        List<String> tmpList = new ArrayList<String>();
        for(GoodsSeries goodsSeries:goodsSeriesList){
            tmpList.add(String.valueOf(goodsSeries.getId())+"_"+goodsSeries.getCategoryId().toString());
        }

       Iterator<Map.Entry<String,String>> it = seriesCategoryIdsMap.entrySet().iterator();
        while (it.hasNext()){
            String key = it.next().getKey();
           if( tmpList.contains( key)){
               continue;
           }else{
               logger.error("*****业务方数据有在线上没有对应"+key);
           }
        }
    }

    /**
     * 如果serie不在映射关系里面就移除
     * @param list
     */
    private void filterSeriesList(List<GoodsSeries> list){
        for(int i =0 ; i<list.size() ;i++){
           if(isSeriesFilter(list.get(i))){
//               logger.error("****源数据系列ID:"+list.get(i).getId()+"不在业务方给的映射关系里面。【"+JSON.toJSONString(list.get(i))+"】");
               logger.error("****源数据系列ID:"+list.get(i).getId()+"不在业务方给的映射关系里面。");
              list.remove(i);
               i--;
           }
        }
    }

    /**
     * 如果 seriesCategoryIdsMap有这个seriesid _categoryId这是需要我们处理，不要过滤，返回false，否则返回true
     * @param goodsSeries
     * @return
     */
    private boolean isSeriesFilter(GoodsSeries goodsSeries){
        return seriesCategoryIdsMap.containsKey(String.valueOf(goodsSeries.getId())+"_"+goodsSeries.getCategoryId().toString())==true?false:true;
    }
    /**
     * 可以更新goods对象中seriesId和goodsSeries对中的iD
     *
     * @param newId
     * @param goodsSeries
     */
    private void updSeriesField(Long newId, GoodsSeries goodsSeries) {
        for (Goods goods : goodsSeries.getGoodsList()) {//设置goods中的category为新的categoryID
            goods.setCategoryId(newId);
        }

        //设置系列的categoryId为newid
        goodsSeries.setCategoryId(newId);
    }



    /**
     * test 数据绑定 COUNTRYID
     *
     * @param countryId
     * @param goodsSeries
     */
    private void testBuildingCountryId(Integer countryId, GoodsSeries goodsSeries) {
        for (Goods goods : goodsSeries.getGoodsList()) {//设置goods中的category为新的categoryID
            goods.setCountryId(countryId);
        }
        goodsSeries.setCountryId(countryId);
    }

    private void printAdd(String srcName, String targetName, GoodsAttrNameValue nameValue, String catgoryId, AttributeCategoryValueRelation attributeCategoryValueRelation) {

        String srcFileName = archiveFileName; //
        FileWriter writer = null;
        try {
            writer = new FileWriter(srcFileName, true);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write("旧类目: oldCatgoryId=" + catgoryId + ",goodsId=" + nameValue.getGoodsId() + "属性：" + srcName + "在目标库无对应。但是可以相似匹配新类目 categoryId =" + attributeCategoryValueRelation.getCategoryId() + ",属性" + targetName);
            bw.newLine();
            bw.flush();
            if (bw != null)
                bw.close();
            if (writer != null)
                writer.close();
        } catch (IOException e) {
            logger.error(e);
        }


    }

    private void print(GoodsSeries goodsSeries) throws IOException {

        String srcFileName =archiveFileName; //
        FileWriter writer = null;
        try {
            writer = new FileWriter(srcFileName, true);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        BufferedWriter bw = new BufferedWriter(writer);
        for (Goods goods : goodsSeries.getGoodsList()) {
//            logger.info("oldGoodsId=" + goods.getBeforeId() + ",newGoodsId=" + goods.getId());
            bw.write("oldGoodsId=" + goods.getBeforeId() + ",newGoodsId=" + goods.getId());
            bw.newLine();
//        for(GoodsAttrNameValue nameValue = goods.getGoodsAttrNameValueList())
        }
        bw.flush();
        if (bw != null)
            bw.close();
        if (writer != null)
            writer.close();


    }

    public static void main(String[] arg0) {
//        PropertyConfigurator.configure("log4j.properties");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        GoodsSeriesMigration goodsSeriesMigration = (GoodsSeriesMigration) ctx.getBean("goodsSeriesMigration");
        goodsSeriesMigration.run();
//        String str="color((颜色))";
//        GoodsSeriesMigration migration = new GoodsSeriesMigration();
//       logger.info( migration.subString(str));
    }

//    private void exportExcel(List<GoodsSeries> list){
//        for(int i=0 ; i<list.size() ;i++){
//            exportExcel(list.get(i),i);
//        }
//    }
//    private void exportExcel(GoodsSeries goodsSeries ,int index){
//        String filename ="E:\\excel\\";
//        if(index ==1){
//           filename=filename+String.valueOf(goodsSeries.getId())+".csv";
//        }else if(index %10) {
//
//        }
//    }


}

/**
============= 扩展属性 目标==================
SELECT
  extended_attribute_category_id,
  ea.id extended_attribute_id,
  extended_attribute_value_id extends_attr_value_id,
   SUBSTRING(TRIM(ea.NAME),LOCATE('(',TRIM(ea.NAME))+1,LOCATE(')',TRIM(ea.NAME))-LOCATE('(',TRIM(ea.NAME))-1) NAME,
 SUBSTRING(TRIM(eav.value ),LOCATE('(',TRIM(eav.value ))+1,LOCATE(')',TRIM(eav.value ))-LOCATE('(',TRIM(eav.value ))-1) VALUE,

  eavr.id eavr_Id,
  category_id
FROM
  extended_attribute_value_relation eavr
  JOIN extended_attribute_category eac
    ON eavr.extended_attribute_category_id = eac.id
  JOIN extended_attribute ea
    ON ea.id = eac.extended_attribute_id
  JOIN extended_attribute_value eav
    ON eav.id = eavr.extended_attribute_value_id
WHERE category_id = 223009



==============扩展属性 源数据库============
select extended_attr.id,extended_attr.name, extended_attr_value.value from extended_attr_value join extended_attr
on extended_attr_value.extends_attr_id=extended_attr.id
where category_id=206008001;




=============商品属性 目标库 ===============

 SELECT acvr.id,
              c.id categoryId,
          attribute_category_id,
          a.id  attributeId,
          attribute_value_id,
        a.name attributeNameName,
       av.value attributeValueValue
      FROM(SELECT * FROM category c1 WHERE  NOT EXISTS (SELECT 1 FROM category c2 WHERE c2.parent_id=c1.id)) c
          JOIN attribute_category ac ON ac.`category_id`=c.id
          JOIN attribute a ON a.id=ac.`attribute_id`
          JOIN attribute_category_value_relation acvr ON acvr.`attribute_category_id`=ac.id
          JOIN attribute_value av ON  acvr.`attribute_value_id`=av.`id`
          WHERE c.id = 243006001



=========================商品属性   源数据库=========================


SELECT
  g.category_id,
  g.id goods_Id,
  ga.id goods_Attr_Id,
  ga.name goods_Attr_Name,
  gav.value goods_attr_value_value,
  gav.id goods_attr_value_id
FROM
  goods g
  JOIN goods_goods_attr_value_relation ggavr
    ON ggavr.goods_id = g.id
  JOIN goods_attr ga
    ON ggavr.goods_attr_id = ga.id
  JOIN goods_attr_value gav
    ON gav.id = ggavr.goods_attr_value_id
WHERE g.category_id = 1
**/