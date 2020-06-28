<template>
  <div>
    <div>
      <div class="header-box">
        <router-link to="/"><el-button type="text">返回首页</el-button> </router-link>
        <div class="title-tag">文章标题</div>
        <el-input class="title-input" v-model="articleForm.title"></el-input>
        <el-button class="save-article" type="success" @click="save(0)">保存</el-button>
        <el-button class="publish-btn" type="primary" @click="dialogVisible = true;">发布</el-button>
      </div>
      <mavon-editor
        class="editor"
        v-model="articleForm.content"
        @change="change"
        ref="md"
        style="min-height: 600px"
      ></mavon-editor>
    </div>

    <el-dialog title="选择分类与标签" :visible.sync="dialogVisible" width="40%">
      <div>文章分类</div>
      <div>
        <el-radio-group v-model="articleForm.category.id">
          <el-radio
            v-for="(item) in categoryList"
            :key="item.id"
            :label="item.id"
            :value="item.id"
            border
            size="small"
          >{{item.name}}</el-radio>
        </el-radio-group>
      </div>
      <div>
        <div>文章标签</div>
        <el-select v-model="articleForm.tags" value-key="id" multiple placeholder="请选择">
          <el-option v-for="item in tagList" :key="item.id" :label="item.name" :value="item"></el-option>
        </el-select>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save(1)">发布</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mavonEditor } from "mavon-editor";
import "mavon-editor/dist/css/index.css";
export default {
  components: {
    mavonEditor
  },
  data() {
    return {
      articleForm: {
        id: null,
        title: "",
        content: "",
        category: {
          id: null,
        },
        tags: [],
        status: null
      },
      dialogVisible: false,
      categoryList: [],
      tagList: []
    };
  },
  mounted() {
    this.getCategoryList();
    this.getTagList();
    this.init();
    
  },
  methods: {
    init() {
      var articleId = this.$route.query.articleId;
      if (articleId !== undefined) {
        this.getArticleDetail(articleId);
      }
    },
    getArticleDetail(articleId) {
      this.axios
        .get("/api/user/article/" + articleId,{
          params:{isEdit:true}
        })
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.articleForm = data.data;
            if(this.articleForm.category == null){
              this.articleForm.category = {id:null}
            }
            console.log('内容'+JSON.stringify(this.articleForm))
          }
        })
        .catch(err => {});
    },
    getCategoryList() {
      this.axios
        .get("/api/categories")
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.categoryList = data.data;
          }
        })
        .catch(err => {
          console.log("错误");
        });
    },
    getTagList() {
      this.axios
        .get("/api/tags")
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.tagList = data.data;
            console.log("tagList"+JSON.stringify(this.tagList));
          }
        })
        .catch(err => {
          console.log("错误");
        });
    },
    save(status){
      if(this.articleForm.title.trim().length == 0 || this.articleForm.content.trim().length == 0){
        this.$message({type: 'warning',message:'内容不能为空'})
        return false;
      }
      this.articleForm.status = status;
      if(this.articleForm.id == null){
        this.addArticle();
      } else {
        this.updateArticle();
      }
    },
    //新增文章
    addArticle() {
      this.axios
        .post("/api/user/article",{
          title: this.articleForm.title,
          content: this.articleForm.content,
          status: this.articleForm.status,
          tags: this.articleForm.tags,
          category: {
            id: this.articleForm.category.id,
          }
        })
        .then(res => {
          var data = res.data;
          if (data.code === 201) {
            this.$message({type: 'success',message:'添加成功'})
            this.$router.push({name:'user_article'});
          } else {
            this.$message({type: 'error',message:'失败'})
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    updateArticle(){     
      this.axios.put("/api/user/article",{
        id: this.articleForm.id,
         title: this.articleForm.title,
          content: this.articleForm.content,
          status: this.articleForm.status,
          tags: this.articleForm.tags,
          category: {
            id: this.articleForm.category.id,
          }
      }).then(res => {
                var data = res.data;
                if(data.code === 200){
                    this.$message({type: 'success',message:'成功'})
                     this.$router.push({name:'user_article'});
                } else {
                   this.$message({type: 'error',message:'失败'})
                    console.log("请求失败"+JSON.stringify(res.data));
                }
            }).catch(err => {
                console.log('错误'+ err);
            });
    },
    change(value, render) {
      this.articleForm.content = render;
    }
  }
};
</script>

<style scoped>
.header-box {
  display: flex;
  padding: 5px;
}
.title-tag {
  display: flex;
  font-family: "Courier New", Courier, monospace;
  justify-content: center;
  align-items: center;
}
.title-input {
  width: 300px;
  margin-left: 15px;
}
.save-article {
  margin-left: 15px;
}
.publish-btn {
  width: 100px;
}
.editor {
  margin-top: 10px;
}
</style>