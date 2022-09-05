package com.finalpi.qq_forward.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by wzw on 2022/9/5
 **/
@Data
@TableName("forward")
public class Forward {
    @TableId(type = IdType.AUTO) //采用数据库自增
    private long id;
    private String hash;
    private String json;
}
