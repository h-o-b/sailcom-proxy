
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

	$scope.httpInfo = {
		isActive: false,
		url: "",
		resultCode: "",
		resultText: "",
		resultHtml: ""
	};

	$scope.startAjax = function(url) {
		$scope.httpInfo.isActive = true;
//		jQuery("#spinner").show();
		$scope.httpInfo.url = url;
		$scope.httpInfo.resultCode = "";
		$scope.httpInfo.resultText = "";
		$scope.httpInfo.resultHtml = "";
	};

	$scope.stopAjax = function(code, isHtml, text) {
		$scope.httpInfo.isActive = false;
//		jQuery("#spinner").hide();
		$scope.httpInfo.resultCode = code;
		if (isHtml) {
			$scope.httpInfo.resultHtml = text;
		} else {
			$scope.httpInfo.resultText = text;
		}
	};

	$scope.execRequest = function(url, f) {
		$scope.startAjax(url);
		var api = "http://hannes-brunner.no-ip.org/sailcom-proxy/" + url;
/*
		if ($scope.sessionInfo.session) {
			api += (api.indexOf("?") >= 0 ? "&" : "?") + "session=" + encodeURIComponent($scope.sessionInfo.session);
		}
*/
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

}
