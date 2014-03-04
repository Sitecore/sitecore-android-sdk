scmobile.motion_manager = (function () {

    var Accelerometer = function () {
        this.plugin = new scmobile.utils.ScPlugin('motion_manager');
    }

    Accelerometer.prototype.start = function (onSuccess, onError) {
        this.plugin.exec("start", {}, function (data) {
            var obj = JSON.parse(data);
            onSuccess(obj)
        }, onError)
    }

    Accelerometer.prototype.stop = function () {
        this.plugin.exec("stop", {})
    }

    return {
        Accelerometer: function () {
            return new Accelerometer();
        }
    };

}());
