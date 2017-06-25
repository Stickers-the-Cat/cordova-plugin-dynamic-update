var exec = require("cordova/exec");

var appUpdater = function () {
    this.name = "appUpdater";
};


//DynamicUpdate.prototype.deploy = function (onError) {
//    exec(null, onError, "DynamicUpdate", "deploy", []);
//};*/

appUpdater.prototype.update = function (onUpdate, onError) {
    // appUpdateData
    exec(onUpdate, onError, "appUpdateData", "getData", []);
    console.log('testing 123...');
};

appUpdater.prototype.download = function (onSuccess, onError, url) {
    exec(onSuccess, onError, "appDownloader", "download", [{"url": url}]);
};

module.exports = new appUpdater();
