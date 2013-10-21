var scmobile = {};

scmobile.utils = {};

scmobile.utils._construct = function () {

    var plugins = {};
    var redirects = [];
    var currentGuid = Math.floor(Math.random() * 100000);

    this.ScPlugin = function (plugin) {
        this.plugin = plugin;
        this.guid = currentGuid++;
        plugins[this.guid] = this;

        this.exec = function (method, params, successCallback, failCallback) {
            this.successCallback = successCallback;
            this.failCallback = failCallback;

            params = params || {};

            var path = '/' + plugin + '/' + method + '?'
                + 'params=' + encodeURIComponent(JSON.stringify(params));

            var newLocation = 'sc://localhost' + path + '&guid=' + this.guid;
            redirects.push(newLocation);

            setTimeout(function() {
                if (redirects.length > 0) {
                    var location = redirects[0];
                    window.location = location;
                    redirects.splice(0,1);
                }
            }, 150. * redirects.length);
        }

        this.onSuccess = function (message) {
            if (this.successCallback) this.successCallback(message);
        }

        this.onFailure = function (message) {
            if (this.failCallback) this.failCallback(message);
        }

    }

    this.sendSuccess = function (guid, message) {
        var plugin = plugins[guid];
        plugin.onSuccess(decodeURIComponent(message));
    }

    this.sendFailure = function (guid, message) {
        var plugin = plugins[guid];
        plugin.onFailure(message);
    }

    this.sendScmobileReadyEvent = function () {
        var event = document.createEvent('Event');
        event.initEvent('scmobileReady', true, false);
        document.dispatchEvent(event);
    }

}

scmobile.utils._construct();
