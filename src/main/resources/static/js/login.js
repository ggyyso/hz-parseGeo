/*
 *  天地图平台集成全局变量
 */
var TDT = {
	//天地图单点登录服务地址
	SSO_URL: 'https://sso.tianditu.gov.cn/login',
	//本应用系统域名地址
	HOME_URL: 'http://shaanxi.tianditu.gov.cn/portal',
	//本应用系统中web.xml引入客户端库拦截器配置的地址
	HOME_LOGIN: '/login',
	//获得用户信息，在本应用系统中web.xml引入客户端库拦截器配置的地址。
	HOME_USERINFO: '/user/userInfo'
};

/*
 *  天地图平台集成接口
 */
var T_Sso = function(config){
	this.config = config || null;
	this.callback = this.config != null ? typeof this.config.callback != 'undefined' ? this.config.callback : null : null;
};
T_Sso.prototype = {
	//初始化
	init: function(){
		this.loginSso();
	},
	//判断用户是否登录到系统
	loginSso: function(){
		if(this.callback == null || this.callback == ''){
			return false;
		}
		var url = TDT.SSO_URL + '?action=console&service=' + TDT.HOME_URL + TDT.HOME_USERINFO + '?callback=' + this.callback;
		$.ajax({
	        type : 'get',
	        async:false,
	        url : url,
	        dataType : 'jsonp',
	        jsonp: 'callback',
	        jsonpCallback: 'formLogin',
	        success : function(data){
				console.log("test");
			}
	    });
	}
};

//全局的ajax访问，处理ajax清求时session超时
$.ajaxSetup({
	contentType: 'application/x-www-form-urlencoded;charset=utf-8',
	complete: function(XMLHttpRequest,textStatus){
		//通过XMLHttpRequest取得响应头，logined，
		var logined = XMLHttpRequest.getResponseHeader('logined');
		if(logined == 'logout'){
			alert('登录信息已过期');
			window.location.reload();
		}
	}
});

