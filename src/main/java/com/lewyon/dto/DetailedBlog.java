package com.lewyon.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lewyon.pojo.Tag;


/**
 * 修改博客传到修改页面的类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedBlog {

    private Long id;
    private String avatar;
    private String nickname;
    private boolean published;
    private String flag;
    private String title;
    private String content;
    private Long typeId;
    private String tagIds;
    private String firstPicture;
    private String description;
    private boolean recommend;
    private boolean shareStatement;
    private boolean appreciation;
    private boolean commentabled;
    private Date updateTime;
    private Integer views;
    
    private List<Tag> tags = new ArrayList<>();

}
