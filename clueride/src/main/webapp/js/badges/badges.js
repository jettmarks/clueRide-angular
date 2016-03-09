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
            addBadge: addBadge,
            saveBadges: saveBadges,
            getBadges: getBadges,
            hasBadge: hasBadge,
            clearBadges: clearBadges
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

    /** Primarily for testing since most badges will arrive from the REST API. */
    function addBadge(badge) {
        viewModel.badges.push(badge);
    }

    /** Uses the session to tell which user this is. */
    function reloadBadges() {
        // TODO: placeholder for refreshing badges for a given user
        // in the meanwhile, the LoginService handles this using the LoginResource.
    }

    /** Performed when we logout a user. */
    function clearBadges() {
        viewModel.badges = {};
    }

    /**
     * Brute force loop through all badge entries; may make sense to put these in a hash.
     * @param badgeToCheck - Valid list defined in the Java Badge enum.
     * @returns {boolean}
     */
    function hasBadge(badgeToCheck) {
        for (var b in viewModel.badges) {
            var badge = viewModel.badges[b];
            if (badge === badgeToCheck) {
                return true;
            }
        }
        return false;
    }

}(window.angular));