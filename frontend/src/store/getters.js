
export function getTheme(state){
    return state.theme;
}
export function getThemeId(state){
    return state.themeId;
}
export function getUserData(state){
    return state.userData;
}
export function getToken(state){
    return state.userData.token;
}
export function getURL(state){
    return state.userData.url;
}
export function getUserEmail(state){
    return state.userData.userEmail;
}
export function getUserId(state){
    return state.userData.userId;
}
export function getUserName(state){
    return state.userData.userName;
}
export function getColor(state){
    return state.colorPick;
}
