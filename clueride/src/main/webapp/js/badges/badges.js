(function (angular) {
    'use strict';

    var viewModel = {
        badges: []
    };

    angular
        .module('crPlayer.BadgesModule', [])
        .controller('BadgesController', BadgesController)
        .service('BadgesService', BadgesService)
    ;

    BadgesController.$inject = [];

    function BadgesController() {
        //var vm = this;

        //vm.exposedObject = {};
        //vm.exposecFunction = exposedFunction;
    }

    function BadgesService() {
        return {
            setBadgeScope: setBadgeScope,
            reloadBadges: reloadBadges,
            saveBadges: saveBadges,
            getBadges: getBadges,
            hasBadge: hasBadge
        };
    }

    function setBadgeScope(scope) {
        viewModel = scope;
    }

    function saveBadges(badges) {
        viewModel.badges = badges;
    }

    function getBadges() {
        return viewModel.badges;
    }

    function reloadBadges(loginName) {
        // TODO: placeholder for refreshing badges for a given user
    }

    /**
     * Brute force loop through all badge entries; may make sense to put these in a hash.
     * @param badgeToCheck - Valid list defined in the Java Badge enum.
     * @returns {boolean}
     */
    function hasBadge(badgeToCheck) {
        for (var b in viewModel.badges) {
            var badge = viewModel.badges[b];
            if (badge.value === badgeToCheck) {
                return true;
            }
        }
        return false;
    }

}(window.angular));