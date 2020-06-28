<template>
  <div>
    <div class="page-box">
      <div class="container-box">
        <!-- 主要内容 -->
        <div class="main-box">
          <!-- 顶栏 -->
          <div class="tab-box">
            <el-tabs v-model="activeName" @tab-click="handleClick">
              <el-tab-pane label="我的文章" name="my"></el-tab-pane>
              <el-tab-pane label="点赞文章" name="like"></el-tab-pane>
              <el-tab-pane label="收藏夹" name="collect"></el-tab-pane>
            </el-tabs>
          </div>
          <router-view @active="getActiveName"></router-view>
        </div>
        <!-- 用户 -->
        <div class="userInfo-box">
            <div class="avatar-box"><el-avatar shape="square" :size="100" fit="contain" :src="user.avatar"></el-avatar></div>
            <div class="username" v-text="user.nickname"></div>
            <div class="info-data">
              <div class="info-item"><img src="../../static/images/article16.png" /><div class="amount">文章数量{{user.articleAmount}}</div></div>
              <div class="info-item"><img src="../../static/images/like16t.png" /><div class="amount">总获赞数{{user.obtainLikes}}</div></div>
              <div class="info-item"><img src="../../static/images/views16t.png" /><div class="amount">被浏览量{{user.obtainViews}}</div></div>
            </div>
            <div class="userInfo-btn"><el-button size="mini" @click="toPersonalInfo">个人资料</el-button></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeName: "",
      user: {},
    };
  },
 created(){
    this.init();
  },
  methods: {
    init(){
      this.getUserInfo();
      this.activeName = this.$route.query.activeName;
      console.log(this.activeName);
      console.log(this.$route.query.activeName);
    },
    getUserInfo(){
       this.axios
        .get("/api/user")
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.user = data.data;
            sessionStorage.setItem("User",JSON.stringify(data.data));
          } else {
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    handleClick(tab, event) {
      console.log(tab, event);
      switch(tab.name){
          case 'my': this.$router.push({name:'user_article'});break;
          case 'like': this.$router.push({name:'like_article'});break;
          case 'collect': this.$router.push({name:'favorites_list'});break;
      }
    },
    getActiveName(data){
      this.activeName = data;
    },
    toPersonalInfo(){
      this.$router.push('/personal_info');
    }
  }
};
</script>

<style scoped>
img {
  height: 100%;
}
.page-box {
    display: flex;
    justify-content: center;
    background-color: #F4F5F5;
}
.container-box {
    width: 960px;
    display: flex;
    margin-top: 15px;
}
.main-box {
    width: 700px;
    min-height: 600px;
    background-color: #fff;
}
.tab-box {
    padding: 20px 20px 0px 20px;
}

/* 用户信息css */
.userInfo-box {
    width: 240px;
    height: 340px;
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
  /* color:#B2BAC2; */
  display: flex;
  align-items: center;
  margin-top: 5px;
  margin-left: 30%;
}
.amount {
  margin-left: 10px;
  font-size: 15px;
  font-family:  "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
}
.userInfo-btn {
  height: 60px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>