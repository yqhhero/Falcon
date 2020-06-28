<template>
  <div>
    <div class="page-box">
      <div class="max-box">
        <div class="main-box">
          <!-- title -->
          <div class="title-row">{{article.title}}</div>
          <!-- 作者 -->
          <div class="author-row">
            <div class="author-nickname" v-if="article.user != null">{{article.user.nickname}}</div>
            <div class="author-item">{{article.publishTime}}</div>
            <div class="author-item">浏览量{{article.views}}</div>
          </div>
          <!-- 文章内容 -->
          <div class="typo article-content js-toc-content" v-html="article.content"></div>
          <!-- 评论区 -->
          <div id="commentArea" class="commentArea">
            <div class="comment-top">
              <div class="edit-comment">
                <el-avatar icon="el-icon-user-solid"></el-avatar>
                <el-input
                  class="textarea-input"
                  type="textarea"
                  v-model="commentContent"
                  :placeholder="replyNickname"
                ></el-input>
              </div>
              <div class="publish-btn">
                <el-button @click="clear" size="small">清空</el-button>
                <el-button @click="onSubmitComment" size="small">发表</el-button>
              </div>
            </div>
            <!-- 评论列表 -->
            <div class="comment-list">
              <div
                :v-if="commentList.length > 0"
                v-for="item in commentList"
                :key="item.value"
                class="comment-item"
              >
                <div class="row-one">
                  <div class="avatar-item">
                    <el-avatar :src="item.user.avatar"></el-avatar>
                  </div>
                  <div class="info-container">
                    <div class="user-nickname">{{item.user.nickname}}</div>
                    <div class="comment-content">{{item.content}}</div>
                    <div class="info-bottom">
                      <div class="comment-time-text">{{item.createTime}}</div>
                      <!-- <div class="img-item">
                        <img src="../../static/images/like16.png" alt />
                      </div>-->
                      <div class="img-item" @click="replyComment(item.id,item.user.nickname)">
                        <img src="../../static/images/comment16.png" alt />
                      </div>
                    </div>
                  </div>
                </div>
                <div
                  :v-if="item.replyComments.length > 0"
                  v-for="reply in item.replyComments"
                  :key="reply.value"
                  class="replay-list"
                >
                  <div class="avatar-item">
                    <el-avatar :src="reply.user.avatar"></el-avatar>
                  </div>
                  <div class="info-container">
                    <div
                      class="user-nickname"
                    >{{reply.user.nickname}}&nbsp;回复&nbsp;@{{reply.parentComment.user.nickname}}</div>
                    <div class="comment-content">{{reply.content}}</div>
                    <div class="info-bottom">
                      <div class="comment-time-text">{{reply.createTime}}</div>
                      <!-- <div class="img-item">
                        <img src="../../static/images/like16.png" alt />
                      </div>-->
                      <div
                        class="img-item"
                        slot="reference"
                        @click="replyComment(reply.id,reply.user.nickname)"
                      >
                        <img src="../../static/images/comment16.png" alt />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 侧栏 -->
        <div class="right-box">
          <!-- 用户 -->
          <div class="userInfo-box" v-if="article.user != null">
            <div class="avatar-box">
              <el-avatar shape="square" :size="100" fit="contain" :src="article.user.avatar"></el-avatar>
            </div>
            <!-- <div class="username" >{{article.user.nickname}}</div> -->
            <div class="info-data">
              <div class="info-item">
                <img src="../../static/images/article16.png" />
                <div class="amount">文章数量{{article.user.articleAmount}}</div>
              </div>
              <div class="info-item">
                <img src="../../static/images/like16t.png" />
                <div class="amount">总获赞数{{article.user.obtainLikes}}</div>
              </div>
              <div class="info-item">
                <img src="../../static/images/views16t.png" />
                <div class="amount">被阅读数{{article.user.obtainViews}}</div>
              </div>
            </div>
          </div>
          <!-- 侧边工具 -->
          <div class="tool">
            <el-badge :value="article.likes" class="item" type="info">
              <el-button class="tool-btn" @click="likeArticle">
                <img v-show="!liked" src="../../static/images/like22.png" />
                <img v-show="liked" src="../../static/images/liked22.png" />
              </el-button>
            </el-badge>
            <el-badge :value="article.comments" class="item" type="info">
              <a href="#commentArea">
                <el-button class="tool-btn">
                  <img src="../../static/images/comment22.png" />
                </el-button>
              </a>
            </el-badge>
            <el-badge :value="article.collects" class="item" type="info">
              <el-popover placement="left" title="收藏夹" width="240" trigger="click">
                <div class="favorites-box">
                  <div
                    class="favorites-item"
                    v-for="(favorites,index) in favoritesList"
                    :key="index"
                  >
                    <div class="favorites-name">{{favorites.name}}</div>
                    <el-button
                      class="collect-btn"
                      @click="collectArticle(favorites.id)"
                      v-show="!favorites.isCollect"
                      size="small"
                    >添加</el-button>
                    <el-button
                      class="collect-btn"
                      @click="cancelCollectArticle(favorites.id)"
                      v-show="favorites.isCollect"
                      type="primary"
                      size="small"
                    >取消</el-button>
                  </div>
                </div>
                <el-button class="tool-btn" slot="reference" @click="openFavorites">
                  <img src="../../static/images/collect22.png" />
                </el-button>
              </el-popover>
            </el-badge>
            <el-popover placement="left" title="目录" width="300" trigger="click">
              <div>
                <ol class="js-toc"></ol>
              </div>
              <el-button class="tool-btn" slot="reference" @click="openList">
                <img src="../../static/images/list22.png" />
              </el-button>
            </el-popover>
            <el-button class="tool-btn" @click="openReportWindow">
              <img src="../../static/images/report22.png" alt />
            </el-button>
          </div>
        </div>
        <el-backtop :bottom="100">
          <div
            style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .12);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }"
          >UP</div>
        </el-backtop>
      </div>
    </div>

    <!-- 反馈对话窗 -->
    <el-dialog title="反馈" :visible.sync="dialogVisible" width="30%">
      <el-form>
        <el-form-item label="原因描述">
          <el-input type="textarea" v-model="reason"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="onSubmitReport">提交</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import tocbot from "tocbot";
