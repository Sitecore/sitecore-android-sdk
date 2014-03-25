scmobile.notification = {

    alert: function (title, message, callback, buttons) {
        var plugin = new scmobile.utils.ScPlugin('notification');

        var params = {
            title: title,
            message: message,
            buttons: buttons
        }

        plugin.exec('alert', params, callback);
    }
}
