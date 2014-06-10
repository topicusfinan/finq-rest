'use strict';
angular.module('jbehave').controller('CollectionCtrl', ['$scope','$http', function StoryCtrl($scope,$http) {
  $scope.stories = [];

  $scope.loadCollection = function($scope,$label) {    
  /**  $http({method: 'GET', url: 'http://www.mocky.io/v2/5396c57bb85ce45918d73ac6'}).**/
  /** $http({method: 'GET', url: 'http://erwin-finan.finan.local:8080/runner/stories'}).
    success(function(data,status,header,config){
    var storiesResult = JSON.parse(data);
        angular.forEach(storiesResult, function(story) {
          console.log('story = ' + story);
         $scope.stories.push({text:story, done:false});
        });
    }); **/
  var data = '[{"name":"scenario description","steps":["Given a system state","When I do something","Then system is in a different state"]},{"name":"scenario description 2","steps":["Given a system state","When I do something","Then system is in a different state"]}]';

  var storiesResult = JSON.parse(data);
        angular.forEach(storiesResult, function(story) {
          console.log('story = ' + story.name);
          if (!story.done) $scope.stories.push({text:story.name});
        });
  };

  $scope.submitStories = function() {
    // submit data
    console.log('Submitting scenarios ' + $scope.story.done);
  };

}]);
