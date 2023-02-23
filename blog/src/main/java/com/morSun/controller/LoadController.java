package com.morSun.controller;

import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.constants.SystemConstants;
import com.morSun.exception.SystemException;
import com.morSun.service.LoadService;
import com.morSun.service.impl.tencent.TencentCDNServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

@RestController
public class LoadController {

    @Autowired
    private LoadService loadService;

    /**
     * 上传用户头像
     * @param avatar
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadAvatar(@RequestParam("img") MultipartFile avatar, HttpServletRequest request, HttpServletResponse response)
    {
        if (Objects.isNull(avatar))
            throw new SystemException(ErrorCode.NULL_ERROR);
        //上传头像
        return loadService.upLoadImg(avatar, request, response);
    }
}
