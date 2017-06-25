var exec = require("cordova/exec");

var appUpdater = function () {
    this.name = "appUpdater";
};

/*DynamicUpdate.prototype.download = function (onSuccess, onError, url) {
    exec(onSuccess, onError, "DynamicUpdate", "download", [{"url": url}]);
};

DynamicUpdate.prototype.deploy = function (onError) {
    exec(null, onError, "DynamicUpdate", "deploy", []);
};*/

appUpdater.prototype.update = function (onUpdate) {
    exec(onUpdate, null, "appUpdateData", "get_data", []);
};

module.exports = new appUpdater();
