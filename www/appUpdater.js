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

    exec(onUpdate, onError, "appUpdateData", "get_data", []);
    console.log('testing 123...');
    alert('testing');
};

module.exports = new appUpdater();
