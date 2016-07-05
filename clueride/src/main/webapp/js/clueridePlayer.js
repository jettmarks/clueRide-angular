var app = angular.module('clueridePlayer', [
  'ngCookies',
  'ngResource',
  'ngRoute',
  'crPlayer.Team',
  'crPlayer.Gps',
  'crPlayer.LoginModule',
  'crPlayer.BadgesModule',
  'crPlayer.outingDetails',
  'crPlayer.CourseDetails',
  'crPlayer.MenuModule',
  'crPlayer.Bubble',
  'crPlayer.GameState',
  'crPlayer.OutingModule',
  'crPlayer.Invitation',
  'crPlayer.invite',
  'crPlayer.loading',
  'crMap',
  'crPlayer.Location',
  'common.LocationResource',
  'crLocEdit',
  'camera',
  'crPlayer.Status',
  'crPlayer.ClueModule',
  'crPlayer.CourseModule',
  'common.CourseResource',
  'common.ClueResource',
  'ui.bootstrap',
  'mobile-angular-ui',
  'angularFileUpload',

  // touch/drag feature: this is from 'mobile-angular-ui.gestures.js'
  // it is at a very beginning stage, so please be careful if you like to use
  // in production. This is intended to provide a flexible, integrated and and 
  // easy to use alternative to other 3rd party libs like hammer.js, with the
  // final purpose to integrate gestures into default ui interactions like
  // opening sidebars, turning switches on/off ..
  'mobile-angular-ui.gestures'
]);

app.run(function($transform) {
  window.$transform = $transform;
});

// 
// You can configure ngRoute as always, but to take advantage of SharedState location
// feature (i.e. close sidebar on backbutton) you should setup 'reloadOnSearch: false' 
// in order to avoid unwanted routing.
// 
app.config(function($routeProvider, $httpProvider) {
    $routeProvider.when('/',                {templateUrl: 'birdseye.html', reloadOnSearch: false});
    $routeProvider.when('/map',             {templateUrl: 'js/map/map.html', reloadOnSearch: false});
    $routeProvider.when('/logout',          {templateUrl: 'js/login/logout.html', reloadOnSearch: false});
    $routeProvider.when('/location',        {templateUrl: 'js/loc/loc.html', reloadOnSearch: false});
    $routeProvider.when('/locEdit',         {templateUrl: 'js/locEdit/locEditMain.html', reloadOnSearch: false});
    $routeProvider.when('/locEdit/clues',   {templateUrl: 'js/locEdit/clueEdit.html', reloadOnSearch: false});
    $routeProvider.when('/locEdit/edit',    {templateUrl: 'js/locEdit/locEdit.html', reloadOnSearch: false});
    $routeProvider.when('/locEdit/images',  {templateUrl: 'js/locEdit/imageList.html', reloadOnSearch: false});
    $routeProvider.when('/locEdit/newImage',{templateUrl: 'js/locEdit/newImage.html', reloadOnSearch: false});
    $routeProvider.when('/locEdit/estab',   {templateUrl: 'js/locEdit/newImage.html', reloadOnSearch: false});
    $routeProvider.when('/status',          {templateUrl: 'js/status/status.html', reloadOnSearch: false});
    $routeProvider.when('/outing',          {templateUrl: 'js/outing/outingEdit.html', reloadOnSearch: false});
    $routeProvider.when('/invitation',      {templateUrl: 'js/invitation/landing.html', reloadOnSearch: false});
    $routeProvider.when('/ice',             {templateUrl: 'ice.html', reloadOnSearch: false});

    /* Setup interceptor for redirects and other exceptions. */
    $httpProvider.interceptors.push(function() {
        return {
            'response': function(response) {
                "use strict";
                console.log("Caught response: " + response.status + " URL: " + response.config.url);
                return response;
            },
            'responseError': function(response) {
                "use strict";
                console.log("Caught Response Error: " + response.status);
                window.location = "login.html";
                return response;
            }
        };
    });

});
//
// `$touch example`
// 

app.directive('toucharea', ['$touch', function($touch){
  // Runs during compile
  return {
    restrict: 'C',
    link: function($scope, elem) {
      $scope.touch = null;
      $touch.bind(elem, {
        start: function(touch) {
          $scope.touch = touch;
          $scope.$apply();
        },

        cancel: function(touch) {
          $scope.touch = touch;  
          $scope.$apply();
        },

        move: function(touch) {
          $scope.touch = touch;
          $scope.$apply();
        },

        end: function(touch) {
          $scope.touch = touch;
          $scope.$apply();
        }
      });
    }
  };
}]);

