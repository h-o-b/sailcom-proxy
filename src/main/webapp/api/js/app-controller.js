
/*globals angular*/
"use strict";

angular
.module("app")
.controller("AppController", AppController);

AppController.$inject = ["$scope", "$rootScope", "$http"];

function AppController($scope, $rootScope, $http) {

	// Utilities
	$rootScope.log = function (msg) {
		console.log(msg);
	};

	$rootScope.alert = function (msg) {
		alert(msg);
	};

	$scope.cid = "";

	$scope.httpInfo = {
		isActive: false,
		url: "",
		resultCode: "",
		resultText: "",
		resultHtml: ""
	};

	$scope.startAjax = function(url) {
		$scope.httpInfo.isActive = true;
		$scope.httpInfo.url = url;
		$scope.httpInfo.resultCode = "";
		$scope.httpInfo.resultText = "";
		$scope.httpInfo.resultHtml = "";
	};

	$scope.stopAjax = function(code, isHtml, text) {
		$scope.httpInfo.isActive = false;
		$scope.httpInfo.resultCode = code;
		if (isHtml) {
			$scope.httpInfo.resultHtml = text;
		} else {
			$scope.httpInfo.resultText = text;
		}
	};

	$scope.execRequest = function(url, f) {
		if ($scope.cid) {
			url += (url.indexOf("?") >= 0 ? "&" : "?") + "cid=" + encodeURIComponent($scope.cid);
		}
		$scope.startAjax(url);
		var api = "/sailcom-proxy/" + url;
		$http.get(api).then(function(rsp) {
			$scope.stopAjax(rsp.status + " (" + rsp.statusText + ")", /*isHtml*/ false, JSON.stringify(rsp.data, undefined, 2));
			f && f(rsp);
		}, function(rsp) {
			$scope.stopAjax(rsp.status + " (" + rsp.statusText + ")", /*isHtml*/ rsp.headers("content-type") && rsp.headers("content-type").indexOf("text/html") >= 0, JSON.stringify(rsp.data, undefined, 2));
		});
	};

	$scope.login = function(user, pwd) {
		$scope.execRequest("session/login?user=" + user + "&pwd=" + pwd);
	};

	$scope.logout = function() {
		$scope.execRequest("session/logout");
	};

	$scope.beginCdiConversation = function() {
		$scope.execRequest("test/cdi/beginConversation", function(rsp) {
			$scope.cid = rsp.data.cid;
		});
	};

	$scope.endCdiConversation = function() {
		$scope.execRequest("test/cdi/endConversation", function(rsp) {
			$scope.cid = "";
		});
	};

}
