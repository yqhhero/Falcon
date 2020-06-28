<template>
  <div class="container_box">
    <!-- 导航部分 -->
    <el-container>
      <el-header class="header">
        <div class="navigation">
          <div class="nav_box">
            <div class="logo">
              <div @click="toHome">轻书</div>
            </div>
            <el-button type="text" class="nav_item">
              <div @click="toHome">首页</div>
            </el-button>
            <div class="nav_item_search">
              <el-input class="search_input" size="medium" v-model="searchParam" placeholder="搜索"></el-input>
              <el-button
                @click="search"
                icon="el-icon-search"
                class="search_icon"
                size="medium"
                circle
              ></el-button>
            </div>
            <el-button
              class="edit_btn"
              type="primary"
              size="medium"
              icon="el-icon-edit"
              @click="toCreatePage"
            >写文章</el-button>
            <div v-show="loginshow" class="nav_item_auth">
              <el-button type="text" @click="openLogin">登录</el-button>
              <el-button type="text" @click="registeredDialogVisible = true">注册</el-button>
            </div>
            <div v-show="!loginshow" class="nav_item_auth">
              <el-dropdown>
                <span class="el-dropdown-link">
                  <el-avatar :src="user.avatar" fit="contain"></el-avatar>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>
                    <div @click="enter(1)">我的主页</div>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <div @click="enter(2)">点赞文章</div>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <div @click="enter(3)">我的收藏</div>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <div @click="logout">退出</div>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
        </div>
      </el-header>
      <el-main class="main">
        <router-view></router-view>
      </el-main>
    </el-container>

    <!-- 登录窗口 -->
    <el-dialog title="登录" :visible.sync="loginDialogVisible" width="25%" center>
      <el-form ref="loginForm" :model="loginUser" :rules="userRules">
        <el-form-item class="form_item" prop="username">
          <el-input class="input_login" v-model="loginUser.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item class="form_item" prop="password">
          <el-input
            class="input_login"
            type="password"
            v-model="loginUser.password"
            placeholder="密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item class="form_item">
          <el-button class="login_btn" type="primary" @click="submitLogin('loginForm')">登录</el-button>
        </el-form-item>
        <div class="other_item">
          <el-link type="primary" @click="registeredDialogVisible = true">注册</el-link>
          <!-- <el-link type="primary">忘记密码</el-link> -->
        </div>
      </el-form>
    </el-dialog>

    <!-- 注册 -->
    <el-dialog title="注册" :visible.sync="registeredDialogVisible" width="25%" center>
      <el-form
        ref="registeredForm"
        :rules="userRules"
        :model="registeredUser"
        label-width="80px"
      >
        <el-form-item class="form_item" label="账户名称" prop="username">
          <el-input class="input_login" v-model="registeredUser.username" placeholder="账户名"></el-input>
        </el-form-item>
        <el-form-item class="form_item" label="昵称" prop="nickname">
          <el-input class="input_login" v-model="registeredUser.nickname" placeholder="昵称"></el-input>
        </el-form-item>
        <el-form-item class="form_item" label="密码" prop="password">
          <el-input
            class="input_login"
            type="password"
            v-model="registeredUser.password"
            placeholder="密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item class="form_item" label="确认密码" prop="confirmPassword">
          <el-input
            class="input_login"
            type="password"
            v-model="registeredUser.confirmPassword"
            placeholder="密码"
            show-password
          ></el-input>
        </el-form-item>
        <div class="other_item">
          <el-button class="login_btn" type="primary" @click="submitForm('registeredForm')">注册</el-button>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.registeredUser.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      searchParam: "",
      loginshow: null,
      loginDialogVisible: false,
      registeredDialogVisible: false,
      loginUser: {
        username: "",
        password: ""
      },
      registeredUser: {
        username: "",
        password: "",
        confirmPassword: "",
        nickname: ""
      },
      user: {
        avatar: ""
      },
      userRules: {
        username: [
          { required: true, message: '请输入账户名称', trigger: 'blur' },
          { min: 3, max: 7, message: "长度在 3 到 7 个字符", trigger: "blur" }
        ],
        nickname: [
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 2, max: 7, message: "长度在 2 到 7 个字符", trigger: "blur" }
        ],
        password: [
           { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 16, message: "长度在 6 到 16 个字符", trigger: "blur" }
        ],
        confirmPassword: [{ validator: validatePass, trigger: "blur" }]
      }
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      if (sessionStorage.getItem("User") != null) {
        this.user = JSON.parse(sessionStorage.getItem("User"));
      }
      if (sessionStorage.getItem("Authorization")) {
        this.loginshow = false;
      } else {
        this.loginshow = true;
      }
    },
    toHome(){
      this.$router.push('/');
      this.searchParam = '';
    },
    toCreatePage() {
      if (!sessionStorage.getItem("Authorization")) {
        this.$message("请登录");
        return false;
      }
      this.$router.push({ path: "/edit_article" });
    },
    enter(val) {
      console.log("状态" + val);
      switch (val) {
        case 1:
          this.$router.push({
            path: "/personal_center",
            query: { activeName: "" }
          });
          break;
        case 2:
          this.$router.push({
            path: "/personal_center/like_article",
            query: { activeName: "like" }
          });
          break;
        case 3:
          this.$router.push({
            path: "/personal_center/favorites_List",
            query: { activeName: "collect" }
          });
          break;
      }
    },
    //退出登录
    logout() {
      sessionStorage.removeItem("Authorization");
      sessionStorage.removeItem("User");
      this.loginshow = true;
    },
    //打开登录窗口
    openLogin() {
      this.loginDialogVisible = true;
      this.loginUser = { username: "", password: "" };
    },
    openRegistered() {
      this.registeredDialogVisible = true;
    },
    //登录校验
     submitLogin(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.login();
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    //登录方法
    login() {
      this.axios
        .get("/api/login", {
          params: {
            username: this.loginUser.username,
            password: this.loginUser.password
          }
        })
        .then(res => {
          console.log(JSON.stringify(res.data));
          if (res.data.code === 200) {
            let userToken = res.data.data.token;
            this.user = res.data.data.user;
            console.log(res.data.data.user);
            sessionStorage.setItem("User", JSON.stringify(res.data.data.user));
            // this.$store.commit('changeLogin',{Authorization:userToken});
            sessionStorage.setItem("Authorization", userToken);
            this.loginshow = false;
            this.loginDialogVisible = false;
          } else {
            console.log("失败");
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    //提交注册
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.registered();
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    //注册
    registered() {
      this.axios
        .post("/api/registered", {
          username: this.registeredUser.username,
          password: this.registeredUser.password,
          nickname: this.registeredUser.nickname
        })
        .then(res => {
          console.log(JSON.stringify(res.data));
          if (res.data.code === 201) {
            this.$message({ type: "success", message: "注册成功" });
            this.loginshow = false;
            this.loginDialogVisible = false;
          } else {
            console.log("失败");
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    search() {
      console.log("检索内容"+this.searchParam)
      if (this.searchParam == null) {
        this.$message("请输入搜索内容");
        return false;
      }
      this.$router.push({
        path: "/search",
        query: { queryParam: this.searchParam }
      });
    }
  },
};
</script>

<style scoped>
a {
  text-decoration: none;
  color: #409eff;
}
.header {
  padding: 0;
}
.main {
  padding: 0;
  margin: 0;
}
.navigation {
  width: 100%;
  padding: 0;
  position: fixed;
  display: flex;
  background-color: #fff;
  justify-content: center;
  border-bottom: 1px solid #dcdfe6;
  z-index: 999;
  /* box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); */
  _position: absolute; /* 把导航栏位置定义为绝对位置  关键*/
  /* _top: expression(documentElement.scrollTop + "px");
  z-index: 9999; */
}
.nav_box {
  width: 960px;
  height: 55px;
  display: flex;
  align-items: center;
}
.logo {
  width: 100px;
  font-size: 30px;
  color: #409eff;
  cursor: pointer;
}
.nav_item_search {
  width: 400px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  font-size: 25px;
}
.search_input {
  width: 200px;
}
.search_icon {
  margin-left: 10px;
}
.nav_item {
  width: 80px;
  font-size: 18px;
  color: #409eff;
}
.edit_btn {
  margin-left: 140px;
}
.nav_item_auth {
  margin-left: 40px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}
.el-icon-arrow-down {
  font-size: 12px;
}

.input_login {
  width: 250px;
}
.form_item {
  text-align: center;
}
.login_btn {
  width: 250px;
}
.other_item {
  display: flex;
  justify-content: space-around;
  color: #409eff;
}
</style>