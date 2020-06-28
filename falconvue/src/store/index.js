import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        Authorization: sessionStorage.getItem('Authorization') ? sessionStorage.getItem('Authorization') : '',
    },
    mutations: {
        changeLogin(state,user){
            state.Authorization = user.Authorization;
            sessionStorage.setItem('Authorization',user.Authorization);
        },
        // changeUser(state,user){
        //     console.log('vux'+JSON.stringify(user));
        //     state.user = user;
        // }
    }
})

export default store