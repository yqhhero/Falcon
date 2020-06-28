package com.yqh.falcon.model.es;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "article",type = "doc",
        useServerConfiguration = true,createIndex = false)
public class EsArticle {

    @Id
    private Long id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String content;
    @Field(type = FieldType.Integer)
    private Integer views;
    @Field(type = FieldType.Boolean)
    private Boolean status;
    @Field(type = FieldType.Date,format = DateFormat.custom,
            pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || epoch_millis" )
    @JsonProperty("edit_time")
    private Date editTime;
    @Field(type = FieldType.Date,format = DateFormat.custom,
            pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || epoch_millis" )
    @JsonProperty("publish_time")
    private Date publishTime;
    @Field(type = FieldType.Integer)
    private Integer likes;
    @Field(type = FieldType.Integer)
    private Integer comments;
    @Field(type = FieldType.Integer)
    private Integer collects;
    @Field(type = FieldType.Long)
    @JsonProperty("user_id")
    private Long userId;
    @Field(type = FieldType.Integer)
    @JsonProperty("category_id")
    private Integer categoryId;
}
