<template>
  <div class="max_div">
    <div class="content_div">
      <!-- 主栏 -->
      <div class="left_box">
        <div class="category">
          <el-tabs class="tab-box" v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="发现" name="发现"></el-tab-pane>
            <el-tab-pane
              v-for="item in categoryList"
              :key="item.value"
              :label="item.name"
              :name="item.name"
            ></el-tab-pane>
          </el-tabs>
        </div>
        <div class="header_list">
          <div class="header_btn" @click="change(1)">
            <div>
              <img src="../../static/images/popular16.png" />
            </div>
            <div class="popular-text">热门</div>
          </div>
          <div class="header_btn" @click="change(0)">
            <div>
              <img src="../../static/images/new16.png" />
            </div>
            <div class="new-text">最新</div>
          </div>
        </div>
        <!-- 文章项目 -->
        <div :key="index" v-for="(article,index) in articles"  class="article_row">
          <div class="userName_box">
            {{ article.user.nickname }}-{{article.publishTime}}
            <div class="tags-box" v-if="article.tags.length > 0">
              <div v-for="tag in article.tags" :key="tag.value">-{{tag.name}}</div>
            </div>
          </div>
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
            <div class="img_box">
              <div>
                <img src="../../static/images/view16.png" />
              </div>
              <div class="amount">{{ article.views }}</div>
            </div>
          </div>
        </div>
        <!-- 底部 -->
        <div class="left-bottom">
          <el-button v-if="moreShow" type="text" @click="loadArticles">more</el-button>
          <p v-if="loading">加载中...</p>
          <p v-if="noMore">没有更多了</p>
        </div>
      </div>
      <!-- 侧栏 -->
      <div class="right_box">
        <div class="right-box-item-top">站内推荐</div>
        <div class="right-box-item-main">
          <div v-for="(item,index) in  recommendArticleList" :key="index" class="main-item">
            <router-link :to="{path:'/article_detail',query:{articleId:item.id,type:0}}"><p class="recommend-title">{{ item.title }}</p></router-link>
          </div>
        </div>
        <!-- <el-button class="right-box-item-bottom" type="text">查看更多>></el-button> -->
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeName: "发现",
      currentCategory: 0,
      isPopualr: 0,
      pageNum: 1,
      pageSize: 6,
      pages: null,
      articles: [],
      count: 4,
      loading: false,
      recommendArticleList: [],
      categoryList: []
    };
  },
  mounted() {
    this.getCategoryList();
    this.getArticleList();
    this.getRecommendArticleList();
  },
  methods: {
    change(val){
      this.isPopualr = val;
      this.getArticleList();
    },
    //获取文章列表信息
    getArticleList() {
      this.axios
        .get("/api/articles/category/" + this.currentCategory, {
          params: {
            popular: this.isPopualr,
            pageNum: 1,
            pageSize: 5
          }
        })
        .then(res => {
          // console.log("数据" + JSON.stringify(res.data));
          var pageInfo = res.data.data;
          if (res.data.code === 200) {
            this.articles = pageInfo.data;
            this.pages = pageInfo.pages;
            this.pageNum = 2;
          } else {
            console.log("失败");
          }
        })
        .catch(err => {
          console.log("错误");
        });
    },
    //加载更多
    loadArticles() {
      if (this.pageNum > this.pages) {
        return true;
      }
      this.axios
        .get("/api/articles/category/" + this.currentCategory, {
          params: {
            popular: this.isPopualr,
            pageNum: this.pageNum,
            pageSize: 5,
          }
        })
        .then(res => {
          // console.log("数据" + JSON.stringify(res.data));
          if (res.data.code === 200) {
            var pageInfo = res.data.data;
            pageInfo.data.forEach(Element => {
              this.articles.push(Element);
            });
            this.pageNum += 1;
            this.pages = pageInfo.pages;
          } else {
            console.log("失败");
          }
        })
        .catch(err => {
          console.log("错误");
        });
    },
    //获取分类信息列表
    getCategoryList() {
      this.axios.get("/api/categories").then(res => {
        var data = res.data;
        if(data.code === 200){
          this.categoryList = data.data;
        }
      }).catch(err => {
        console.log("错误");
      })
    },
    //文章加载
    load() {
      this.loading = true;
      this.axios
        .get("/articles", {
          params: {}
        })
        .then(res => {
          console.log(res);
        })
        .catch(err => {});
      this.articles.push(this.article);
      this.count += 1;
      this.loading = false;
    },
    handleClick(tab, event) {
      console.log(tab, event);
      switch(tab.name){
         case '发现':  this.currentCategory = 0 ;break;
        case 'Java':  this.currentCategory = 1 ;break;
        case '前端': this.currentCategory = 2 ;break;
        case '后端': this.currentCategory = 3 ;break;
        case '数据库': this.currentCategory = 4 ;break;
        case '阅读': this.currentCategory = 5 ;break;
      }
      this.getArticleList();
    },
    //获取推荐文章列表信息
    getRecommendArticleList(){
       this.axios
        .get("/api/recommend/articles")
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.recommendArticleList = data.data
          } else {
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
  },
  computed: {
    noMore() {
      return this.articles.length > 25 || this.pageNum > this.pages;
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
.max_div {
  min-height: 600px;
  display: flex;
  justify-content: center;
  background-color: #f4f5f5;
}
.content_div {
  width: 960px;
  margin-top: 20px;
  display: flex;
}
.left_box {
  width: 700px;
}

/* 分类信息 */
.category {
  display: flex;
  background-color: #fff;
  padding: 10px;
  /* border-bottom: 1px solid #f4f5f5; */
}
.category-item {
  width: 60px;
  text-align: center;
}
.tab-box {
  width: 700px;
}

/* 头选择 */
.header_list {
  height: 20px;
  padding: 5px;
  display: flex;
  /* align-items: center; */
  background-color: #fff;
}
.header_btn {
  width: 50px;
  margin-left: 13px;
  display: flex;
  font-size: 13px;
  cursor: pointer;
}
.popular-text {
  margin-left: 3px;
  color: #d81e06;
}
.new-text {
  margin-left: 3px;
  color: #1296db;
}
/* -----end-------- */

.article_row {
  width: 652px;
  height: 80px;
  padding: 18px 24px;
  margin-top: 2px;
  background-color: #fff;
}
.userName_box {
  display: flex;
  font-size: 12px;
  font-family: Microsoft YaHei;
  color: #b2bac2;
}
.tags-box {
  display: flex;
}
.title_box {
  margin-top: 8px;
  font-size: 17px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: #2e3135;
  cursor: pointer;
}
.article_bottom_box {
  margin-top: 10px;
  display: flex;
}
.img_box {
  width: 50px;
  height: 20px;
  display: flex;
  cursor: pointer;
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

.right_box {
  margin-left: 20px;
  background-color: #fff;
  width: 240px;
  height: 390px;
  padding: 20px;
  display: flex;
  flex-direction: column;
}
.right-box-item-top {
  width: 240px;
  height: 50px;
  font-size: 20px;
  display: flex;
  color: #409eff;
  border-bottom: 1px solid #dcdfe6;
}
.right-box-item-main {
  width: 240px;
  height: 300px;
}
.main-item {
  height: 50px;
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #4f4f4f;
  border-bottom: 1px solid #dcdfe6;
}
.recommend-title {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 13px;
  font-family: Microsoft YaHei;
  color: #000;
}
.right-box-item-bottom {
  height: 38px;
}
</style>