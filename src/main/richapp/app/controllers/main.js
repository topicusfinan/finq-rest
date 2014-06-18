'use strict';
angular.module('jbehave').controller('StoryCtrl', ['$scope','$http', 'filterFilter', function StoryCtrl($scope,$http,filterFilter) {
  $scope.stories = [];
  $scope.checkedStories = [];

  $scope.loadStories = function() {    
   $http({method: 'GET', url: '/api/stories'}).
    success(function(data,status,header,config){
      console.log(data);
    var storiesResult = data;
        angular.forEach(storiesResult, function(story) {
          console.log('story = ' + story);
         $scope.stories.push({text:story.name});
         $scope.stories.push({scenarios:story.scenarios});

        });
    }); 
  };


  $scope.loadScenarios = function(story) {    
   console.log('load scenarios for story = ' + story);
      angular.forEach(story.scenarios, function(scenario) {
        console.log('scenario = ' + scenario);
       $scope.scenarios.push({text:scenario.name});
      });

  };





  $scope.submitStories = function() {
    // submit data
    console.log('Submitting scenarios ' + $scope.checkedStories);
  };
  
  $scope.selectStory = function(story) {
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