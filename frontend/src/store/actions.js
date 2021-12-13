import $axios from 'axios'
import { getServerURL } from '../common/lib/function.js';

// user API
export function userLogoutMM({state}, payload){
    const url = '/api/v4/users/logout'
    return $axios({
        method: 'post',
        url: url,
        headers:{
            "Authorization": "bearer " + payload,
        },
    })
}

export function userLoginMM({state}, payload){
    const url = '/api/v4/users/login'
    const body = payload.loginData

    return $axios.post(url, body);
}

export function getUserMM({state}){
    const url = '/api/v4/users/me'

    return $axios.get(url);
}

export function userLogin({state}, payload){
    const url = '/api/v1/user'
    const body = {
        "token" : payload.token,
        "userEmail" : payload.userEmail,
        "userName" : payload.userName,
        "userId": payload.userId,
        "url" : getServerURL(),
    }
    return $axios.post(url, body);
}

export function userTokenRefresh({state}, payload){
    const url = '/api/v1/user'

    return $axios({
        method: 'patch',
        url: url,
        data:{
            "userEmail" : payload.email, 
            "token" : payload.token, 
            "url" : getServerURL(),
            "userId" : payload.userId
        }
    })
}
export function deleteUser({state}, payload){
    const url = '/api/v1/user'

    return $axios({
        method: 'delete',
        url: url,
        headers:{
            'auth':  payload.token
        },
        data:{
            "token": payload.token,
            "url": payload.url,
            "userEmail": payload.userEmail,
            "userId": payload.userId,
            "userName": payload.userName,
        }
    })
}
export function userSync({state}, payload){
    const url = '/api/v1/sync'
    const headers = { "auth": payload.token }

    return $axios.get(url, {headers});
}

export function setUserSetting({state}, payload){
    const url = '/api/v1/redis/user'
    const headers = {
        'auth' : payload.token
    }
    const body = {
        'teams' : payload.teams,
        'theme' : payload.theme
    }
    return $axios({
        method: 'post',
        url: url,
        headers:{
            'auth':  payload.token
        },
        data:{
            'teams' : payload.teams,
            'theme' : payload.theme
        }
    })

}

export function getUserSetting({state}, payload){
    const url = '/api/v1/redis/user'
    const headers = {
        'auth' : payload.token
    }

    return $axios.get(url, {headers});
}

// notice API
export function getMonthNotice({state}, payload){
    const url = '/api/v1/notification/month?year=' + payload.year + '&month=' + payload.month
    const headers = { 
        'auth': payload.token 
    }

    return $axios.get(url, {headers});
}

export function getDayNotice({state}, payload){
    const url = '/api/v1/notification/day?year=' + payload.year + '&month=' + payload.month + '&day=' + payload.day
    const headers = {
        'auth': payload.token 
    }
    return $axios.get(url, {headers});
}

export function getNoticeDetail({state}, payload){
    const url = '/api/v1/notification/post/' + payload.postId
    const headers = { "auth": payload.token }
    
    return $axios.get(url, {headers});
}

export function getNoticeSearch({state}, payload){
    const url = '/api/v1/notification/search?word=' + payload.word
    const headers = { "auth": payload.token, "Content-Type" : "application/json;charset=utf-8" }
    
    return $axios.get(url, {headers});
}


export function deleteNotice({state}, payload){
    const url = '/api/v1/notification/delete/' + payload.postId
    return $axios({
        method: 'delete',
        url: url,
        headers:{
            'auth':  payload.token,
        },
    })
}


// bot API
export function uploadNotice({state}, payload){
    const url = '/plugins/com.mattermost.plugin-mbotc/api/v1/create-notification-with-editor'
    return $axios({
        method: 'post',
        url: url,
        headers:{
            'auth':  payload.token,
            'Content-Type': 'multipart/form-data'
        },
        data: payload.notice
    })
}


//File API
export function getFile({state}, payload){
    const url = '/api/v4/files/' + payload.fileId
    return $axios({
        method: 'get',
        url: url,
        headers:{
            "Authorization": "bearer " + payload.token,
            "responseType": "blob"
        }
    })
}

export function getFileLink({state}, payload){
    const url = '/api/v4/files/' + payload.fileId + "/link"
    return $axios({
        method: 'get',
        url: url,
        headers:{
            "Authorization": "bearer " + payload.token,
        }
    })
}

// export function getFileThumbnail({state}, payload){
//     const url = '/api/v4/files/' + payload.fileId + "/thumbnail"
//     const headers = { 
//         "Authorization": "bearer " + payload.token, 
//         "responseType": "arraybuffer"
//     }
    
//     return $axios.get(url, {headers});
// }