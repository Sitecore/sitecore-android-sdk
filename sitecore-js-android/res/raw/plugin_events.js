scmobile.events = function () {

    var Event = function (properties) {
        this.plugin = new scmobile.utils.ScPlugin('events');
        if (properties !== undefined) {
            this.title = properties.title;
            this.notes = properties.notes;
            this.location = properties.location;
            this.alarm = properties.alarm;
            this.startDate = properties.startDate;
            this.endDate = properties.endDate;
        }
    };

    Event.prototype.save = function(onSuccess, onError) {
        var startDate = new Date(this.startDate);
        var endDate = new Date(this.endDate);

        var params = {
            title: this.title,
            notes: this.notes,
            location: this.location,
            alarm: this.alarm,
            startDate: startDate.getTime(),
            endDate: endDate.getTime()
        };

        this.plugin.exec("save", params, onSuccess, onError);
    };

    return {

        create: function (properties) {
            return new Event(properties);
        }
    }
}();
