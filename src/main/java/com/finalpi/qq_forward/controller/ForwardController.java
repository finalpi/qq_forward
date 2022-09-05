package com.finalpi.qq_forward.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.finalpi.qq_forward.bean.Forward;
import com.finalpi.qq_forward.bean.ForwardParam;
import com.finalpi.qq_forward.bean.GetMsgParam;
import com.finalpi.qq_forward.mapper.ForwardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by wzw on 2022/9/5
 **/
@RestController
@RequestMapping("/api")
public class ForwardController {
    @Autowired
    private ForwardMapper forwardMapper;

    @GetMapping("/")
    public String getMsg(GetMsgParam getMsgParam){
        Forward forward = forwardMapper.selectOne(new QueryWrapper<Forward>().lambda().eq(Forward::getHash, getMsgParam.getHash()));
        if (forward == null){
            throw new RuntimeException("找不到对应的消息");
        }
        return forward.getJson();
    }

    @PostMapping("/add")
    public String saveMsg(@RequestBody Map<String,Object> forwardParam){
        if (!"finalpi".equals(forwardParam.get("auth").toString())){
            throw new RuntimeException("key错误");
        }
        Forward forward = new Forward();
        forward.setHash(forwardParam.get("key").toString());
        forward.setJson(JSON.toJSONString(forwardParam.get("data")));
        forwardMapper.insert(forward);
        return "ok";
    }

}
