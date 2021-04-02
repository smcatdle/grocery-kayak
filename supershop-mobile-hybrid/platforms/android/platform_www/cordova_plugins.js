cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/cordova-plugin-sharedpref/www/SharedPref.js",
        "id": "cordova-plugin-sharedpref.SharedPref",
        "pluginId": "cordova-plugin-sharedpref",
        "clobbers": [
            "SharedPref"
        ]
    },
    {
        "file": "plugins/cordova-plugin-whitelist/whitelist.js",
        "id": "cordova-plugin-whitelist.whitelist",
        "pluginId": "cordova-plugin-whitelist",
        "runs": true
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-sharedpref": "0.0.3",
    "cordova-plugin-whitelist": "1.0.0",
    "org.apache.cordova.console": "0.2.13"
}
// BOTTOM OF METADATA
});