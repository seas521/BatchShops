package com.if2c.harald.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.if2c.harald.beans.PictureManage;
import com.if2c.harald.db.Config;
import com.if2c.harald.mail.MailCreator;
import com.if2c.harald.tools.BackupLog;
import com.if2c.harald.tools.DateUtils;
import com.if2c.harald.tools.HttpClients;

/**
 * 更新首页展示的无效商品 1、首页每个展示的每个单品在后台都配有备用商品，单展示商品失效时，展示备用商品
 * 2、替换商品时发邮件通知运营人员；商品即将失效时发邮件预警
 * 
 * @author zhw
 * 
 *         2014-7-1
 */
public class IndexGoodsUpdateJob extends JobBase {
	public IndexGoodsUpdateJob() {
		super();
	}

	Connection conn = null;
	int dispalyGoodsId = 0;
	int backGoodsId = 0;
	String dispalyPictureId = null;
	String backPictureId = null;
	String displayId = null;
	String backId = null;
	int position = 0;
	Map<String, String> dataMap = new HashMap<String, String>();
	String hostName=Config.getConf().getHomePageHost().getHost();
	String to = "huanglu@izptec.com;caijianmin@izptec.com;liuxiaonan@izptec.com;wucunjie@izptec.com";// 收件人
	String cc = "zhonghuawei@izptec.com";// 邮件抄送人
	
	//String to = "shiyirui@izptec.com;zhangying1@izptec.com;zhanglan@izptec.com;yanjianying@izptec.com;zhouqian@izptec.com";// 收件人
	//String cc = "dengruoyu@izptec.com;zhonghuawei@izptec.com";// 邮件抄送人
	String teplateHtml = "indexLimitWarningEmail.html";
	String teplateHtmlPre = "indexLimitPreWarningEmail.html";
	String from = "Noreply-service@haixuan.com";
	int oneHour = 3600000;
	int halfHour= 1800000;

	public void run() {
		if(hostName.equalsIgnoreCase("http://www.haixuan.com")){
			to = "shiyirui@izptec.com;zhangying1@izptec.com;zhanglan@izptec.com;yanjianying@izptec.com;zhouqian@izptec.com";// 收件人
			cc = "dengruoyu@izptec.com;zhonghuawei@izptec.com";// 邮件抄送人
		}
		try {
			conn = this.getConnection();
			 //DealLimitGoods();//处理限时抢购商品
			 DealFloorGoods();// 处理楼层商品
			 DealOtherGoods();// 处理秒杀、团购、新品
			 DealBackPreparedGoods();//处理后台备选商品

		} catch (SQLException e) {
			BackupLog.logError("function run runs fail", e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				conn = null;
			}
		}

	}

	/**
	 * by zhw
	 * 监测后台备选商品，失效
	 * 
	 */
	private void DealBackPreparedGoods() {
		List<PictureManage> backOtherGoodsList = getActiveData("picture_manage");
		List<PictureManage> backFloorGoodsList = getFloorsData("picture_manage");
		List<PictureManage> backFloorPreparedGoodsList=new ArrayList<PictureManage>();
		List<PictureManage> miaoshaPreparedBackGoodsList=new ArrayList<PictureManage>();
		List<PictureManage> tuangouPreparedBackGoodsList=new ArrayList<PictureManage>();
		List<PictureManage> newPreparedBackGoodsList=new ArrayList<PictureManage>();
		//监控楼层备选商品
		int size=backFloorGoodsList.size();
		for (int i = 0; i < size; i++) {
			if (i % 2 == 1) {
				backFloorPreparedGoodsList.add(backFloorGoodsList.get(i));
			}
		}
		int n = backFloorPreparedGoodsList.size();
		for (int i = 0; i < n; i++) {
			backGoodsId = backFloorPreparedGoodsList.get(i).getGoods_serries_id();
			backId = backFloorPreparedGoodsList.get(i).getId();
			if(!valid(backGoodsId)){
				setStatusBack(backId);
			}
		}
		//后台秒杀备选
		for (int i = 5; i < 10; i++) {
			if(backOtherGoodsList.get(i).getGoods_serries_id()!=0){
				miaoshaPreparedBackGoodsList.add(backOtherGoodsList.get(i));
			}
		}
		for(int i=0;i<miaoshaPreparedBackGoodsList.size();i++){
			backId = miaoshaPreparedBackGoodsList.get(i).getId();
			backGoodsId = miaoshaPreparedBackGoodsList.get(i).getGoods_serries_id();
			
			String endTime = miaoshaPreparedBackGoodsList.get(i).getEndTime();
			
			if((!promotionGoodsValid(backGoodsId, 4)
					|| !WithinTime(endTime))){
				setStatusBack(backId);
			}
		}
		//后台团购备选
		for (int i = 15; i < 20; i++) {
			if(backOtherGoodsList.get(i).getGoods_serries_id()!=0){	
			tuangouPreparedBackGoodsList.add(backOtherGoodsList.get(i));
			}
		}
		for(int i=0;i<tuangouPreparedBackGoodsList.size();i++){
			backId = tuangouPreparedBackGoodsList.get(i).getId();
			backGoodsId = tuangouPreparedBackGoodsList.get(i).getGoods_serries_id();
			
			String endTime = tuangouPreparedBackGoodsList.get(i).getEndTime();
			if((!promotionGoodsValid(backGoodsId, 3)
					|| !WithinTime(endTime))){
				setStatusBack(backId);
			}
		}
		//后台新品备选
		for (int i = 25; i < 30; i++) {
			if(backOtherGoodsList.get(i).getGoods_serries_id()!=0){
				newPreparedBackGoodsList.add(backOtherGoodsList.get(i));
			}
		}
		for(int i=0;i<newPreparedBackGoodsList.size();i++){
			backId = newPreparedBackGoodsList.get(i).getId();
			backGoodsId = newPreparedBackGoodsList.get(i).getGoods_serries_id();
			if(!valid(backGoodsId)){
				setStatusBack(backId);
			}
		}
	}

