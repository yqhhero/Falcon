package com.yqh.falcon.service;

import com.yqh.falcon.model.Tag;

import java.util.List;

public interface TagService {

    int addTag(Tag tag);
    int updateTag(Tag tag);

    List<Tag> getAllTag();
}
