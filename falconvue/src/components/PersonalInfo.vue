<template>
  <div>
    <div class="page-container">
      <div class="max-box">
        <h2>个人资料</h2>
        <div class="avatar-row">
          <div class="row-item">头像</div>
          <div class="avatar-img">
            <el-avatar shape="square" :size="80" fit="contain" :src="user.avatar"></el-avatar>
          </div>
          <!-- <el-upload
            class="avatar-uploader"
            action="https://jsonplaceholder.typicode.com/posts/"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>-->
          <!-- <div class="upload-avatar"><el-button size="small">点击上传</el-button></div> -->
        </div>
        <div class="row">
          <div class="row-item">用户昵称</div>
          <div v-text="user.nickname"></div>
        </div>
        <div class="row">
          <div class="row-item">电话号码</div>
          <div v-text="user.phone"></div>
        </div>
        <div class="row">
          <div class="row-item">email</div>
          <div v-text="user.email"></div>
        </div>
        <div class="row">
          <div class="row-item">加入日期</div>
          <div v-text="user.createTime"></div>
        </div>
        <div class="edit-btn">
          <el-button @click="openEditWindow">修改资料</el-button>
          <el-button @click="openModifyPassword">修改密码</el-button>
        </div>
      </div>
    </div>

    <el-dialog title="资料修改" :visible.sync="dialogVisible" width="30%" :show-close="false">
      <el-form :model="userForm" label-width="100px" label-position="top">
        <el-form-item label="头像">
          <el-input v-model="userForm.avatar"></el-input>
        </el-form-item>
        <el-form-item label="用户昵称">
          <el-input v-model="userForm.nickname"></el-input>
        </el-form-item>
        <el-form-item label="电话号码">
          <el-input v-model="userForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="email">
          <el-input v-model="userForm.email"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateUserInfo">提交</el-button>
      </span>
    </el-dialog>

    <el-dialog title="修改密码" :visible.sync="passwordDialogVisible" width="30%" :show-close="false">
      <el-form :model="passwordForm" label-width="100px" label-position="top">
        <el-form-item label="原密码">
          <el-input v-model="passwordForm.password" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="passwordForm.newPassword" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="passwordForm.confirmPassword" show-password></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="passwordDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateUserPassword">提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: false,
      passwordDialogVisible: false,
      passwordForm: {
        password: "",
        newPassword: "",
        confirmPassword: ""
      },
      user: {},
      userForm: {
        nickname: "",
        phone: "",
        email: "",
        avatar: ""
      }
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.getUserInfo();
    },
    //获取用户信息
    getUserInfo() {
      this.axios
        .get("/api/user")
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.user = data.data;
            this.dialogVisible = false;
            sessionStorage.setItem("User", JSON.stringify(data.data));
          } else {
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    //用户密码修改
    updateUserPassword() {
      if (this.passwordForm.newPassword != this.passwordForm.confirmPassword) {
        this.$message({ type: "warning", message: "两次输入不相同" });
        return false;
      }
      this.axios
        .put("/api/user/password", {
          password: this.passwordForm.password,
          newPassword: this.passwordForm.newPassword
        })
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.getUserInfo();
            this.passwordDialogVisible = false;
            this.$message({ type: "success", message: "修改成功" });
            sessionStorage.removeItem("Authorization");
            sessionStorage.removeItem("User");
            window.location.reload();
          } else {
            this.$message({ type: "error", message: "修改失败" });
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          this.$message({ type: "error", message: "发生错误失败！" });
          console.log("错误" + err);
        });
    },
    openEditWindow() {
      this.dialogVisible = true;
      this.userForm = this.user;
    },
    openModifyPassword() {
      this.passwordDialogVisible = true;
      this.passwordForm = {
        password: "",
        newPassword: "",
        confirmPassword: ""
      };
    },
    //更新用户信息
    updateUserInfo() {
      this.axios
        .put("/api/user", {
          avatar: this.userForm.avatar,
          nickname: this.userForm.nickname,
          phone: this.userForm.phone,
          email: this.userForm.email
        })
        .then(res => {
          var data = res.data;
          if (data.code === 200) {
            this.getUserInfo();
            this.$message({ type: "success", message: "修改成功" });
          } else {
            this.$message({ type: "error", message: "修改失败" });
            console.log("请求失败" + JSON.stringify(res.data));
          }
        })
        .catch(err => {
          this.$message({ type: "error", message: "发生错误失败！" });
          console.log("错误" + err);
        });
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    }
  }
};
</script>

<style scoped>
.page-container {
  min-height: 750px;
  /* height: 100%; */
  display: flex;
  justify-content: center;
  background-color: #f4f5f5;
}
.max-box {
  width: 620px;
  height: 520px;
  margin-top: 15px;
  padding: 40px;
  background-color: #fff;
}
.row {
  height: 50px;
  display: flex;
  align-items: center;
  padding: 10px 0px 10px 0px;
  border-bottom: 1px solid #dcdfe6;
}
.avatar-row {
  height: 80px;
  display: flex;
  padding: 10px 0px 10px 0px;
  border-bottom: 1px solid #dcdfe6;
}
.upload-avatar {
  margin-left: 30px;
  display: flex;
  align-items: center;
}
/* .avatar-img {
  margin-left: 60px;
} */
.edit-btn {
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.row-item {
  width: 150px;
}

/* ------avatar------ */
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 80px;
  height: 80px;
  line-height: 80px;
  text-align: center;
}
.avatar {
  width: 80px;
  height: 80px;
  display: block;
}
/* ----------- */
</style>