<template>
  <div>
    <div class="article-box" v-for="article in articleList" :key="article.value">
      <!-- 第一行 -->
      <div class="article-top-box" v-if="article.status == 1">
        <el-tag size="medium" v-if="article.status == 1">发布</el-tag>
        <div class="publishTime" v-text="article.publishTime"></div>
      </div>
      <div class="article-top-box" v-if="article.status == 0">
        <el-tag size="medium" v-if="article.status == 0" type="success">草稿</el-tag>
        <div class="publishTime" v-text="article.editTime"></div>
      </div>
      <!-- 标题行 -->

      <router-link :to="{path:'/article_detail',query:{articleId:article.id,type:1}}">
        <div class="article-title">{{ article.title }}</div>
      </router-link>

      <!-- 信息行 -->
      <div class="article-info-box">
        <!-- 点赞数量 -->
        <div class="info-item">
          <img src="../../static/images/like16.png" alt />
          {{article.likes}}
        </div>
        <!-- 评论数量 -->
        <div class="info-item item-margin-left">
          <i class="el-icon-chat-square"></i>
          {{article.comments}}
        </div>
        <!-- 浏览量 -->
        <div class="info-item item-margin-left">
          <i class="el-icon-view"></i>
          {{article.views}}
        </div>
        <el-dropdown class="operational">
          <span class="el-dropdown-link">
            <i class="el-icon-arrow-down el-icon-arrow-right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>
              <div @click="editArticle(article.id)">编辑</div>
            </el-dropdown-item>
            <el-dropdown-item>
              <div @click="deleteArticle(article.id)">删除</div>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <div class="left-bottom">
      <el-button v-if="moreShow" type="text" @click="loadArticleList">more</el-button>
      <p v-if="loading">加载中...</p>
      <p v-if="noMore">没有更多了</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      articleList: [],
      pageNum: 1,
      // pageSize: 4,
      pages: 0
    };
  },
  mounted() {
    this.getUserArticleList();
  },
  methods: {
    //获取用户文章列表
    getUserArticleList() {
      this.axios
        .get("/api/user/articles", {
          params: {
            pageNum: 1,
            pageSize: 4
          }
        })
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.articleList = data.data.data;
            this.pages = data.data.pages;
            this.pageNum = 2;
          } else {
            this.$message("加载失败");
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    //文章加载
    loadArticleList() {
      this.loading = true;
      this.axios
        .get("/api/user/articles", {
          params: {
            pageNum: this.pageNum,
            pageSize: 4
          }
        })
        .then(res => {
          console.log(JSON.stringify(res.data));
          var resultData = res.data;
          if (resultData.code === 200) {
            resultData.data.data.forEach(Element => {
              this.articleList.push(Element);
            });
            this.pageNum += 1;
            this.pages = resultData.data.pages;
          } else {
            this.$message("加载失败");
          }
          this.loading = false;
        })
        .catch(err => {});
    },
    //删除文章
    deleteArticle(articleId) {
      this.axios
        .delete("/api/user/article/" + articleId)
        .then(res => {
          var data = res.data;
          if (data.code === 204) {
            this.$message({ type: "success", message: "删除成功" });
            this.getUserArticleList();
          } else {
            this.$message("删除失败");
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    //修改文章
    editArticle(articleId) {
      this.$router.push({
        path: "/edit_article",
        query: { articleId: articleId }
      });
    }
  },
  computed: {
    noMore() {
      return this.pageNum > this.pages;
    },
    disabled() {
      return this.loading || this.noMore;
    },
    moreShow() {
      return !this.loading && !this.noMore;
    }
  }
};
</script>

<style scoped>
a {
  text-decoration: none;
  color: #000;
}
a:hover {
  color: #409eff;
}
.article-box {
  padding: 20px 0px 20px 0px;
  margin-left: 20px;
  margin-right: 20px;
  border-bottom: 1px solid #dcdfe6;
}
.article-top-box {
  display: flex;
}
.publishTime {
  display: flex;
  align-items: center;
  font-size: 13px;
  margin-left: 8px;
  color: #b2bac2;
}
.article-title {
  height: 50px;
  display: flex;
  align-items: center;
  font-size: 25px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  cursor: pointer;
}
.article-info-box {
  display: flex;
  color: #b2bac2;
}
.info-item {
  width: 50px;
  font-size: 14px;
}
.item-margin-left {
  margin-left: 10px;
}
.operational {
  margin-left: 450px;
}
.left-bottom {
  display: flex;
  justify-content: center;
}
</style>