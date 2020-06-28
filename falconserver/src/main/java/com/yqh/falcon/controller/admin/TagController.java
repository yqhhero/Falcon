package com.yqh.falcon.controller.admin;

import com.yqh.falcon.model.Tag;
import com.yqh.falcon.model.dto.ResponseDto;
import com.yqh.falcon.model.enums.ResultCode;
import com.yqh.falcon.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 增加标签
     *
     * @param tag
     * @return
     */
    @PostMapping("/tag")
    public ResponseDto addTag(@RequestBody Tag tag) {
        int resultNum = tagService.addTag(tag);
        if (resultNum == 1) {
            return ResponseDto.success(ResultCode.CREATED);
        }
        return ResponseDto.fail(ResultCode.FAIL);
    }

    /**
     * 更新标签
     *
     * @param tag
     * @return
     */
    @PutMapping("/tag")
    public ResponseDto updateTag(@RequestBody Tag tag) {
        int resultNum = tagService.updateTag(tag);
        if (resultNum == 1) {
            return ResponseDto.success(ResultCode.OK);
        }
        return ResponseDto.fail(ResultCode.FAIL);
    }

    /**
     * 得到所有标签
     *
     * @return
     */
    @GetMapping("/tags")
    public ResponseDto getAllTag() {
        List<Tag> tags = tagService.getAllTag();
        return ResponseDto.success(ResultCode.OK, tags);
    }

}
