package com.if2c.harald.migration;

import com.alibaba.fastjson.JSON;
import com.if2c.harald.beans.*;
import com.if2c.harald.beans.query.AttributeCategoryValueRelationQuery;
import com.if2c.harald.beans.query.ExtAttrNameValueQuery;
import com.if2c.harald.beans.query.GoodsSeriesQuery;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:商品类目迁移
 * dao层方法规则：线上查询用qry*，目标库是get*
 * User: Courser
 * Date: 14-4-28
 * Time: 下午4:35
 */
@Component
public class GoodsSeriesMigrationMini {
    private static final Logger logger = Logger.getLogger(GoodsSeriesMigrationMini.class);
    /**
     * 业务方提供文件
     */
    private static  final String readFileName ="d:\\123.txt";
    /**
     * 归档文件
     */
    private static  final String archiveFileName="archive.data";


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
            if(map.containsKey(s2cr.getSeriesId()+"_"+s2cr.getOldCategoryId()) && map.containsValue(s2cr.getNewCategoryId().toLowerCase())){
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
                    if("-1".equals(arrays[2])){
                        logger.error("业务方没有给出映射关系：【"+tempString+"】");
                    }else{
//                        categoryMap.put(arrays[1].toString(),arrays[2].toString());//key:旧类目，value:新类目
//                        seriesCategoryIdsList.add(arrays[0].toString()+"_"+arrays[1].toString());//系列_类目
                        seriesCategoryIdsMap.put(arrays[0].toString()+"_"+arrays[1].toString(),arrays[2]);//系列_类目,新类目
                        s2crList.add(new S2cr(arrays[0],arrays[1],arrays[2]));
                    }

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
     * 初始化新的扩展属性与类目的关系
     */
    private void initExtAttrValueMap(Long categoryId) {
        List<ExtAttrNameValue> extAttrNameValueList = extAttrNameValueService.getAll(new ExtAttrNameValueQuery(categoryId));
        //格式化扩展属性
        formatExtAttrs(extAttrNameValueList);
        logger.warn("------目标库所有扩展属性集合大小【"+extAttrNameValueList.size()+"】");
        for (ExtAttrNameValue extAttrNameValue : extAttrNameValueList) {

            if (seriesCategoryIdsMap.containsValue(extAttrNameValue.getCategoryId().toString()))//类目ID相同，则有对应关系
                targeExtAttrNameValueMap.put(extAttrNameValue.getCategoryId().toString()+ "_" + extAttrNameValue.getExtendsAttrName() + "_" + extAttrNameValue.getExtendsAttrValue(), extAttrNameValue);
            else
                logger.error("****目标库此扩展属性被过滤掉【"+JSON.toJSONString(extAttrNameValue)+"】");
        }
        logger.warn("--------目标库与有效的{旧系列_旧类目， 新类目}关系对比后 大小【"+targeExtAttrNameValueMap.size()+"】");

        logger.warn("--------目标库与有效的{旧系列_旧类目， 新类目}关系对比后 有效数据（新类目_扩展属性_值）【" + JSON.toJSON(targeExtAttrNameValueMap) + "】");
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
          logger.error("源库：categoryId="+extAttrNameValue.getCategoryId().toString()+",扩展属性 【"+extAttrNameValue.getExtendsAttrName()+","+extAttrNameValue.getExtendsAttrValue()+" 】在目标库类目【"+seriesCategoryIdsMap.get(extAttrNameValue.getGoodsSeriesId().toString()+"_"+extAttrNameValue.getCategoryId().toString())+"】无对应,Json:"+JSON.toJSONString(extAttrNameValue));
          return false ;
      }

 }


    /**
     * 设置一个类目下所有的商品的所有属性的ID
     *
     * @param goodsSeries
     */
    private void processTargetOneGoodsSeries(GoodsSeries goodsSeries) {
        //初始化商品属性集合
        initTargetCategoryAttr(goodsSeries.getCategoryId());

        //设置目标库这个SPU对应的所有商品的属性
        processTargetGoods(goodsSeries.getGoodsList());
        //可以不清除
        targetacvrMap.clear();

        //更新类目为新的类目ID，同时设置扩展属性
        updSeriesField(Long.valueOf(seriesCategoryIdsMap.get(goodsSeries.getId()+"_"+goodsSeries.getCategoryId().toString())), goodsSeries);


    }

