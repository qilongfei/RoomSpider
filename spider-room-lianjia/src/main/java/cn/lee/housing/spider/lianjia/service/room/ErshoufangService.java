package cn.lee.housing.spider.lianjia.service.room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lee.housing.spider.lianjia.model.room.Baojia;
import cn.lee.housing.spider.lianjia.model.room.Ershoufang;
import cn.lee.housing.spider.lianjia.repository.room.BaojiaDao;
import cn.lee.housing.spider.lianjia.repository.room.ErshoufangDao;
import cn.lee.housing.spider.lianjia.spider.MySpider;
import cn.lee.housing.spider.lianjia.spider.pipeline.ErshoufangPipeline;
import cn.lee.housing.spider.lianjia.spider.processor.ChengjiaoProcessorFactory;
import cn.lee.housing.spider.lianjia.spider.processor.ErshoufangProcessor;
import cn.lee.housing.spider.lianjia.spider.processor.ErshoufangProcessorFactory;
import cn.lee.housing.spider.lianjia.spider.proxy.XdailiProxyProvider;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.PriorityScheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by jason on 17-9-29.
 */
@Service
public class ErshoufangService {

    @Autowired
    private ErshoufangDao dao;
    @Autowired
    private BaojiaDao baojiaoDao;
    @Autowired
    private ErshoufangPipeline pipeline;
    @Autowired
    private XdailiProxyProvider proxyProvider;
    @Autowired
    private ErshoufangProcessorFactory factory;

    /**
     * 爬取
     *
     * @param area
     * @return
     */
    public Map doSpider(String area) {
        Map result = new HashMap();
        boolean isSuccess = true;
        try {
            HttpClientDownloader downloader = new HttpClientDownloader();
            downloader.setProxyProvider(proxyProvider);
            Spider spider = MySpider.create(factory.getObject(area))
                    .setScheduler(new PriorityScheduler())
                    .addPipeline(pipeline)
                    .addPipeline(new ConsolePipeline())
                    .addUrl(ErshoufangProcessor.START_URL + ChengjiaoProcessorFactory.convertName(area));
            spider.setDownloader(downloader);
            spider.thread(10).start();//启动爬虫
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
            result.put("desc", e.getMessage());
        }

        result.put("success", isSuccess);
        return result;
    }

    public boolean isRecrawl(String roomId) {
        Ershoufang cj = dao.findByRoomId(roomId);
        return cj == null || cj.isReCrawl();
    }


    public Baojia saveBaojia(Baojia baojia) {
        Baojia last = baojiaoDao.findFirstByRoomIdOrderByCrawTimeDesc(baojia.getRoomId());
        if (last != null && !StringUtils.equalsIgnoreCase(last.getPrice(), baojia.getPrice())) {
            Ershoufang ershoufang = dao.findByRoomId(baojia.getRoomId());
            ershoufang.setTotalPrice(baojia.getPrice());
            saveErshoufang(ershoufang);
            baojiaoDao.save(baojia);
        }

        return baojia;
    }

    public Ershoufang saveErshoufang(Ershoufang ershoufang) {
        dao.save(ershoufang);
        List<Baojia> prices = baojiaoDao.findByRoomId(ershoufang.getRoomId());
        if (prices == null || prices.size() == 0) {
            Baojia bj = new Baojia(ershoufang.getRoomId());
            bj.setPrice(ershoufang.getTotalPrice());
            bj.setCrawTime(ershoufang.getCrawTime());
            baojiaoDao.save(bj);
        }
        return ershoufang;
    }
}
