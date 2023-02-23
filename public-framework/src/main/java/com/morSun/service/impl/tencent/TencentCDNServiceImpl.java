package com.morSun.service.impl.tencent;

import com.morSun.constants.SystemConstants;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.AppendObjectRequest;
import com.qcloud.cos.model.AppendObjectResult;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.region.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: tlw
 * @date: 2021年10月12日
 * @Description: COS对象存储
 */
@Service
public class TencentCDNServiceImpl {
    // 将此对象打印出来的日志信息绑定类
    private static final Logger LOG = LoggerFactory.getLogger(TencentCDNServiceImpl.class);

    @Value("${cos.secretId}")
    private String accessKey;

    @Value("${cos.secretKey}")
    private String secretKey;

    @Value("${cos.regionName}")
    private String regionName;

    @Value("${cos.bucketName}")
    private String bucketName;

    @Value("${cos.fileName}")
    private String keyName;

    // 在存储桶后有显示
    private final String  APPID="1256524210";

    /**
     * 上次图片对接腾讯CDN
     *
     * @param fileDataFileName
     * @param request
     * @param response
     * @return
     */
    public Map<String, Object> ContentCOS(MultipartFile fileDataFileName, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(regionName));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);

        // 获取文件类型,jpg或是png
        String name = fileDataFileName.getOriginalFilename();
        String fileType = name.substring(name.lastIndexOf(".") + 1);

        // bucket名需包含appid
        String key = keyName + "/" + UUID.randomUUID().toString() + "." + fileType;
        String url = null;
        try {
            // 处理文件路径
            String filePath = request.getSession().getServletContext().getRealPath("/") + fileDataFileName.getOriginalFilename();
            fileDataFileName.transferTo(new File(filePath));
            // 处理到本地服务器
            File localFile = new File(filePath);

            // 报错请求对象
            AppendObjectRequest appendObjectRequest = new AppendObjectRequest(bucketName, key, localFile);
            // 设置节点,上传到腾讯云
            appendObjectRequest.setPosition(0L);
            AppendObjectResult appendObjectResult = cosclient.appendObject(appendObjectRequest);
            // 文件大小
            long nextAppendPosition = appendObjectResult.getNextAppendPosition();
            LOG.info("文件大小：{}", nextAppendPosition);

            // 获取返回对象
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            COSObject cosObject = cosclient.getObject(getObjectRequest);
            url = cosObject.getObjectContent().getHttpRequest().getURI().toString();
            LOG.info("COS对象地址：{}", url);
            map.put(SystemConstants.PIC_AVATAR_PATH, url);
            map.put("success", true);
            map.put("msg", "上传成功");
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cosclient.shutdown();
        }
        return map;
    }
}
