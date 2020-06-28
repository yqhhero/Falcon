<template>
  <div>
    <div>
      <div v-for="(article,index) in articleList" :key="index" class="article_row">
        <div class="userName_box">作者-{{ article.userName }}-{{article.publishTime}}</div>
        <router-link :to="{path:'/article_detail',query:{articleId:article.id,type:0}}">
          <el-link class="title_box">{{ article.title }}</el-link>
        </router-link>
        <div class="article_bottom_box">
          <div class="img_box">
            <div>
              <img src="../../static/images/like16.png" />
            </div>
            <div class="amount">{{ article.likes }}</div>
          </div>
          <div class="img_box">
            <div>
              <img src="../../static/images/comment.png" />
            </div>
            <div class="amount">{{ article.comments }}</div>
          </div>
          <div class="cancel" @click="cancelCollection(article.id)">
            取消喜欢
            <!-- <el-button type="text" size="small" @click="cancelCollection(article.id)">取消收藏</el-button> -->
          </div>
        </div>
      </div>
      <div class="left-bottom">
        <el-button v-if="moreShow" type="text" @click="loadArticleList">more</el-button>
        <p v-if="loading">加载中...</p>
        <p v-if="noMore">没有更多了</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      articleList: [],
      count: 4,
      loading: false,
      pageNum: 1,
      pages: 0
    };
  },
  mounted() {
    this.init();
    this.getUserLikeArticle();
  },
  methods: {
    //文章加载
    loadArticleList() {
      this.loading = true;
      this.axios
        .get("/api/user/like/articles", {
          params: {
            pageNum: this.pageNum+1,
            pageSize: 1
          }
        })
        .then(res => {
          console.log(JSON.stringify(res.data));
          var resultData = res.data;
          if (resultData.code === 200) {
            resultData.data.data.forEach(Element => {
              this.articleList.push(Element);
            });
            this.pages = resultData.data.pages;
            this.pageNum += 1;
          } else {
            this.$message('获取失败');
          }
        })
        .catch(err => {
           this.$message('错误');
        });
      this.loading = false;
    },
    init() {
      // this.$emit("active", this.$route.query.activeName);
    },
    //获取用户喜欢列表
    getUserLikeArticle() {
      this.axios
        .get("/api/user/like/articles", {
          params: {
            pageNum: 1,
            pageSize: 2
          }
        })
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.articleList = data.data.data;
            this.pageNum = 2;
            this.pages = data.data.pages;
          } else {
             this.$message('获取失败');
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
           this.$message('错误');
          console.log("错误" + err);
        });
    },
    //取消收藏
    cancelCollection(articleId) {
      this.axios
        .post("/api/user/article/" + articleId)
        .then(res => {
          var data = res.data;
          if (data.code === 201) {
            this.$message({ type: "success", message: "成功" });
            this.getUserLikeArticle();
          } else {
            this.$message("失败");
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          this.$message("失败");
          console.log("错误" + err);
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
}
.article_row {
  width: 652px;
  height: 80px;
  padding: 18px 24px;
  margin-top: 2px;
  background-color: #fff;
}
.userName_box {
  font-size: 12px;
  font-family: Microsoft YaHei;
  color: #b2bac2;
}
.title_box {
  margin-top: 8px;
  font-size: 17px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  cursor: pointer;
  color: #000;
}
.article_bottom_box {
  margin-top: 10px;
  display: flex;
}
.img_box {
  width: 50px;
  height: 20px;
  display: flex;
  cursor: default;
}
.amount {
  margin-left: 3px;
  font-size: 12px;
  color: #b2bac2;
}
.left-bottom {
  display: flex;
  justify-content: center;
}

.cancel {
  font-size: 13px;
  cursor: pointer;
  color: #409EFF;
}
/* 分界线 */
</style>