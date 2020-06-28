package com.yqh.falcon.service.impl;

import com.yqh.falcon.mapper.TagMapper;
import com.yqh.falcon.model.Tag;
import com.yqh.falcon.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public int addTag(Tag tag) {
        return tagMapper.insertTag(tag);
    }

    @Override
    public int updateTag(Tag tag) {
        return tagMapper.updateTag(tag);
    }

    @Override
    public List<Tag> getAllTag() {
        return tagMapper.findAllTag();
    }
}
