import Vue from 'vue'
import Router from 'vue-router'
import Navigation from '@/components/Navigation'
import IndexView from '@/components/IndexView'
import EditArticle from '@/components/EditArticle'
import ArticleDetail from '@/components/ArticleDetail'
import PersonalCenter from '@/components/PersonalCenter'
import UserArticle from '@/components/UserArticle'
import LikeArticle from '@/components/LikeArticle'
import FavoritesList from '@/components/FavoritesList'
import FavoritesDetail from '@/components/FavoritesDetail'

import PersonalInfo from '@/components/PersonalInfo'
import Search from '@/components/Search'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      component: Navigation,
      children: [
        {
          path: '',
          component: IndexView
        },
        {
          path: 'article_detail',
          component: ArticleDetail
        },
        {
          path: 'personal_center',
          component: PersonalCenter,
          children: [
            {
              name: 'user_article',
              path: 'user_article',
              component: UserArticle
            },
            {
              name: 'like_article',
              path: 'like_article',
              component: LikeArticle
            },
            {
              name: 'favorites_list',
              path: 'favorites_list',
              component: FavoritesList
            },
            {
              name: 'favorites_detail',
              path: 'favorites_detail',
              component: FavoritesDetail
            }
          ]
        },
        {
          path: 'personal_info',
          component: PersonalInfo
        },
        {
          path: 'search',
          component: Search,
        }
      ]
    },
    {
      path: '/edit_article',
      component: EditArticle
    },
  

  ]
});

router.beforeEach((to,from,next) => {
  if(to.path === '/' || to.path === '/article_detail' || to.path === '/search'){
    next();
  } else {
    let token = sessionStorage.getItem('Authorization');
    if(!token){
      next('/');
    } else {
      next();
    }
  }
})

export default router;


