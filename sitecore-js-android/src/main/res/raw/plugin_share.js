scmobile.share = function() {
      var buildparams = function(obj){
        var message = obj.text;
        if (obj.urls !== undefined) message += " " + obj.urls.join(" ");
        return {
            text: message,
            imageUrls: obj.imageUrls
        }
      }

  return{
    Email: function () {
          var email = this;

          this.send = function (onSuccess, onError) {
              var plugin = new scmobile.utils.ScPlugin('share');

              var params = {
                  to: email.toRecipients,
                  cc: email.ccRecipients,
                  bcc: email.bccRecipients,
                  subject: email.subject,
                  body: email.messageBody,
                  isHtml: email.isHTML
              }

              plugin.exec("sendEmail", params, onSuccess, onError);
          }
      },

    Social: function () {
          var social = this;
          social.imageUrls = [];

          this.send = function (onSuccess, onError) {
              var plugin = new scmobile.utils.ScPlugin('share');
              plugin.exec("sendSocial", buildparams(social), onSuccess, onError);
          }
    },

    Facebook: function () {
          var obj = this;
          obj.imageUrls = [];

          this.send = function (onSuccess, onError) {
              var plugin = new scmobile.utils.ScPlugin('share');
              plugin.exec("sendSocial", buildparams(obj), onSuccess, onError);
          }
    },

    Tweet: function () {
          var obj = this;
          obj.imageUrls = [];

          this.send = function (onSuccess, onError) {
              var plugin = new scmobile.utils.ScPlugin('share');
              plugin.exec("sendSocial", buildparams(obj), onSuccess, onError);
          }
    }
    }
}();
