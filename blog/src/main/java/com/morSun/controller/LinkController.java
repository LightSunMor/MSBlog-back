package com.morSun.controller;

import com.morSun.common.BaseResponse;
import com.morSun.service.MsLinkService;
import com.morSun.vo.LinkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/link")
public class LinkController {
    @Autowired
    private MsLinkService linkService;

    /**
     *  获取所有的友链，一般不多就不使用分页
     * @return
     */
    @GetMapping("/getAllLink")
    public BaseResponse<List<LinkVo>> getAllLink()
    {
        BaseResponse<List<LinkVo>> response = linkService.getAllLinkVo();
        return response;
    }
}
