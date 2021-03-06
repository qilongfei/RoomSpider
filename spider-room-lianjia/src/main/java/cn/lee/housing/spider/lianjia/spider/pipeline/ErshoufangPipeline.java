package cn.lee.housing.spider.lianjia.spider.pipeline;

import cn.lee.housing.spider.lianjia.model.room.Baojia;
import cn.lee.housing.spider.lianjia.model.room.Ershoufang;
import cn.lee.housing.spider.lianjia.service.room.ErshoufangService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jason on 17/7/14.
 */
@Service
public class ErshoufangPipeline implements Pipeline {

    @Autowired
    private ErshoufangService service;


    @Override
    public void process(ResultItems resultItems, Task task) {
        Ershoufang entity = resultItems.get("ershoufang");
        if (entity != null) {
            service.saveErshoufang(entity);
        }
        Baojia bj = resultItems.get("baojia");
        if (bj != null) {
            service.saveBaojia(bj);
        }
    }
}
