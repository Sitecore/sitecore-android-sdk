scmobile.google_maps = (function() {

    var GoogleMaps = function() {
        this.plugin = new scmobile.utils.ScPlugin('google_maps');
    }

    GoogleMaps.prototype.addresses = [];

    GoogleMaps.prototype.show = function(onSuccess, onError) {
        var params = {
            drawRoute: this.drawRoute,
            addresses: this.addresses
        }
        this.plugin.exec("show", params, onSuccess, onError);
    }

    return {
        GoogleMaps: function() {
            return new GoogleMaps();
        }
    }
}())