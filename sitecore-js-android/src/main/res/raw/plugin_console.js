function Console() {
    this.log = function (message) {
        var plugin = new scmobile.utils.ScPlugin('console');
        var params = {message: message};
        plugin.exec('log', params);
    }
}
scmobile.console = new Console();
