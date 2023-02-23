package com.morSun.service.impl;

import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.constants.SystemConstants;
import com.morSun.exception.SystemException;
import com.morSun.service.LoadService;
import com.morSun.service.impl.tencent.TencentCDNServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

@Service
public class LoadServiceImpl implements LoadService {
    @Autowired
    private TencentCDNServiceImpl tencentCDNService;

    /**
     * 上传图片到腾讯云cos
     * @param avatar
     * @param request
     * @param response
     * @return
     */
    @Override
    public BaseResponse<String> upLoadImg(MultipartFile avatar, HttpServletRequest request, HttpServletResponse response) {
        //1.对文件的类型和大小进行判断
        String regex="image/.{3,}";
        boolean matches = avatar.getContentType().matches(regex);
        if (!matches)
            throw new SystemException(ErrorCode.NOT_PIC_TYPE);
        if (avatar.getSize()>4*1024*1024)
            throw new SystemException(ErrorCode.OVER_PIC_SIZE);
        //2.上传图片
        Map<String, Object> contentCOS = tencentCDNService.ContentCOS(avatar, request, response);
        //3.返回值为空
        if (Objects.isNull(contentCOS))
            throw new SystemException(ErrorCode.NO_RESPONSE);

        return ResultUtil.success(String.valueOf(contentCOS.get(SystemConstants.PIC_AVATAR_PATH)));
    }
}
