package com.morSun.service;

import com.morSun.common.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoadService {
    BaseResponse<String> upLoadImg(MultipartFile avatar, HttpServletRequest request, HttpServletResponse response);

}
