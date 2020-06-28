<template>
  <div>
    <div class="max-container">
      <div class="favorites-box">
        <div class="favorites-name" v-text="favorites.name"></div>
        <div class="favorites-desc">收藏夹描述：{{favorites.description}}</div>
        <el-row>
          <el-col :span="3" class="article-amount">{{countAmount}}条内容</el-col>
          <el-col :span="2">
            <el-button type="text" @click="openEditPage">编辑</el-button>
          </el-col>
          <el-col :span="2">
            <el-button type="text" @click="deleteFavorites">删除</el-button>
          </el-col>
        </el-row>
      </div>
      <!-- 收藏夹文章列表 -->
      <div v-for="(article,index) in favorites.articleList" :key="index" class="article_row">
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
          <div class="cancel" @click="cancelCollection(article.id)">取消收藏</div>
          <!-- <el-button size="small" type="text" @click="cancelCollection(article.id)"></el-button> -->
        </div>
      </div>
      <!-- <div class="left-bottom">
        <el-button v-if="moreShow" type="text" @click="load">more</el-button>
        <p v-if="loading">加载中...</p>
        <p v-if="noMore">没有更多了</p>
      </div> -->
    </div>

    <el-dialog :show-close="false" title="编辑收藏夹" :visible.sync="dialogVisible" width="30%">
      <el-form :model="favorites" :rules="rules" ref="favoritesForm">
        <el-form-item label="收藏夹名称" prop="name">
          <el-input v-model="favorites.name"></el-input>
        </el-form-item>
        <el-form-item label="是否公开" prop="secret">
          <el-switch v-model="favorites.secret"></el-switch>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="favorites.description"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm('favoritesForm')">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: false,
      favoritesId: null,
      favorites: {},
      countAmount: null,
      rules: {
         name: [
            { required: true, message: '请输入', trigger: 'blur' },
            { min: 1, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
          ],
      }
    };
  },
  mounted(){
    this.init();
  },
  methods: {
    init(){
      this.favoritesId = parseInt(this.$route.query.favoritesId);
      console.log('收藏夹Id'+this.favoritesId);
      this.getUserFavoritesDetail();
    },
    //   打开编辑页面
    openEditPage() {
      this.dialogVisible = true;
    },
    //取消收藏
    cancelCollection(articleId){
       this.axios
        .delete("/api/user/favorites/"+this.favoritesId+"/article/"+articleId)
        .then(res => {
          var data = res.data;
          if (data.code === 204) {
            this.getUserFavoritesDetail();
            this.$message({type: 'success',message: '取消成功'});
          } else {
            this.$message({type: 'warning',message: '取消失败'});
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          this.$message({type: 'warning',message: '发生错误'});
          console.log("错误" + err);
        });
    },
    //校验
     submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.updateFavorites();
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
    //更新收藏夹
    updateFavorites(){
      this.axios
        .put("/api/user/favorites",{
          id: this.favoritesId,
          name: this.favorites.name,
          description: this.favorites.description,
          secret: this.favorites.secret
        })
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
           this.$message({type: 'success',message: '修改成功'});
           this.dialogVisible = false;
           this.getUserFavoritesDetail();
          } else {
            this.$message({type: 'warning',message: '修改失败'});
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
           this.$message({type: 'warning',message: '发生错误'});
          console.log("错误" + err);
        });
    },
    //删除收藏夹
    deleteFavorites() {
       this.axios
        .delete("/api/user/favorites/"+this.favoritesId)
        .then(res => {
          var data = res.data;
          if (data.code === 204) {
            this.$router.push({
            path: "/personal_center/favorites_List",
            query: { activeName: "collect" }
          });
           this.$message({type: 'success',message: '删除成功'});
          } else {
            this.$message({type: 'warning',message: '删除失败'});
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
           this.$message({type: 'warning',message: '发生错误'});
          console.log("错误" + err);
        });
    },
    //获取用户收藏夹内的文章列表
    getUserFavoritesDetail() {
       this.axios
        .get("/api/user/favorites/"+this.favoritesId+"/articles")
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.favorites = data.data;
            this.countAmount = this.favorites.articleList.length
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
    // noMore() {
    //   return this.count >= 10;
    // },
    // disabled() {
    //   return this.loading || this.noMore;
    // },
    // moreShow() {
    //   return !this.loading && !this.noMore;
    // }
  }
};
</script>

<style scoped>
/* .max-container {
} */
.favorites-box {
  border-bottom: 1px solid #dcdfe6;
  margin-left: 20px;
  margin-right: 20px;
}
.favorites-name {
  font-size: 16px;
  font-weight: bold;
}
.favorites-desc {
  font-size: 14px;
}
.article-amount {
  height: 40px;
  display: flex;
  align-items: center;
  font-size: 14px;
}
/* -------------- */

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
  /* justify-content: center; */
  cursor: pointer;
  /* border: 1px solid #EDEEEF; */
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