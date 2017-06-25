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

appUpdater.prototype.update = function (onUpdate, onError) {

    exec(onUpdate, onError, "appUpdateData", "getData", []);
    console.log('testing 123...');
};

module.exports = new appUpdater();
