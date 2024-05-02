package com.thread;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.YonghuEntity;
import com.entity.view.YonghuView;
import com.service.YonghuService;
import com.utils.PageUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 线程执行方法（做一些项目启动后 一直要执行的操作，比如根据时间自动更改订单状态，比如订单签收30天自动收货功能，比如根据时间来更改状态）
 * 本线程用于更新用户状态。如果会员截止日期小于当前日期 就把状态改为非会员
 */
public class MyThreadMethod extends Thread  {
    private YonghuService yonghuService;

    public YonghuService getYonghuService() {
        return yonghuService;
    }

    public void setYonghuService(YonghuService yonghuService) {
        this.yonghuService = yonghuService;
    }

    public void run() {
        while (!this.isInterrupted()) {// 线程未中断执行循环
            try {
                HashMap<String, Object> params = new HashMap<>();
                params.put("yonghuTimeEnd",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                params.put("yonghuTypes",2);
                params.put("page","1");
                params.put("limit","1000");
                params.put("orderBy","id");
                PageUtils pageUtils = yonghuService.queryPage(params);
                List<YonghuView> list =(List<YonghuView>)pageUtils.getList();
                List<YonghuEntity> yonghuList = new ArrayList<>();
                for(YonghuView l:list){
                    YonghuEntity yonghuEntity = new YonghuEntity();
                    yonghuEntity.setId(l.getId());
                    yonghuEntity.setYonghuTypes(1);
                    yonghuList.add(yonghuEntity);
                }
                if(yonghuList.size()>0){
                    yonghuService.updateBatchById(yonghuList);
                }
                System.out.println("----zhixingxiancheng-------gengxinzhuangtai-----");
                Thread.sleep( 1000 * 60); //每隔一分钟执行一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
