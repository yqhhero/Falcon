<template>
  <div>
    <!-- 页面 -->
    <div class="page-box">
      <!-- 主体内容 -->
      <div class="main-box">
        <div v-for="(article,index) in articles" :key="index" class="article_row">
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
        <div class="left-bottom" >
          <el-pagination v-if="hasContent" background layout="prev, pager, next" 
          :total="total"
          :page-count="2"
          :current-page="pageNum"
          :page-size="pageSize"
           @current-change="handleCurrentChange">
          </el-pagination>
          <div v-if="!hasContent">未找到</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      articles: [],
      searchParam: "",
      pageNum: 1,
      pageSize: 5,
      pages: null,
      total: 0
    };
  },
  mounted() {
    this.init();
  },
  watch: {
    '$route'(){
      this.init();
    }
  },
  computed: {
    hasContent(){
      return this.articles.length > 0;
    }
  },
  methods: {
    init() {
      console.log("参数" + this.searchParam);
      this.searchParam = this.$route.query.queryParam;
      console.log("接收参数" + this.searchParam);
      this.search();
    },
    search() {
      this.axios
        .get("/api/articles/" + this.searchParam, {
          params: {
            pageNum: this.pageNum,
            pageSize: 5
          }
        })
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.articles = data.data.data;
            this.pages = data.data.pages;
            this.total = data.data.total;
          } else {
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    handleCurrentChange(val) {
      console.log("当前改变：" + val);
      this.pageNum = val;
      this.search();
    },
  },
};
</script>

<style scoped>
a {
  text-decoration: none;
}
.page-box {
  display: flex;
  justify-content: center;
  min-height: 700px;
  background-color: #f4f5f5;
}
.main-box {
  margin-top: 15px;
}

/* 头选择 */
.header_list {
  height: 25px;
  padding: 5px;
  display: flex;
  align-items: center;
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
/* ----------- */

/* 文章列表 */
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
/* -----end-------- */
</style>