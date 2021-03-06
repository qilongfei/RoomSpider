package cn.lee.wx.util;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jason on 17-10-16.
 */
public class SignUtils {

    private static Logger logger = LoggerFactory.getLogger(SignUtils.class);

    /**
     * 验证签名
     *
     * @param token     微信服务器token，在env.properties文件中配置的和在开发者中心配置的必须一致
     * @param signature 微信服务器传过来sha1加密的证书签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return
     */
    public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {
        String[] arr = new String[]{token, timestamp, nonce};
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(arr);
        String strin = StringUtils.join(arr, "");
        logger.info(strin);
        // 将三个参数字符串拼接成一个字符串进行sha1加密
        String tmpStr = DigestUtils.sha1Hex(strin);
        logger.info("hashcod:{} , signature:{}", tmpStr, signature);
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return StringUtils.equalsIgnoreCase(tmpStr, signature);
    }
}
