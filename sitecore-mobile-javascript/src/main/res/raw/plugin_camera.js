scmobile.camera = {

    getPicture: function (onSuccess, onError, options) {
        var plugin = new scmobile.utils.ScPlugin('camera');

        plugin.exec("getPicture", options, onSuccess, onError);
    },

    PictureSourceType: {
        PHOTOLIBRARY: 0,
        CAMERA: 1,
        SAVEDPHOTOALBUM: 2
    }
}
