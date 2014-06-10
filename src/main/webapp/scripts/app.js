'use strict';
angular.module('jbehave', ['ngRoute']).config(function($routeProvider) {
  return $routeProvider.when('/', {
    templateUrl: 'stories/main.html',
    controller: 'StoryCtrl'
  }).otherwise({
    redirectTo: '/'
  });
});