	/**
	 * by zhw 处理秒杀、团购、新品商品
	 */
	private void DealOtherGoods() {

		List<PictureManage> miaoshaGoodsList = new ArrayList<PictureManage>();
		List<PictureManage> miaoshaBackGoodsList = new ArrayList<PictureManage>();// 存放有效的备选秒杀商品
		List<PictureManage> tuangouGoodsList = new ArrayList<PictureManage>();
		List<PictureManage> tuangouBackGoodsList = new ArrayList<PictureManage>();// 存放有效的备选全球购商品
		List<PictureManage> newGoodsList = new ArrayList<PictureManage>();
		List<PictureManage> newBackGoodsList = new ArrayList<PictureManage>();// 存放备选新品
		// 获取A2商品
		List<PictureManage> otherGoodsList = getActiveData("picture_manage_online");

		// A201-A205秒杀商品，A206-A210备选商品
		for (int i = 0; i < 5; i++) {
			miaoshaGoodsList.add(otherGoodsList.get(i));
		}
		for (int i = 5; i < 10; i++) {
			if(otherGoodsList.get(i).getGoods_serries_id()!=0){
				miaoshaBackGoodsList.add(otherGoodsList.get(i));
			}
		}

		// A211-A215全球购商品，A216-A220备选商品
		for (int i = 10; i < 15; i++) {
			tuangouGoodsList.add(otherGoodsList.get(i));
		}
		for (int i = 15; i < 20; i++) {
			if(otherGoodsList.get(i).getGoods_serries_id()!=0){	
			tuangouBackGoodsList.add(otherGoodsList.get(i));
			}
		}
		
		// A221-A225新品，A226-A230备选商品
		for (int i = 20; i < 25; i++) {
			newGoodsList.add(otherGoodsList.get(i));
		}
		for (int i = 25; i < 30; i++) {
			if(otherGoodsList.get(i).getGoods_serries_id()!=0){
				newBackGoodsList.add(otherGoodsList.get(i));
			}
		}

		DealMiaoshaGoods(miaoshaGoodsList, miaoshaBackGoodsList);// 处理秒杀商品
		DealTuangouGoods(tuangouGoodsList, tuangouBackGoodsList);// 处理全球购商品
		DealNewUploadGoods(newGoodsList, newBackGoodsList);// 处理新品

	}

	/**
	 * by zhw 处理新品
	 * 
	 * @param newGoodsList
	 * @param newBackGoodsList
	 */
	private void DealNewUploadGoods(List<PictureManage> newGoodsList,
			List<PictureManage> newBackGoodsList) {
		
		//监控备选新品
		if (newBackGoodsList.size() > 0) {
		    for(int i = 0; i < newBackGoodsList.size(); i++){
					backPictureId = newBackGoodsList.get(i).getPictureId();
					backId =newBackGoodsList.get(i).getId();
					backGoodsId =newBackGoodsList.get(i).getGoods_serries_id();
					if (!preValid(backGoodsId)
							&& newBackGoodsList.get(i).getIs_send() == 0) {
						preWarning("", "", backPictureId,
								"", position);
						setIsSend(backId);
					}
		    }
		}
			for (int i = 0; i < 5; i++) {
				dispalyPictureId = newGoodsList.get(i).getPictureId();
				displayId = newGoodsList.get(i).getId();
				dispalyGoodsId = newGoodsList.get(i).getGoods_serries_id();
				
				// 预报警 预报警只发一次邮件 is_send记录 0:未发送过 1:发送过
				if (!preValid(dispalyGoodsId)
						&& newGoodsList.get(i).getIs_send() == 0) {
					preWarning(displayId, backId, dispalyPictureId,
							backPictureId, position);
					setIsSend(displayId);
				}
				
				PictureManage toReplaceBackGoods=getOneValidBackGoods();
				if (!valid(dispalyGoodsId)) {
					// 如果显示商品失效，存在备选商品有效，自动替换
					if(toReplaceBackGoods!=null){
						backPictureId = toReplaceBackGoods.getPictureId();
						backId = toReplaceBackGoods.getId();
						backGoodsId = toReplaceBackGoods.getGoods_serries_id();
						// 商品替换,并清缓存发邮件通知
						try {
							updateGoods(displayId, backId, dispalyPictureId,
									backPictureId, position);

							// 后台对应位置商品未被修改则替换
							if (existBackGoodsActive(dispalyPictureId, dispalyGoodsId)
									&& existBackGoodsActive(backPictureId, backGoodsId)) {
								// 运营后台商品替换,不做其他操作
								updateBackGoods(displayId, backId,
										dispalyPictureId, backPictureId,
										dispalyGoodsId, backGoodsId);
							}
						} catch (SQLException e) {
							BackupLog.logError("replace newupload Goods fail", e);
						}
					}
					//如果显示商品失效，没有可替换商品，只改状态
					else{
						setStatus(displayId);
						if (existBackGoodsActive(dispalyPictureId, dispalyGoodsId)){
						setStatusBack(displayId);
						}
					}
					

				}
				
			}
	}

