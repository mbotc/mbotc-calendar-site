//Input your ServerData Json file location
let serverData = require("./serverData.json");


//Function relative serverData
const getServerData = () =>{
    return serverData
}

const getServerURL = () =>{
    return serverData.SERVER_URL
}

const getServerLock = () =>{
    return serverData.SERVER_LOCK === "true"
}

const getMbotcURL = () =>{
    return serverData.MBOTC_URL
}


//Functions for Data
//get day from Date object
const getDayPicker = (date, month)=>{
    let thisMonth = parseInt(date.substring(5,7))
    if(thisMonth < month){{
        return parseInt(date.substring(8,10) - 50)
    }}else if(thisMonth > month){
        return parseInt(date.substring(8,10) + 50)
    }else{
        return parseInt(date.substring(8,10))
    }
}

//replace some mattermost annotation and substring content to acceptable size
const getTitle = (content)=>{
    let replaceText = content.replace(new RegExp('@here|@HERE|[#*`]|@','g'), '');
    return replaceText.substring(0,20)
}

const getTitleLen = (content, len)=>{
    //console.log(content)
    let replaceText = content.replace(new RegExp('@here|@HERE|[#*`]|@','g'), '');

    if (replaceText.length > len)
        replaceText = replaceText.substring(0, len) + " ...";
    else
        replaceText = replaceText.substring(0, len)
    return replaceText
}

// replace Date object to SQL's time String
const getTime = (date)=>{
    let replaceText = date.substring(0,10) + " " + date.substring(11,16)
    return replaceText
}

exports.getServerData = getServerData;
exports.getServerURL = getServerURL;
exports.getServerLock = getServerLock;
exports.getMbotcURL = getMbotcURL;
exports.getDayPicker = getDayPicker;
exports.getTitle = getTitle;
exports.getTime = getTime;
exports.getTitleLen = getTitleLen;