//
// `$drag` example: drag to dismiss
//
app.directive('dragToDismiss', function($drag, $parse, $timeout){
  return {
    restrict: 'A',
    compile: function(elem, attrs) {
      var dismissFn = $parse(attrs.dragToDismiss);
      return function(scope, elem){
        var dismiss = false;

        $drag.bind(elem, {
          transform: $drag.TRANSLATE_RIGHT,
          move: function(drag) {
            if( drag.distanceX >= drag.rect.width / 4) {
              dismiss = true;
              elem.addClass('dismiss');
            } else {
              dismiss = false;
              elem.removeClass('dismiss');
            }
          },
          cancel: function(){
            elem.removeClass('dismiss');
          },
          end: function(drag) {
            if (dismiss) {
              elem.addClass('dismitted');
              $timeout(function() { 
                scope.$apply(function() {
                  dismissFn(scope);  
                });
              }, 300);
            } else {
              drag.reset();
            }
          }
        });
      };
    }
  };
});

//
// Another `$drag` usage example: this is how you could create 
// a touch enabled "deck of cards" carousel. See `carousel.html` for markup.
//
app.directive('carousel', function(){
  return {
    restrict: 'C',
    scope: {},
    controller: function() {
      this.itemCount = 0;
      this.activeItem = null;

      this.addItem = function(){
        var newId = this.itemCount++;
        this.activeItem = this.itemCount === 1 ? newId : this.activeItem;
        return newId;
      };

      this.next = function(){
        this.activeItem = this.activeItem || 0;
        this.activeItem = this.activeItem === this.itemCount - 1 ? 0 : this.activeItem + 1;
      };

      this.prev = function(){
        this.activeItem = this.activeItem || 0;
        this.activeItem = this.activeItem === 0 ? this.itemCount - 1 : this.activeItem - 1;
      };
    }
  };
});

app.directive('carouselItem', function($drag) {
  return {
    restrict: 'C',
    require: '^carousel',
    scope: {},
    transclude: true,
    template: '<div class="item"><div ng-transclude></div></div>',
    link: function(scope, elem, attrs, carousel) {
      scope.carousel = carousel;
      var id = carousel.addItem();
      
      var zIndex = function(){
        var res = 0;
        if (id === carousel.activeItem){
          res = 2000;
        } else if (carousel.activeItem < id) {
          res = 2000 - (id - carousel.activeItem);
        } else {
          res = 2000 - (carousel.itemCount - 1 - carousel.activeItem + id);
        }
        return res;
      };

      scope.$watch(function(){
        return carousel.activeItem;
      }, function(){
        elem[0].style.zIndex = zIndex();
      });
      
      $drag.bind(elem, {
        //
        // This is an example of custom transform function
        //
        transform: function(element, transform, touch) {
          // 
          // use translate both as basis for the new transform:
          // 
          var t = $drag.TRANSLATE_BOTH(element, transform, touch);
          
          //
          // Add rotation:
          //
          var Dx    = touch.distanceX, 
              t0    = touch.startTransform, 
              sign  = Dx < 0 ? -1 : 1,
              angle = sign * Math.min( ( Math.abs(Dx) / 700 ) * 30 , 30 );
          
          t.rotateZ = angle + (Math.round(t0.rotateZ));
          
          return t;
        },
        move: function(drag){
          if(Math.abs(drag.distanceX) >= drag.rect.width / 4) {
            elem.addClass('dismiss');  
          } else {
            elem.removeClass('dismiss');  
          }
        },
        cancel: function(){
          elem.removeClass('dismiss');
        },
        end: function(drag) {
          elem.removeClass('dismiss');
          if(Math.abs(drag.distanceX) >= drag.rect.width / 4) {
            scope.$apply(function() {
              carousel.next();
            });
          }
          drag.reset();
        }
      });
    }
  };
});

app.directive('dragMe', ['$drag', function($drag){
  return {
    controller: function($scope, $element) {
      $drag.bind($element, 
        {
          //
          // Here you can see how to limit movement 
          // to an element
          //
          transform: $drag.TRANSLATE_INSIDE($element.parent()),
          end: function(drag) {
            // go back to initial position
            drag.reset();
          }
        },
        { // release touch when movement is outside boundaries
          sensitiveArea: $element.parent()
        }
      );
    }
  };
}]);

/* Main entry point for the application. */
app.controller('MainController', [
        '$rootScope',
        '$scope',
        'GameStateService',
        'LocationService',
        'BadgesService',
        'LoginService',
    function(
        $rootScope,
        $scope,
        GameStateService,
        LocationService,
        BadgesService,
        LoginService
    ) {

        $scope.swiped = function(direction) {
            alert('Swiped ' + direction);
        };

        // User agent displayed in home page
        $scope.userAgent = navigator.userAgent;

        /* Bind the Course to the scope. */
        $scope.course = {};
        GameStateService.setCourseScope($scope);

        /* Bind the Location Service to the Scope. */
        $scope.locations = {};
        LocationService.setLocationScope($scope);
        LocationService.init();

        /* Bind the Badges Service to the scope. */
        $scope.badges = {};
        BadgesService.setBadgeScope($scope);
        LoginService.getBadges();
        GameStateService.getOutingState();

        /* Whether or not the Header/Footer nav bars are shown. */
        $rootScope.showHeaderFooter = true;

    }]
);