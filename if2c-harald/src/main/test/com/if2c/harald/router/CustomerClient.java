package com.if2c.harald.router;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.if2c.common.bean.TransportModel;
import com.if2c.router.JIDConstants;
import com.if2c.router.framework.MessageHandler;
import com.if2c.router.framework.basic.JsonPacketResponse;
import com.if2c.router.framework.client.RouterClient;
import com.if2c.router.framework.exception.ComponentException;
import com.if2c.router.framework.exception.RemoteCallException;
import com.if2c.router.framework.exception.RouterException;
import com.if2c.router.framework.protocol.AuthEntity;
import com.if2c.router.framework.protocol.JsonRoutingPacket;
import com.if2c.router.util.JsonUtil;

/**
 * 调用顺序:
 * 1.先运行Router项目(Web项目),启用服务端
 * 2.运行SellerClient，等待接收运营模拟客户端数据
 * 3.运行if2c-harald的com.if2c.harald.router.CustomerClient，向SellerClient发送数据
 * 
 * 模拟运营客户端，向商家发送数据，同时接收商家请求数据
 * @author shengqiang
 *
 * 2014-7-30
 */
public class CustomerClient implements MessageHandler{

    /**s
     * @param args
     */
    public static void main(String[] args) {
        final CustomerClient cc=new CustomerClient();
        new Thread(){
            @Override
            public void run(){
                int i=0;
                while(true){
                    TransportModel model=new TransportModel();
                    model.setAction("盛强_"+i);
                    cc.send(model, "action");
                    i++;
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    RouterClient rc;
    public CustomerClient(){
        rc=new RouterClient(JIDConstants.CUSTOMERJID,JIDConstants.ROUTERJID,this);
        try {
            rc.init("10.0.246.106", 10080, 2, new AuthEntity("if2c"), 60*1000L);
        } catch (RouterException | IOException e) {
            e.printStackTrace();
        }
    }
    public void send(TransportModel model,String action){
        JsonRoutingPacket request = JsonRoutingPacket
                .newRequest(JIDConstants.SELLERJID, action, model);
        System.out.println("运营请求商家数据:"+request);
        JsonRoutingPacket response = null;
        try {
            response = this.rc.syncSend(request);
            model=JsonUtil.json2Bean(response.getEntity(), TransportModel.class);
            System.out.println("运营获取商家返回数据:" + model.getResult());
        } catch (RouterException | RemoteCallException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void handleIncoming(JsonRoutingPacket request, JsonPacketResponse response) throws ComponentException {
        
    }
    


}