export default {
  data() {
    return {
      dialogVisible: false,
      favoritesDialogVisible: false,
      liked: false,
      reason: "",
      article: {},
      commentList: [],
      commentContent: "",
      replyId: null,
      replyNickname: "评论文章",
      favoritesList: []
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      var type = parseInt(this.$route.query.type);
      if (type === 0) {
        this.getArticleDetail();
      } else if (type === 1) {
        this.getUserArticleDetail();
      }
    },
    //文章目录
    openList() {
      tocbot.init({
        // Where to render the table of contents.
        tocSelector: ".js-toc",
        // Where to grab the headings to build the table of contents.
        contentSelector: ".js-toc-content",
        // Which headings to grab inside of the contentSelector element.
        headingSelector: "h1, h2, h3",
        // For headings inside relative or absolute positioned containers within content.
        hasInnerContainers: true
      });
    },
    //打开反馈页面
    openReportWindow() {
      if (!sessionStorage.getItem("Authorization")) {
        this.$message("请登录");
        return false;
      }
      this.dialogVisible = true;
      this.reason = "";
    },
    //提交反馈信息
    onSubmitReport() {
      if (this.reason.trim().length == 0) {
        this.$message({ type: "warning", message: "内容不能为空" });
        return false;
      }
      this.axios
        .post("/api/user/report", {
          reason: this.reason,
          articleId: this.article.id
        })
        .then(res => {
          var data = res.data;
          if (data.code === 201) {
            this.$message({ type: "success", message: "提交成功" });
          } else {
            this.$message({ type: "error", message: "提交失败" });
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    //获取文章详细信息
    getArticleDetail() {
      const articleId = this.$route.query.articleId;
      this.axios
        .get("/api/article/" + articleId)
        .then(res => {
          // console.log(JSON.stringify(res.data));
          var data = res.data;
          const code = data.code;
          if (code === 200) {
            this.article = data.data;
            this.getArticleComment(articleId);
            this.isLiked();
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    //获取用户文章详细信息
    getUserArticleDetail() {
      if (!sessionStorage.getItem("Authorization")) {
        this.$message("请登录");
        return false;
      }
      const articleId = this.$route.query.articleId;
      this.axios
        .get("/api/user/article/" + articleId, {
          params: { isEdit: false }
        })
        .then(res => {
          // console.log(JSON.stringify(res.data));
          var data = res.data;
          const code = data.code;
          if (code === 200) {
            this.article = data.data;
            this.getArticleComment(articleId);
            this.isLiked();
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    //获取文章评论信息
    getArticleComment(articleId) {
      this.axios
        .get("/api/comments/" + articleId)
        .then(res => {
          var data = res.data;
          const code = data.code;
          if (code === 200) {
            // console.log("评论信息" + JSON.stringify(data.data));
            this.commentList = data.data;
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    //点赞
    likeArticle() {
      if (!sessionStorage.getItem("Authorization")) {
        this.$message("请登录");
        return false;
      }
      this.axios
        .post("/api/user/article/" + this.article.id)
        .then(res => {
          var data = res.data;
          if (data.code === 201) {
            this.liked = !this.liked;
            if (this.liked) {
              this.article.likes += 1;
            } else {
              this.article.likes -= 1;
            }
          } else {
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    //查询是否点赞
    isLiked() {
      this.axios
        .get("/api/user/liked/" + this.article.id)
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.liked = true;
          } else {
            console.log("没有点赞");
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    //回复评论
    replyComment(replyId, replyNickname) {
      console.log("回复" + replyId + "--" + replyNickname);
      this.replyId = replyId;
      this.replyNickname = "@" + replyNickname;
    },
    clear() {
      this.replyId = null;
      this.replyNickname = "评论文章";
      this.commentContent = "";
    },
    //提交评论
    onSubmitComment() {
      if (!sessionStorage.getItem("Authorization")) {
        this.$message("请登录");
        return false;
      }
      if (this.commentContent.length === 0) {
        this.$message("请输入内容");
        return false;
      }
      this.axios
        .post("/api/user/comment", {
          content: this.commentContent,
          articleId: this.article.id,
          parentComment: {
            id: this.replyId
          }
        })
        .then(res => {
          var data = res.data;
          if (data.code === 201) {
            this.$message({ type: "success", message: "评论成功" });
            this.getArticleComment(this.article.id);
          } else {
            this.$message("评论失败");
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          this.$message("发生错误");
          console.log("错误" + err);
        });
    },
    //打开收藏夹
    openFavorites() {
      if (!sessionStorage.getItem("Authorization")) {
        this.$message("请登录");
        return false;
      }
      this.axios
        .get("/api/user/favoritesList/article/" + this.article.id)
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.favoritesList = data.data;
          } else {
            this.$message("发生错误");
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          this.$message("发生错误");
          console.log("错误" + err);
        });
    },
    //添加收藏
    collectArticle(favoritesId) {
      this.axios
        .post(
          "/api/user/favorites/" + favoritesId + "/article/" + this.article.id
        )
        .then(res => {
          var data = res.data;
          if (data.code === 201) {
            this.openFavorites();
            this.article.collects += 1;
          } else {
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    //取消收藏
    cancelCollectArticle(favoritesId) {
      this.axios
        .delete(
          "/api/user/favorites/" + favoritesId + "/article/" + this.article.id
        )
        .then(res => {
          var data = res.data;
          if (data.code === 204) {
            this.openFavorites();
            this.article.collects -= 1;
          } else {
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    }
  }
};
</script>

<style scoped>
a {
  text-decoration: none;
}
.tool {
  z-index: 999;
  position: fixed;
  right: 40px;
  bottom: 150px;
  display: flex;
  flex-direction: column;
}
.tool-btn {
  margin-bottom: 3px;
  padding: 7px;
  width: 40px;
  height: 40px;
}

.page-box {
  min-height: 650px;
  display: flex;
  justify-content: center;
  flex-direction: row;
  background-color: #f4f5f5;
}

.max-box {
  width: 960px;
  display: flex;
  margin-top: 15px;
}
.main-box {
  width: 700px;
  margin-bottom: 20px;
  padding: 24px;
  background-color: #fff;
}

.title-row {
  height: 40px;
  font-size: 24px;
  font-weight: bold;
}
.author-row {
  display: flex;
  padding: 5px 0px;
  border-bottom: 1px solid #dcdfe6;
  font-size: 13px;
}
.author-item {
  margin-left: 10px;
}
.article-content {
  padding: 0px 0px 10px 0px;
  border-bottom: 1px solid #dcdfe6;
}

.article-buttom {
  display: flex;
  height: 50px;
  background-color: #f4f5f5;
}
.operating-item {
  width: 80px;
  display: flex;
  margin-left: 5px;
  align-items: center;
  cursor: pointer;
}
.count-item {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: rgb(162, 179, 179);
}

/* 评论区 */
.comment-top {
  margin-top: 30px;
  padding: 10px;
  background-color: #f4f5f5;
}
.edit-comment {
  margin-top: 20px;
  height: 50px;
  display: flex;
}
.textarea-input {
  margin-left: 20px;
}
.publish-btn {
  margin-top: 10px;
  text-align: right;
}

/* 评论列表 */
.comment-list {
  margin-left: 50px;
  margin-top: 20px;
}
.row-one {
  display: flex;
}
.info-container {
  width: 600px;
  padding: 10px;
  margin-left: 10px;
  border-bottom: 1px solid #dcdfe6;
}
.user-nickname {
  font-size: 13px;
}
.comment-content {
  display: flex;
  align-items: center;
  font-size: 14px;
  height: 30px;
}
.comment-time-text {
  font-size: 13px;
  color: #8a9aa9;
}
.info-bottom {
  display: flex;
}
.img-item {
  width: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  cursor: pointer;
}

.replay-list {
  padding: 5px 0px;
  display: flex;
  margin-left: 40px;
}

/* 侧栏 */
/* .right-box {
} */

/* 用户信息css */
.userInfo-box {
  width: 240px;
  height: 300px;
  margin-left: 20px;
  background-color: #fff;
}
.avatar-box {
  height: 140px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.username {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
}
.info-data {
  margin-top: 20px;
}
.info-item {
  display: flex;
  align-items: center;
  margin-top: 5px;
  margin-left: 30%;
}
.amount {
  margin-left: 10px;
  font-size: 15px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}

.favorites-box {
  height: 120px;
  overflow-y: auto;
}
.favorites-item {
  display: flex;
  padding: 10px 0px 10px 0px;
  border-top: 1px solid #f4f5f5;
}
.favorites-name {
  font-size: 16px;
  display: flex;
  align-items: center;
  width: 100px;
  margin-left: 3%;
}
.collect-btn {
  width: 60px;
  height: 32px;
  margin-left: 25%;
}
</style>

<style>
@import "../assets/typo.css";
@import "../assets/tocbot.css";
</style>