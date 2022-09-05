package com.finalpi.qq_forward.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.finalpi.qq_forward.bean.Forward;
import com.finalpi.qq_forward.bean.ForwardParam;
import com.finalpi.qq_forward.bean.GetMsgParam;
import com.finalpi.qq_forward.mapper.ForwardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * Created by wzw on 2022/9/5
 **/
@RequestMapping("/api")
@Controller
@CrossOrigin
public class ForwardController {
    @Autowired
    private ForwardMapper forwardMapper;

    @GetMapping("/")
    public ModelAndView getMsg(GetMsgParam getMsgParam){
        Forward forward = forwardMapper.selectOne(new QueryWrapper<Forward>().lambda().eq(Forward::getHash, getMsgParam.getHash()));
        if (forward == null){
            throw new RuntimeException("找不到对应的消息");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("forward",JSON.parseArray(forward.getJson()));
        modelAndView.setViewName("msg_result");
        return modelAndView;
    }

    @GetMapping("/getByHash")
    @ResponseBody
    public String getByHash(GetMsgParam getMsgParam){
        Forward forward = forwardMapper.selectOne(new QueryWrapper<Forward>().lambda().eq(Forward::getHash, getMsgParam.getHash()));
        if (forward == null){
            throw new RuntimeException("找不到对应的消息");
        }
        return forward.getJson();
    }

    @PostMapping("/add")
    @ResponseBody
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

    @GetMapping( "/forward")
    @ResponseBody
    public ResponseEntity<Resource> forward(@RequestParam Map<String,String> param){
        String url = param.get("url");
        System.out.println("Start: ------------------------->");
        Resource resource = null;
        InputStream inputStream;
        try {

            inputStream = new URL(url).openStream();
            resource = new ByteArrayResource(toByteArray(inputStream));
        } catch (Exception e) {

            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, " filename=\"" + getFileName(url) + "\"")
                .body(resource);
    }

    private byte[] toByteArray(InputStream in){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {

            byte[] bytes = new byte[1024];
            int n;
            while ((n = in.read(bytes)) != -1){

                byteArrayOutputStream.write(bytes, 0, n);
            }
            System.out.println(byteArrayOutputStream.size());
        } catch (Exception e) {

            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private String getFileName(String url){

        String[] strs = url.split("/");
        for (String str: strs){
            // 这里我仅处理png格式的，根据需求修改即可
            if (str.toLowerCase().endsWith(".png")){
                return str;
            }
        }
        return null;
    }
}
