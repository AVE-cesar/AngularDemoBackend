$output.resource("static/assets/js/events", "events.js")##

'use strict';

app.constant('USER_ROLES', {
    all: '*',
    admin: 'admin',
    user: 'user'
});

app.run(function (${dollar}rootScope, ${dollar}location, ${dollar}http, AuthSharedService, Session, USER_ROLES, ${dollar}q, ${dollar}timeout) {

    ${dollar}rootScope.${dollar}on('${dollar}routeChangeStart', function (event, next) {

        if(next.originalPath === "/login" && ${dollar}rootScope.authenticated) {
            event.preventDefault();
        } else if (next.access && next.access.loginRequired && !${dollar}rootScope.authenticated) {
            event.preventDefault();
            ${dollar}rootScope.${dollar}broadcast("event:auth-loginRequired", {});
        } else if (next.access && !AuthSharedService.isAuthorized(next.access.authorizedRoles)) {
            event.preventDefault();
            ${dollar}rootScope.${dollar}broadcast("event:auth-forbidden", {});
        }
    });

    ${dollar}rootScope.${dollar}on('${dollar}routeChangeSuccess', function (scope, next, current) {
        ${dollar}rootScope.${dollar}evalAsync(function () {
            ${dollar}.material.init();
        });
    });

    // Call when the the client is confirmed
    ${dollar}rootScope.${dollar}on('event:auth-loginConfirmed', function (event, data) {
        console.log('login confirmed start ' + data);
        ${dollar}rootScope.loadingAccount = false;
        var nextLocation = (${dollar}rootScope.requestedUrl ? ${dollar}rootScope.requestedUrl : "/book");
        console.log('nextLocation: ' + nextLocation);
        
        var delay = (${dollar}location.path() === "/loading" ? 1500 : 0);

        ${dollar}timeout(function () {
            Session.create(data);
            ${dollar}rootScope.account = Session;
            ${dollar}rootScope.authenticated = true;
            ${dollar}location.path(nextLocation).replace();
        }, delay);

    });

    // Call when the 401 response is returned by the server
    ${dollar}rootScope.${dollar}on('event:auth-loginRequired', function (event, data) {
    	console.log('event:auth-loginRequired received: ' + data);
        if (${dollar}rootScope.loadingAccount && data.status !== 401) {
            ${dollar}rootScope.requestedUrl = ${dollar}location.path()
            ${dollar}location.path('/loading');
        } else {
            Session.invalidate();
            ${dollar}rootScope.authenticated = false;
            ${dollar}rootScope.loadingAccount = false;
            ${dollar}location.path('/login');
        }
    });

    // Call when the 403 response is returned by the server
    ${dollar}rootScope.${dollar}on('event:auth-forbidden', function (rejection) {
    	console.log('event:auth-forbidden received');
        ${dollar}rootScope.${dollar}evalAsync(function () {
            ${dollar}location.path('/error/403').replace();
        });
    });

    // Call when the user logs out
    ${dollar}rootScope.${dollar}on('event:auth-loginCancelled', function () {
    	console.log('event:auth-loginCancelled received');
        ${dollar}location.path('/login').replace();
    });

    // Get already authenticated user account
    AuthSharedService.getAccount();


});