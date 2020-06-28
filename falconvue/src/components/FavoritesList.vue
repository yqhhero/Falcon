<template>
  <div>
    <div class="favorites-container">
      <div class="create-favorites" @click="openCreatePage">
        <i class="el-icon-plus"></i>新建收藏夹
      </div>
      <div class="favorites-row" v-for="item in favoritesList" :key="item.value">
        <router-link
          class="favorites-name"
          :to="{path:'/personal_center/favorites_detail',query:{favoritesId:item.id}}"
        >{{item.name}}</router-link>
        <!-- <div class="favorites-article-amount">{{item.articleAmount}}条内容</div> -->
      </div>
    </div>

    <el-dialog
      title="新建收藏夹"
      center
      :visible.sync="dialogVisible"
      width="30%"
      :show-close=false
    >
      <el-form :model="favorites" label-width="90px">
        <el-form-item label="收藏夹名称">
          <el-input v-model="favorites.name"></el-input>
        </el-form-item>
        <el-form-item label="收藏夹描述">
          <el-input type="textarea" v-model="favorites.description"></el-input>
        </el-form-item>
        <el-form-item label="是否公开">
          <el-switch v-model="favorites.secret"></el-switch>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="createFavorites">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: false,
      favoritesList: [],
      favorites: {
        name: "",
        description: "",
        secret: false
      }
    };
  },
  mounted(){
    this.getFavoritesList();
  },
  methods: {
    openCreatePage() {
      this.favorites = { name: "", description: "", secret: false };
      this.dialogVisible = true;
    },
    //获取用户个人收藏夹列表信息
    getFavoritesList() {
      this.axios
        .get("/api/user/favoritesList")
        .then(res => {
          var data = res.data;
          const code = data.code;
          if (code === 200) {
            this.favoritesList = data.data;
          } else {
            console.log("失败");
          }
        })
        .catch(err => {
          console.log("错误" + err);
        });
    },
    //用户新增收藏夹信息
    createFavorites() {
      this.axios
        .post("/api/user/favorites", {
          name: this.favorites.name,
          description: this.favorites.description,
          secret: this.favorites.secret
        })
        .then(res => {
          var data = res.data;
          const code = data.code;
          if (code === 201) {
            this.getFavoritesList();
            this.dialogVisible = false;
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
  color: #000;
}
a:hover {
  color: #409eff;
}
.favorites-container {
  margin-left: 20px;
  margin-right: 20px;
}
.create-favorites {
  text-align: right;
  font-size: 15px;
  cursor: pointer;
  color: #b2bac2;
}
.favorites-row {
  height: 80px;
  border-bottom: 1px solid #dcdfe6;
}
.favorites-name {
  height: 50px;
  display: flex;
  align-items: center;
  font-size: 17px;
  font-weight: bold;
  cursor: pointer;
}
.favorites-article-amount {
  font-size: 13px;
  color: rgb(187, 183, 183);
}
</style>