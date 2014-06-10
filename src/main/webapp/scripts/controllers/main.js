'use strict';
angular.module('jbehave').controller('StoryCtrl', ['$scope','$http', 'filterFilter', function StoryCtrl($scope,$http,filterFilter) {
  $scope.stories = [];
  $scope.checkedStories = [];
  $scope.scenarios = [];

  $scope.loadStories = function() {    
   $http({method: 'GET', url: '/runner/api/stories'}).
    success(function(data,status,header,config){
      console.log(data);
    var storiesResult = data;
        angular.forEach(storiesResult, function(story) {
          console.log('story = ' + story);
         $scope.stories.push({text:story.name});
        });
    }); 
  };

  $scope.loadScenarios = function($story) {    
   $http({method: 'GET', url: '/runner/api/stories/' + $story + '/scenarios'}).
    success(function(data,status,header,config){
      console.log(data);
    var scenariosResult = data;
        angular.forEach(scenariosResult, function(scenario) {
          console.log('scenario = ' + scenario);
         $scope.scenarios.push({text:scenario.name});
        });
    }); 
  };
 
  $scope.submitStories = function() {
    // submit data
    console.log('Submitting scenarios ' + $scope.checkedStories);
  };
  
  $scope.selectStory = function(story) {
  
    if ($scope.checkedStories.indexOf(story) != -1) return;
    $scope.checkedStories.push(story);
    console.log('Submitting story ' + story);
  }
}]);

angular.module('jbehave').directive('checkList', function() {
  return {
    scope: {
      list: '=checkList',
      value: '@'
    },
    link: function(scope, elem, attrs) {
      var handler = function(setup) {
        var checked = elem.prop('checked');
        var index = scope.list.indexOf(scope.value);

        if (checked && index == -1) {
          if (setup) elem.prop('checked', false);
          else scope.list.push(scope.value);
        } else if (!checked && index != -1) {
          if (setup) elem.prop('checked', true);
          else scope.list.splice(index, 1);
        }
      };
      
      var setupHandler = handler.bind(null, true);
      var changeHandler = handler.bind(null, false);
            
      elem.on('change', function() {
        scope.$apply(changeHandler);
      });
      scope.$watch('list', setupHandler, true);
    }
   };
});