package cn.lee.housing.spider.lianjia.model.room;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.lee.housing.spider.lianjia.model.IdEntity;

/**
 * 二手房成交价格
 * Created by jason on 17-7-21.
 */
@Entity
@Table(name = "lianjia_chengjiao")
public class Chengjiao extends IdEntity {

    private String roomId;

    private String totalPrice;

    private String avgPrice;

    private String listPrice;

    private String cycle;

    private String times;

    private String inspectTimes;

    private String attentionTimes;

    private String viewTimes;


}
