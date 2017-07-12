package cn.lee.housing.spider.lianjia.service.proxy;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * Created by jason on 17-7-12.
 */

/**
 * 启动前从代理爬取100个地址做代理池
 */
@Service
public class ProxyProcessor implements PageProcessor, InitializingBean {

    private Site site = Site.me().setRetryTimes(2).setSleepTime(1000);

    private Spider spider = Spider.create(this).setSpawnUrl(false).addUrl("http://www.xicidaili.com/nn");

    private List<Proxy> proxyList = Lists.newArrayList();

    @Override
    public void process(Page page) {
        List<Selectable> nodes = page.getHtml().xpath("//table[@id=ip_list]//tr").nodes();
        for (int i = 1; i < nodes.size(); i++) {
            Selectable node = nodes.get(i);
            String host = node.xpath("//td[2]/text()").get();
            String port = node.xpath("//td[3]/text()").get();
            proxyList.add(new Proxy(host, Integer.parseInt(port)));
        }
        page.putField("proxy", proxyList);
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        spider.start();
    }

    public void refreshProxy() {
        if(spider.getThreadAlive() > 1){

        }else{
            spider.start();
        }
    }
}