    /**
     * 根据类目获取目标类目属性
     * @param categoryId
     */
    private void initTargetCategoryAttr(Long categoryId){
        AttributeCategoryValueRelationQuery query = new AttributeCategoryValueRelationQuery(categoryId);
        List<AttributeCategoryValueRelation> attrNameValueList = attributeCategoryValueRelationService.getAll(query);
        if(null != attrNameValueList){
            //格式化属性值
            formatAttrs(attrNameValueList);
            for (AttributeCategoryValueRelation acvr : attrNameValueList) {
                if (seriesCategoryIdsMap.containsValue(acvr.getCategoryId().toString())){
                    targetacvrMap.put(acvr.getCategoryId().toString() + "_" + acvr.getAttributeNameName() + "_" + acvr.getAttributeValueValue(), acvr);
                }else{
                    logger.error("*****目标库此商品属性被过滤掉【" + JSON.toJSONString(acvr) + "】");
                }
            }
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
     * @param nameValue 旧的属性
     * @param seriesId 旧seriesID
     * @param categoryId 旧类目
     * @return
     */
    private boolean processTargetOneAcvr(GoodsAttrNameValue nameValue, Long seriesId,String categoryId) {
        String acvrMapKey = concatTargetacvrMapKey(seriesCategoryIdsMap.get(String.valueOf(seriesId)+"_"+categoryId.toString()), nameValue.getGoodsAttrName(), nameValue.getGoodsAttrValueValue());
        if (targetacvrMap.containsKey(acvrMapKey)) {//存在acvrID
            nameValue.setAcvrId(targetacvrMap.get(acvrMapKey).getId());//设置的ACVRID
            return true;
        } else {
//            if ("尺码".equals(nameValue.getGoodsAttrName())) {
//                acvrMapKey = concatTargetacvrMapKey(seriesCategoryIdsMap.get(String.valueOf(seriesId)+"_"+categoryId).toString(), "尺寸", nameValue.getGoodsAttrValueValue());
//                if (targetacvrMap.containsKey(acvrMapKey)) {//存在acvrID
//                    printAdd("尺码", "尺寸", nameValue, categoryId, targetacvrMap.get(acvrMapKey));
////                   nameValue.setAcvrId(targetacvrMap.get(acvrMapKey).getId());//设置设置的ACVRID
//                    return false;
//                }
//            } else if ("尺寸".equals(nameValue.getGoodsAttrName())) {
//                acvrMapKey = concatTargetacvrMapKey(seriesCategoryIdsMap.get(String.valueOf(seriesId)+"_"+categoryId).toString(), "尺码", nameValue.getGoodsAttrValueValue());
//                if (targetacvrMap.containsKey(acvrMapKey)) {//存在acvrID
//                    printAdd("尺寸", "尺码", nameValue, categoryId, targetacvrMap.get(acvrMapKey));
//                    return false;
//                }
//            }
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
        for(S2cr item : s2crList){
            /*********************** 源数据库 ********************************/
            GoodsSeriesQuery query = new GoodsSeriesQuery(2,Long.valueOf(item.getOldCategoryId()),Long.valueOf(item.getSeriesId()));
            //获取源数据SPU数据
            GoodsSeries goodsSeries = goodsSeriesService.qryGoodsSeries(query);
            if(null ==goodsSeries){
                logger.error("源数据goodsSeriesId:"+item.getSeriesId()+",categoryId:"+item.getOldCategoryId()+"不存在");
                continue;
            }
            //设置商品属性
            processOnlineOneGoodsSeries(goodsSeries);
            //设置spu扩展属性
            processOnlineGoodsSeriesExtNameValues(goodsSeries);

            /*********************** 目标数据库 ********************************/
            //数据库切换到目标库
            DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TARGET);

            //设置目标库对应的属性
            processTargetOneGoodsSeries(goodsSeries);


            //初始化扩展属性
            initExtAttrValueMap(goodsSeries.getCategoryId());

            //验证扩展属性并设置扩展属性对应关系
            validateExtAttr(goodsSeries.getExtAttrNameValueList()) ;

            try {
                goodsSeriesService.save(goodsSeries);
                print(goodsSeries);
            } catch (Exception e) {
                logger.error("save target date is error ==>" + e);
                logger.error("this data is ==>" + JSON.toJSONString(goodsSeries));
            }

            //数据库切换到源库
            DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_ONLINE);
        }









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

        String srcFileName = "d:\\11111111111111.txt"; //
        FileWriter writer = null;
        try {
            writer = new FileWriter(srcFileName, true);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        BufferedWriter bw = new BufferedWriter(writer);
        for (Goods goods : goodsSeries.getGoodsList()) {
            logger.info("oldGoodsId=" + goods.getBeforeId() + ",newGoodsId=" + goods.getId());
            bw.write("oldGoodsId=" + goods.getBeforeId() + ",newGoodsId=" + goods.getId());
            bw.newLine();
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
        GoodsSeriesMigrationMini goodsSeriesMigration = (GoodsSeriesMigrationMini) ctx.getBean("goodsSeriesMigrationMini");
        goodsSeriesMigration.run();
//        String str="color((颜色))";
//        GoodsSeriesMigration migration = new GoodsSeriesMigration();
//       logger.info( migration.subString(str));
    }

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