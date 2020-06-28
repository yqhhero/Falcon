package com.yqh.falcon.mapper;

import com.yqh.falcon.model.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper {

    int insertTag(Tag tag);
    int updateTag(Tag tag);

    List<Tag> findAllTag();
}
