function Toast() {

    this.showMessage = function(message) {
        var plugin = new scmobile.utils.ScPlugin('toast');
        var params = {message: message};

        plugin.exec('showMessage', params);
    }

}
scmobile.toast = new Toast();