	private boolean existBackGoodsActive(String PictureId,
			int GoodsId) {
		String sql = "select count(1) from picture_manage p where p.picture_id=? and p.goods_serries_id=? ";
		boolean flag = false;
		int intFlag = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, PictureId);
				ps.setInt(2, GoodsId);
				rs = ps.executeQuery();
				if (rs.next()) {
					intFlag = rs.getInt(1);
				}
				if (intFlag != 0) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeRs(rs);
			closePs(ps);
		}

		return flag;
	}

	/**
	 * by zhw 获取一个有效的备选新品
	 * @return
	 */
	public PictureManage getOneValidBackGoods() {
		PictureManage pictureManage =null;
		String sql = "select e.* from goods g "
				+ " left join goods_series gs on g.series_id=gs.id "
				+ " left join picture_manage_online e on e.goods_serries_id=gs.id"
				+ " where g.inventory>0 and g.active=1 and g.is_virtual=1 and g.status=4 and gs.status=4 and gs.is_virtual=1 "
				+ " and e.picture_id >'A225' and e.picture_id<'A231' order by e.picture_id limit 1";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					pictureManage=new PictureManage();
					pictureManage.setPictureId(rs.getString("picture_id"));
					pictureManage.setGoods_serries_id(rs
							.getInt("goods_serries_id"));
					pictureManage.setPosition(rs.getInt("position"));
					pictureManage.setId(rs.getString("id"));
				}
			}

		} catch (SQLException e) {
			//pictureManage=null;
		} finally {
			closeRs(rs);
			closePs(ps);
		}

		return pictureManage;
	}

	/**
	 * by zhw 处理全球购商品（团购）
	 * 
	 * @param tuangouGoodsList
	 * @param tuangouBackGoodsList
	 */
	private void DealTuangouGoods(List<PictureManage> tuangouGoodsList,
			List<PictureManage> tuangouBackGoodsList) {
		// 如果存在有效的备选商品，检验替换
		if (tuangouBackGoodsList.size() > 0) {
			for(int i=0;i<tuangouBackGoodsList.size();i++){
				long backTime = DateUtils.getMillisecond(tuangouBackGoodsList.get(i)
						.getEndTime()) - System.currentTimeMillis();
				backPictureId = tuangouBackGoodsList.get(0).getPictureId();
				backId = tuangouBackGoodsList.get(0).getId();
				// 监控备选团购商品
				if (0 < backTime && backTime < oneHour && tuangouBackGoodsList.get(i).getIs_send() == 0) {
					preWarning("", "", backPictureId,
							"", position);
					setIsSend(backId);
				}
			}
		}
			for (int i = 0; i < 5; i++) {
				long time = DateUtils.getMillisecond(tuangouGoodsList.get(i)
						.getEndTime()) - System.currentTimeMillis();
				
				dispalyPictureId = tuangouGoodsList.get(i).getPictureId();
				
				displayId = tuangouGoodsList.get(i).getId();
				
				dispalyGoodsId = tuangouGoodsList.get(i).getGoods_serries_id();
				String endTime = tuangouGoodsList.get(i).getEndTime();
				
				
				// 预报警 条件：活动时间还剩1小时 预报警只发一次邮件 is_send记录 0:未发送过 1:发送过
				if (0 < time && time < oneHour && tuangouGoodsList.get(i).getIs_send() == 0) {
					preWarning(displayId, backId, dispalyPictureId,
							backPictureId, position);
					setIsSend(displayId);
				}
				
				PictureManage toReplaceBackTuangouGoods=getOneValidBackActiveGoods(3);
				if ((!promotionGoodsValid(dispalyGoodsId, 3)
						|| !WithinTime(endTime))) {
					if(toReplaceBackTuangouGoods!=null){
						backPictureId = toReplaceBackTuangouGoods.getPictureId();
						backId = toReplaceBackTuangouGoods.getId();
						backGoodsId=toReplaceBackTuangouGoods.getGoods_serries_id();
						try {
							updateGoods(displayId, backId, dispalyPictureId,
									backPictureId, position);
							if (existBackGoodsActive(dispalyPictureId, dispalyGoodsId)
									&& existBackGoodsActive(backPictureId, backGoodsId)) {
								updateBackGoods(displayId, backId,
										dispalyPictureId, backPictureId,
										dispalyGoodsId, backGoodsId);
							}
						} catch (SQLException e) {
							BackupLog.logError("replace tuangou goods fail", e);
						}
					}else{
						setStatus(displayId);
						if(existBackGoodsActive(dispalyPictureId, dispalyGoodsId)){
							setStatusBack(displayId);
						}
					}
					
				}
				
			}
		//}
	}

	/**
	 * by zhw 处理秒杀商品
	 * 
	 * @param miaoshaGoodsList
	 * @param miaoshaBackGoodsList
	 */
	private void DealMiaoshaGoods(List<PictureManage> miaoshaGoodsList,
			List<PictureManage> miaoshaBackGoodsList) {
		// 监控备选秒杀商品
		if (miaoshaBackGoodsList.size() > 0) {
			for(int i=0;i<miaoshaBackGoodsList.size();i++){
				long backTime=DateUtils.getMillisecond(miaoshaBackGoodsList.get(i)
						.getEndTime()) - System.currentTimeMillis();
				backPictureId = miaoshaBackGoodsList.get(i).getPictureId();
				backId =  miaoshaBackGoodsList.get(i).getId();
				if (0 < backTime && backTime < halfHour && miaoshaBackGoodsList.get(i).getIs_send() == 0) {
					preWarning("", "", backPictureId,
							"", position);
					setIsSend(backId);
			}
			}
		}
			for (int i = 0; i < 5; i++) {
				long time = DateUtils.getMillisecond(miaoshaGoodsList.get(i)
						.getEndTime()) - System.currentTimeMillis();
				
				dispalyPictureId = miaoshaGoodsList.get(i).getPictureId();
				displayId = miaoshaGoodsList.get(i).getId();
				dispalyGoodsId = miaoshaGoodsList.get(i).getGoods_serries_id();
				
				String endTime = miaoshaGoodsList.get(i).getEndTime();
				
				// 预报警 条件：活动时间还剩1小时 预报警只发一次邮件 is_send记录 0:未发送过 1:发送过
				if (0 < time && time < halfHour && miaoshaGoodsList.get(i).getIs_send() == 0) {
					preWarning(displayId, backId, dispalyPictureId,
							backPictureId, position);
					setIsSend(displayId);
				}
				
				PictureManage toReplaceBackMiaoshaGoods=getOneValidBackActiveGoods(4);
				if ((!promotionMiaoshaGoodsValid(dispalyGoodsId, 4)
						|| !WithinTime(endTime)) ) {
					//
					if(toReplaceBackMiaoshaGoods!=null){
						backPictureId = toReplaceBackMiaoshaGoods.getPictureId();
						backId = toReplaceBackMiaoshaGoods.getId();
						backGoodsId=toReplaceBackMiaoshaGoods.getGoods_serries_id();
						try {
							updateGoods(displayId, backId, dispalyPictureId,
									backPictureId, position);
							if (existBackGoodsActive(dispalyPictureId, dispalyGoodsId)
									&& existBackGoodsActive(backPictureId, backGoodsId)) {
								updateBackGoods(displayId, backId,
										dispalyPictureId, backPictureId,
										dispalyGoodsId, backGoodsId);
							}
						} catch (SQLException e) {
							BackupLog.logError("replace miaosha goods fail", e);
						}
					}else{
						  setStatus(displayId);
						if(existBackGoodsActive(dispalyPictureId, dispalyGoodsId)){
						  setStatusBack(displayId);
						}
					}
					
				}
				
			}
		//}
	}

	
	/**
	 * by 首页秒杀活动替换条件，不考虑秒杀库存
	 * @param goodsId
	 * @param type
	 * @return
	 */
	private boolean promotionMiaoshaGoodsValid(int goodsId, int type) {
		String sql="select count(1) from promoting_brand_category_goods pbcg "
				+ " left join promotion_rule pr on pbcg.promotion_rule_id=pr.id "
				+ " left join goods gs on gs.id =pbcg.goods_id "
				+ " where gs.status=4 and gs.is_virtual=1 and gs.active=1 and pr.`type` =? and pbcg.`status` in(1,2,3) "
				+ " and pbcg.goods_series_id=? group by pr.id  order by pbcg.id desc limit 1";
		boolean flag = false;
		int intFlag = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, type);
				ps.setInt(2, goodsId);
				rs = ps.executeQuery();
				if (rs.next()) {
					intFlag = rs.getInt(1);
				}
				if (intFlag != 0) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeRs(rs);
			closePs(ps);
		}

		return flag;
	}

	/**
	 * by zhw  获取一个满足替换秒杀或团购商品的备选
	 * @param type 4秒杀  5团购
	 * @return
	 */
	private PictureManage getOneValidBackActiveGoods(int type) {
		PictureManage pictureManage = new PictureManage();
		String sql = "select * from picture_manage_online p where p.picture_id >'A205' and p.picture_id<'A211'";
		if(type==3){
			sql = "select * from picture_manage_online p where p.picture_id >'A215' and p.picture_id<'A221'";
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					pictureManage.setPictureId(rs.getString("picture_id"));
					pictureManage.setGoods_serries_id(rs
							.getInt("goods_serries_id"));
					pictureManage.setPosition(rs.getInt("position"));
					pictureManage.setId(rs.getString("id"));
					pictureManage.setStartTime(rs.getString("start_time"));
					pictureManage.setEndTime(rs.getString("end_time"));
					pictureManage.setIs_send(rs.getInt("is_send"));
					if (promotionGoodsValid(pictureManage.getGoods_serries_id(), type) && WithinTime(pictureManage.getEndTime())) {
						break;
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeRs(rs);
			closePs(ps);
		}
		if (promotionGoodsValid(pictureManage.getGoods_serries_id(), type) && WithinTime(pictureManage.getEndTime())) {
			return pictureManage;
		}else{
			return null;
		}
		
	}

	/**
	 * by zhw 获取首页秒杀商品、团购商品、新品
	 * 
	 * @return
	 */
	private List<PictureManage> getActiveData(String table) {
		List<PictureManage> activeGoodsList = new ArrayList<PictureManage>();
		String sql = "select * from "+table+" e where e.picture_id like 'A2%' order by e.picture_id ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					PictureManage pictureManage = new PictureManage();
					pictureManage.setPictureId(rs.getString("picture_id"));
					pictureManage.setGoods_serries_id(rs
							.getInt("goods_serries_id"));
					pictureManage.setPosition(rs.getInt("position"));
					pictureManage.setId(rs.getString("id"));
					pictureManage.setStartTime(rs.getString("start_time"));
					pictureManage.setEndTime(rs.getString("end_time"));
					pictureManage.setIs_send(rs.getInt("is_send"));
					activeGoodsList.add(pictureManage);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeRs(rs);
			closePs(ps);
		}

		return activeGoodsList;
	}

	/**
	 * by zhw 处理楼层商品
	 * 
	 * @throws SQLException
	 */
	private void DealFloorGoods() throws SQLException {
		List<PictureManage> floorGoodsListBack = new ArrayList<PictureManage>();// 存放备选商品
		List<PictureManage> floorGoodsListDisplay = new ArrayList<PictureManage>();// 存放显示商品
		// 获取单品
		List<PictureManage> floorGoodsList = getFloorsData("picture_manage_online");

		// 楼层商品begin
		int size = floorGoodsList.size();

		// 因为sql的排序，按奇偶拆分显示商品和备选商品，两个list索引一一对应
		for (int i = 0; i < size; i++) {
			if (i % 2 == 1) {
				floorGoodsListBack.add(floorGoodsList.get(i));
			} else {
				floorGoodsListDisplay.add(floorGoodsList.get(i));
			}
		}

		int n = floorGoodsListBack.size();
		for (int i = 0; i < n; i++) {
			dispalyGoodsId = floorGoodsListDisplay.get(i).getGoods_serries_id();
			backGoodsId = floorGoodsListBack.get(i).getGoods_serries_id();
			dispalyPictureId = floorGoodsListDisplay.get(i).getPictureId();
			backPictureId = floorGoodsListBack.get(i).getPictureId();
			displayId = floorGoodsListDisplay.get(i).getId();
			backId = floorGoodsListBack.get(i).getId();
			if (floorGoodsListDisplay.get(i).getPosition() != null) {
				position = floorGoodsListDisplay.get(i).getPosition();
			}
			
			// 预报警 预报警只发一次邮件 is_send记录 0:未发送过 1:发送过
			if (!preValid(dispalyGoodsId)
					&& floorGoodsListDisplay.get(i).getIs_send() == 0) {
				preWarning(displayId, backId, dispalyPictureId, backPictureId,
						position);
				setIsSend(displayId);
			}
			if (!preValid(backGoodsId)
					&& floorGoodsListBack.get(i).getIs_send() == 0) {
				preWarning("", "", backPictureId, "",
						position);
				setIsSend(backId);
			}

			// 如果显示商品失效，备选商品有效，自动替换
			if (!valid(dispalyGoodsId) && valid(backGoodsId)) {
				// 楼层商品替换,并清缓存发邮件通知
				updateGoods(displayId, backId, dispalyPictureId, backPictureId,
						position);
				// 运营后台商品替换,不做其他操作
				if (existBackGoods(dispalyPictureId, dispalyGoodsId, position)
						&& existBackGoods(backPictureId, backGoodsId, position)) {
					// 后台对应位置商品未被修改则替换
					updateBackGoods(displayId, backId, dispalyPictureId,
							backPictureId, dispalyGoodsId, backGoodsId);
				}
			}
			
		}

	}

	/**
	 * by zhw 替换首页后台商品
	 * 
	 * @param displayId
	 * @param backId
	 * @param dispalyPictureId
	 * @param backPictureId
	 * @param dispalyGoodsId
	 * @param backGoodsId
	 * @throws SQLException
	 */
	private void updateBackGoods(String displayId, String backId,
			String dispalyPictureId, String backPictureId, int dispalyGoodsId,
			int backGoodsId) throws SQLException {
		String sql = "UPDATE picture_manage SET picture_id=? WHERE id=? ";
		PreparedStatement ps = null;

		try {
			conn.setAutoCommit(false);
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, dispalyPictureId);
				ps.setString(2, backId);
				ps.execute();
				ps.setString(1, backPictureId);
				ps.setString(2, displayId);
				ps.execute();
				// 被替换商品置为无效 status=1;
				setStatusBack(displayId);
			}
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			closePs(ps);
		}

	}

	private void setStatusBack(String id) {
		String sql = "UPDATE picture_manage SET status=? WHERE id=? ";
		PreparedStatement ps = null;
		if (conn != null) {
			try {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, 1);
				ps.setString(2, id);
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closePs(ps);
			}

		}
	}

	/**
	 * by zhw 判断某一楼层某个位置的商品是否被修改
	 * 
	 * @param PictureId
	 * @param GoodsId
	 * @param position
	 * @return
	 */
	private boolean existBackGoods(String PictureId, int GoodsId, int position) {
		String sql = "select count(1) from picture_manage p where p.picture_id=? and p.goods_serries_id=? and p.position=?";
		boolean flag = false;
		int intFlag = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, PictureId);
				ps.setInt(2, GoodsId);
				ps.setInt(3, position);
				rs = ps.executeQuery();
				if (rs.next()) {
					intFlag = rs.getInt(1);
				}
				if (intFlag != 0) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeRs(rs);
			closePs(ps);
		}

		return flag;
	}

	/**
	 * by zhw 预报警邮件发送后置标识位is_send=1
	 * 
	 * @param displayId
	 */
	private void setIsSend(String displayId) {
		String sql = "UPDATE picture_manage_online SET is_send=? WHERE id=? ";
		PreparedStatement ps = null;
		if (conn != null) {
			try {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, 1);
				ps.setString(2, displayId);
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closePs(ps);
			}

		}
	}

	/**
	 * by zhw 给运营人员发送预报警邮件
	 * 
	 * @param displayId
	 * @param backId
	 * @param dispalyPictureId
	 * @param backPictureId
	 * @param position
	 */
	private void preWarning(String displayId, String backId,
			String dispalyPictureId, String backPictureId, int position) {
		MailCreator mailCreator = new MailCreator(conn);
		// 发送邮件通知运营人员被替换的商品
		if (dispalyPictureId.equals("A201") || dispalyPictureId.equals("A202")
				|| dispalyPictureId.equals("A203")
				|| dispalyPictureId.equals("A204")
				|| dispalyPictureId.equals("A205")
				|| dispalyPictureId.equals("A206")
				|| dispalyPictureId.equals("A207")
				|| dispalyPictureId.equals("A208")
				|| dispalyPictureId.equals("A209")
				|| dispalyPictureId.equals("A210")) {
			dataMap.put("location", "秒杀");
			teplateHtmlPre = "indexLimitPreWarningEmail.html";
		} else if (dispalyPictureId.equals("A221")
				|| dispalyPictureId.equals("A222")
				|| dispalyPictureId.equals("A223")
				|| dispalyPictureId.equals("A224")
				|| dispalyPictureId.equals("A225")
				|| dispalyPictureId.equals("A226")
				|| dispalyPictureId.equals("A227")
				|| dispalyPictureId.equals("A228")
				|| dispalyPictureId.equals("A229")
				|| dispalyPictureId.equals("A230")) {
			dataMap.put("location", "新品");
			teplateHtmlPre = "indexPreWarningEmail.html";
		} else if (dispalyPictureId.equals("A211")
				|| dispalyPictureId.equals("A212")
				|| dispalyPictureId.equals("A213")
				|| dispalyPictureId.equals("A214")
				|| dispalyPictureId.equals("A215")
				|| dispalyPictureId.equals("A216")
				|| dispalyPictureId.equals("A217")
				|| dispalyPictureId.equals("A218")
				|| dispalyPictureId.equals("A219")
				|| dispalyPictureId.equals("A220")) {
			dataMap.put("location", "环球购");
			teplateHtmlPre = "indexLimitPreWarningEmail.html";
		} /*else if (dispalyPictureId.equals("A119")
				|| dispalyPictureId.equals("A120")
				|| dispalyPictureId.equals("A121")) {
			dataMap.put("location", "限时抢购");
		}*/ else {
			dataMap.put("location", position + "楼");
			teplateHtmlPre = "indexPreWarningEmail.html";
		}
		dataMap.put("position", dispalyPictureId);
		BackupLog.logInfo("预报警商品位置  "+dataMap.get("location"));
		try {
			mailCreator.createEmailWithTemplate("商品替换预报警", to, cc, from,
					teplateHtmlPre, dataMap);

		} catch (SQLException e) {
			BackupLog.logError("create email fail", e);
		}

	}

	/**
	 * by zhw 判断是否达到预报警条件，下架、库存=1
	 * 
	 * @param dispalyGoodsId
	 * @return
	 */
	private boolean preValid(int dispalyGoodsId) {
		String sql = "select ifNull(sum(g.inventory),0) from goods g "
				+ "where g.series_id =? and g.active=1 and g.is_virtual=1 and g.status=4 ";
		boolean flag = true;
		int intFlag = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, dispalyGoodsId);
				rs = ps.executeQuery();
				if (rs.next()) {
					intFlag = rs.getInt(1);
				}
				if (intFlag == 1) {
					flag = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeRs(rs);
			closePs(ps);
		}

		return flag;
	}

	/**
	 * by zhw 处理限时抢购商品
	 * 
	 * @throws SQLException
	 */
	private void DealLimitGoods() throws SQLException {
		int oneHour = 3600000;
		// 获取限时抢购商品
		List<PictureManage> limitGoodsList = getLimitGoods();
		// 3个限时抢购商品begin
		List<PictureManage> limitGoodsListBack = new ArrayList<PictureManage>();// 存放有效的备选商品
		for (int i = 3; i < 6; i++) {
			backGoodsId = limitGoodsList.get(i).getGoods_serries_id();

			String endTime = limitGoodsList.get(i).getEndTime();

			if (valid(backGoodsId) && WithinTime(endTime)) {
				limitGoodsListBack.add(limitGoodsList.get(i));
			}
		}
		// 如果存在有效的备选商品，检验替换
		if (limitGoodsListBack.size() > 0) {
			for (int i = 0; i < 3; i++) {
				long time = DateUtils.getMillisecond(limitGoodsList.get(i)
						.getEndTime()) - System.currentTimeMillis();
				dispalyPictureId = limitGoodsList.get(i).getPictureId();
				backPictureId = limitGoodsListBack.get(0).getPictureId();
				displayId = limitGoodsList.get(i).getId();
				backId = limitGoodsListBack.get(0).getId();
				dispalyGoodsId = limitGoodsList.get(i).getGoods_serries_id();
				String endTime = limitGoodsList.get(i).getEndTime();
				if (!valid(dispalyGoodsId) || !WithinTime(endTime)) {
					updateGoods(displayId, backId, dispalyPictureId,
							backPictureId, position);
				}
				// 预报警 条件：活动时间还剩1小时 预报警只发一次邮件 is_send记录 0:未发送过 1:发送过
				if (0 < time && time < oneHour && limitGoodsList.get(i).getIs_send() == 0) {
					preWarning(displayId, backId, dispalyPictureId,
							backPictureId, position);
					setIsSend(displayId);
				}
			}
		}
	}

	/**
	 * by zhw 判断时间是否大于当前时间
	 */
	private boolean WithinTime(String endTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = formatter.format(new Date());
		if (DateUtils.strToDatehhmmss(endTime).after(
				DateUtils.strToDatehhmmss(currentTime))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * by zhw 获取限时抢购商品
	 */
	private List<PictureManage> getLimitGoods() {
		List<PictureManage> limitGoodsList = new ArrayList<PictureManage>();
		String sql = "select * from picture_manage_online e where e.picture_id >= 'A119' and e.picture_id like 'A1%' order by e.picture_id ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					PictureManage pictureManage = new PictureManage();
					pictureManage.setPictureId(rs.getString("picture_id"));
					pictureManage.setGoods_serries_id(rs
							.getInt("goods_serries_id"));
					pictureManage.setPosition(rs.getInt("position"));
					pictureManage.setId(rs.getString("id"));
					pictureManage.setStartTime(rs.getString("start_time"));
					pictureManage.setEndTime(rs.getString("end_time"));
					pictureManage.setIs_send(rs.getInt("is_send"));
					limitGoodsList.add(pictureManage);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeRs(rs);
			closePs(ps);
		}

		return limitGoodsList;
	}

	/**
	 * by zhw 判断团购商品是否失效
	 * 
	 * @param goodsId
	 * @param type
	 * @return
	 */
	private boolean promotionGoodsValid(int goodsId, int type) {
		String sql ="select sum(pbcg.inventory) from promoting_brand_category_goods pbcg left join promotion_rule pr on pbcg.promotion_rule_id=pr.id "
				+ " left join goods_series gs on gs.id =pbcg.goods_series_id "
				+ " where gs.status=4 and gs.is_virtual=1 and pr.`type` =? and pbcg.`status` in(1,2,3) "
				+ " and pbcg.goods_series_id=? and pbcg.goods_id!=0 group by pbcg.promotion_rule_id order by pbcg.id desc limit 1";
		/*String sql = "select pbcg.inventory from promoting_brand_category_goods pbcg left join promotion_rule pr on pbcg.promotion_rule_id=pr.id"
				+ " left join goods_series gs on gs.id =pbcg.goods_series_id"
				+ " where gs.status=4 and gs.is_virtual=1 and pr.`type` =? and pbcg.goods_series_id=? and pbcg.goods_id=0 order by pbcg.id desc limit 1";*/
		boolean flag = false;
		int intFlag = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, type);
				ps.setInt(2, goodsId);
				rs = ps.executeQuery();
				if (rs.next()) {
					intFlag = rs.getInt(1);
				}
				if (intFlag != 0) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeRs(rs);
			closePs(ps);
		}

		return flag;
	}

	/**
	 * by zhw 查询商品是否有效
	 */
	private boolean valid(int goodsId) {
		String sql = "select count(distinct g.id) from goods g left join goods_series gs on g.series_id=gs.id "
				+ "where g.inventory>0 and g.active=1 and g.is_virtual=1 and g.status=4 and gs.status=4 and gs.is_virtual=1 and gs.id=?";
		boolean flag = false;
		int intFlag = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, goodsId);
				rs = ps.executeQuery();
				if (rs.next()) {
					intFlag = rs.getInt(1);
				}
				if (intFlag != 0) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeRs(rs);
			closePs(ps);
		}

		return flag;
	}

	/**
	 * by zhw 备选商品替换无效商品,清缓存,并发送邮件通知运营人员
	 * 
	 * @param backPictureId
	 * @param dispalyPictureId
	 * @param position
	 * @throws SQLException
	 */
	private void updateGoods(String dispalyId, String backId,
			String dispalyPictureId, String backPictureId, int position)
			throws SQLException {
		String sql = "UPDATE picture_manage_online SET picture_id=? WHERE id=? ";
		PreparedStatement ps = null;

		try {
			conn.setAutoCommit(false);
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, dispalyPictureId);
				ps.setString(2, backId);
				ps.execute();
				MailCreator mailCreator = new MailCreator(conn);
				// 发送邮件通知运营人员被替换的商品
				if (dispalyPictureId.equals("A201")
						|| dispalyPictureId.equals("A202")
						|| dispalyPictureId.equals("A203")
						|| dispalyPictureId.equals("A204")
						|| dispalyPictureId.equals("A205")) {
					dataMap.put("location", "秒杀");
					teplateHtml = "indexLimitWarningEmail.html";
				} else if (dispalyPictureId.equals("A221")
						|| dispalyPictureId.equals("A222")
						|| dispalyPictureId.equals("A223")
						|| dispalyPictureId.equals("A224")
						|| dispalyPictureId.equals("A225")) {
					dataMap.put("location", "新品");
					teplateHtml = "indexWarningEmail.html";
				} else if (dispalyPictureId.equals("A211")
						|| dispalyPictureId.equals("A212")
						|| dispalyPictureId.equals("A213")
						|| dispalyPictureId.equals("A214")
						|| dispalyPictureId.equals("A215")) {
					dataMap.put("location", "环球购");
					teplateHtml = "indexLimitWarningEmail.html";
				} /*else if (dispalyPictureId.equals("A119")
						|| dispalyPictureId.equals("A120")
						|| dispalyPictureId.equals("A121")) {
					dataMap.put("location", "限时抢购");
				} */else {
					dataMap.put("location", position + "楼");
					teplateHtml = "indexWarningEmail.html";
				}
				dataMap.put("position", dispalyPictureId);
				// mailCreator.createEmail("首页商品替换报警", "zhonghuawei@izptec.com",
				// "Noreply-service@haixuan.com",
				// dispalyPictureId+"商品已被系统自动替换");
				mailCreator.createEmailWithTemplate("商品替换报警", to, cc, from,
						teplateHtml, dataMap);
				ps.setString(1, backPictureId);
				ps.setString(2, dispalyId);
				ps.execute();
				// 被替换商品置为无效 status=1;
				setStatus(dispalyId);
				/*HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(Config.getConf()
						.getHomePageHost().getHost()
						+ "/clearcacheindex.html");*/
				System.out.print("before "+new Date());
				BackupLog.logInfo("before "+new Date());
				String result=HttpClients.sendGet(Config.getConf().getHomePageHost().getHost()+"/clearcacheindex.html", "");
				BackupLog.logInfo("after "+new Date());
				System.out.print("after "+new Date());
			}
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			closePs(ps);
		}
	}

	/**
	 * by zhw 更改商品状态位置，特别提醒：0有效 1无效
	 * 
	 * @param dispalyId
	 */
	private void setStatus(String dispalyId) {
		String sql = "UPDATE picture_manage_online SET status=? WHERE id=? ";
		PreparedStatement ps = null;
		if (conn != null) {
			try {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, 1);
				ps.setString(2, dispalyId);
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closePs(ps);
			}

		}
	}

	/**
	 * by zhw 获取楼层商品，包括显示商品和备选商品
	 */
	public List<PictureManage> getFloorsData(String table) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PictureManage> listAllFloor = new ArrayList<PictureManage>();

		String forList = "select * from "+table+" e where e.picture_id like 'F%' and e.img_path is not null and e.goods_serries_id is not null order by e.position,e.picture_id";
		// 按位置和picture_id排序，商品成对出现，结果集index为偶数的商品为前一个商品备选
		try {
			if (conn != null) {
				ps = conn.prepareStatement(forList);
				rs = ps.executeQuery();
				while (rs.next()) {
					PictureManage pictureManage = new PictureManage();
					pictureManage.setPictureId(rs.getString("picture_id"));
					pictureManage.setGoods_serries_id(rs
							.getInt("goods_serries_id"));
					pictureManage.setPosition(rs.getInt("position"));
					pictureManage.setId(rs.getString("id"));
					pictureManage.setIs_send(rs.getInt("is_send"));
					listAllFloor.add(pictureManage);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeRs(rs);
			closePs(ps);
		}
		return listAllFloor;

	}

	public static void main(String[] args) {
		IndexGoodsUpdateJob indexGoodsUpdateJob = new IndexGoodsUpdateJob();
		indexGoodsUpdateJob.run();
		System.exit(0);

	}

}
