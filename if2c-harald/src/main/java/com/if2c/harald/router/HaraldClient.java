package com.if2c.harald.router;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.if2c.common.bean.SaleAfterModel;
import com.if2c.harald.db.Config;
import com.if2c.router.AdapterRouterClient;
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

@Component
public class HaraldClient implements MessageHandler {

    private static Logger log=LoggerFactory.getLogger(HaraldClient.class);
    
    RouterClient rc=null;
    
    private HaraldClient(){
    }
    public static HaraldClient getInstance(){
        return HaraldClientBuilder.instance;
    }

    private static class HaraldClientBuilder {
        private static HaraldClient instance=new HaraldClient();
    }
    
    public void init() {
        log.info("Project [Harald] will connect to Router......");
        rc = new AdapterRouterClient(JIDConstants.HARALDJID, JIDConstants.ROUTERJID, this);
        if (rc == null) {
            log.error("", new Exception("There's an exception when registering HARALDJID..."));
            return;
        }
        try {
            String routerAddr=Config.getConf().getRouterAddr();
            rc.init(routerAddr, 10080, 2, new AuthEntity("if2c",1),6000);
            log.info("Project [Harald] has connected to Router...");
        } catch (NumberFormatException | RouterException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    public void saleAfterOpt(SaleAfterModel model){
        try {
            log.info("Method of SaleAfterOpt will send "+model+" to Project [Zhenghe]......");
            JsonRoutingPacket response=this.rc.syncSend(JsonRoutingPacket.newRequest(JIDConstants.ZHENGHEJID, "saleAfterOpt", model));
            SaleAfterModel result=JsonUtil.json2Bean(response.getEntity(), SaleAfterModel.class);
            log.info("Method of SaleAfterOpt received an resultFlag=["+result.getResultFlag().name()+"] of saleAfterId=["+model.getSaleAfterInfoId()+"] ......");
        } catch (RouterException | RemoteCallException e) {
            log.error("", new Exception("There's an exception ["+e+"] when invoking saleAfterOpt......"));
        }
    }
    
    @Override
    public void handleIncoming(JsonRoutingPacket arg0, JsonPacketResponse arg1) throws ComponentException {

    }

}
