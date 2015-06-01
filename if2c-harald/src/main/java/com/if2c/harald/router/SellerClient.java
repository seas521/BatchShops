package com.if2c.harald.router;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.if2c.common.bean.TransportModel;
import com.if2c.router.JIDConstants;
import com.if2c.router.framework.MessageHandler;
import com.if2c.router.framework.basic.JsonPacketResponse;
import com.if2c.router.framework.client.RouterClient;
import com.if2c.router.framework.constants.RouterConstants;
import com.if2c.router.framework.exception.RemoteCallException;
import com.if2c.router.framework.exception.RouterException;
import com.if2c.router.framework.protocol.JsonRoutingPacket;
import com.if2c.router.util.JsonUtil;

/**
 * 调用顺序:
 * 1.先运行Router项目(Web项目),启用服务端
 * 2.运行SellerClient，等待接收运营模拟客户端数据
 * 3.运行if2c-harald的com.if2c.harald.router.CustomerClient，向SellerClient发送数据
 * 
 * 模拟运营客户端，向商家发送数据，同时接收商家请求数据
 * 
 * @author shengqiang
 *
 * 2014-7-31
 */
public class SellerClient implements MessageHandler {

	public static SellerClient getInstance() {
		return INSTANCE;
	}
	RouterClient rc;
	public void init() throws RouterException, IOException {
	    System.out.println("___________________________________Seller准备连接...");
		rc = new RouterClient(JIDConstants.SELLERJID,
				RouterConstants.ROUTERJID, this);
		String addressPropertie = "223.202.100.230";
		int portPropertie = 10080;
		int maxConnPropertie = 2;
		Long maxDealtimePropertie = 60*1000L;
		rc.init(addressPropertie, portPropertie,
				maxConnPropertie, JIDConstants.loginToken,
				maxDealtimePropertie);
		System.out.println("___________________________________Seller连接成功...");
	}

	@Override
	public void handleIncoming(JsonRoutingPacket request,
			JsonPacketResponse response) {
	    System.out.println("商家接收到运营请求类型:"+request.getAction());
	    if(StringUtils.equals(request.getAction(), "action")){
	        TransportModel model = JsonUtil.json2Bean(
	                request.getEntity(), TransportModel.class);
	        System.out.println("商家接收到运营请求的数据:"+model.getAction());
	        model.setResult("OK");
	        try {
                response.writePacket(request.asResult(model));
            } catch (IOException | RouterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
	    }
	}

	public SellerClient() {
		
	}

	private static SellerClient INSTANCE = new SellerClient();

	public void stop() {
		rc.shutdown();
	}
	
	public static void main(String[] args) throws RouterException, RemoteCallException, IOException {
	    new SellerClient().init();
    }
	
}
