package com.if2c.harald.migration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.if2c.harald.beans.Giftcard;
import com.if2c.harald.beans.Goods;
import com.if2c.harald.tools.DesUtil;


public class GoodsPriceExportTask extends ImageMigrationTask {

	public GoodsPriceExportTask() throws FileNotFoundException, IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public void run() throws SQLException {
		conn = getConnection();
		File file = new File("E:\\xxxx.csv");
		try {
			file.createNewFile();
			Goods goods=null;
			export(goods, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		conn.close();
	}
	
	public void export(Goods goods, File toFile) throws SQLException  {
		List<Goods> listGoods=getAllGoods();

		String sparator = ",";
		try {
			File file = toFile;
			file.createNewFile();
			OutputStreamWriter pw = null;
			pw = new OutputStreamWriter(new FileOutputStream(file), "GB2312");
			pw.write("订单编号");
			pw.write(sparator);		
			pw.write("商品金额(原价)");
			pw.write(sparator);
			pw.write("\r\n");
			StringBuffer password;
			String p;
			for (int j=0;j<listGoods.size();j++) {
				String num = listGoods.get(j).getNum();	
				String name=listGoods.get(j).getSnapshot();
				 Map a = (Map)JSON.parse(name);
		   	       Map b= (Map)a.get("goods");
		   	       Float price = Float.valueOf(b.get("price").toString());				
		   	    String price1=String.valueOf(price);
                pw.append(num).append(sparator)
                        .append(price1).append(sparator)
                      ;
                        
				pw.write("\r\n");
			
			}

			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Goods> getAllGoods() throws SQLException
	{
		String sql = "select orders.order_num ,order_goods_relation.`snapshot` "
             +" from orders"
             +" left join user on user.id=orders.user_id"
             +" left join deliver_type_defination on deliver_type_defination.code=orders.deliver_type"
             +" left join order_goods_relation on order_goods_relation.order_id=orders.id"
             +" left join shop_brand on shop_brand.brand_id=orders.brand_id"
             +" left join shop on shop.id=shop_brand.shop_id"
             +" left join seller on seller.id=shop.seller_id"
             +" left join goods on goods.id=order_goods_relation.goods_id"
             +" left join category on category.id=goods.category_id"
             +" left join shop_deduction on shop_deduction.category_id=category.parent_id"
             +" left join goods_series on goods_series.id=goods.series_id"
             +" where shop_deduction.shop_id=shop.id and goods.name is not null and orders.`status` in (1,2,3,4,5,6) and"
             +" orders.create_time>'2014-01-25 00:00:00' and orders.create_time<'2014-04-18 00:00:00' order by orders.order_num";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List <Goods> noType = new ArrayList<Goods>();
		List<Goods> GoodsList = new ArrayList<Goods>();
		while (rs.next()) {
			Goods goods = new Goods();	
			goods.setNum(rs.getString("order_num"));		
			goods.setSnapshot(rs.getString("snapshot"));		
				GoodsList.add(goods);
		}
		return GoodsList;
		
	}
	
	
	public static void main(String[] args) throws FileNotFoundException,
	IOException, SQLException {
		GoodsPriceExportTask task = new GoodsPriceExportTask();
 task.run();
 System.exit(0);
}

}
