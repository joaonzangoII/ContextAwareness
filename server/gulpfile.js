const elixir = require('laravel-elixir');

require('laravel-elixir-vue');

/*
 |--------------------------------------------------------------------------
 | Elixir Asset Management
 |--------------------------------------------------------------------------
 |
 | Elixir provides a clean, fluent API for defining some basic Gulp tasks
 | for your Laravel application. By default, we are compiling the Sass
 | file for our application, as well as publishing vendor resources.
 |
 */

bowerDir = 'resources/assets/bower/',
    applicationDir = 'public/admin/',
    applicationCss = [
        // applicationDir + 'css/material-dashboard.css',
        applicationDir + 'creative-tim-light/css/animate.min.css',
        applicationDir + 'creative-tim-light/css/light-bootstrap-dashboard.css',
        applicationDir + 'creative-tim-light/css/pe-icon-7-stroke.css',
        // applicationDir + 'creative-tim-light/css/demo.css',
        applicationDir + 'css/app.css',
        // applicationDir + 'css/material_icons.css',
        // applicationDir + 'css/jquery.datetimepicker.css',
    ],
    applicationJs = [
        // applicationDir + 'js/app.js',
        applicationDir + 'creative-tim-light/js/bootstrap-checkbox-radio-switch.js',
        applicationDir + 'creative-tim-light/js/chartist.min.js',
        applicationDir + 'creative-tim-light/js/bootstrap-notify.js',
        applicationDir + 'creative-tim-light/js/light-bootstrap-dashboard.js',
        // applicationDir + 'js/material-dashboard.js',
        // applicationDir + 'js/bootstrap-sortable.js',
        applicationDir + 'js/moment.min.js',
        // applicationDir + 'js/jquery.datetimepicker.js',
        // applicationDir + 'js/bootstrap-filestyle.min.js',
    ],
    stylesCss = [
        bowerDir + 'bootstrap/dist/css/bootstrap.css',
        bowerDir + 'font-awesome/css/font-awesome.css',
        bowerDir + 'jquery-ui/themes/base/jquery-ui.css',
    ],
    scriptsJs = [
        bowerDir + 'jquery/dist/jquery.js',
        // bowerDir + 'jquery-ui/jquery-ui.js',
        bowerDir + 'bootstrap/dist/js/bootstrap.js',
    ];

elixir(function (mix) {
    mix
        .styles(stylesCss, 'public/css/bower_assets.css', bowerDir)
        .styles(applicationCss, 'public/css/application_assets.css', applicationDir)
        .styles([
            'public/css/bower_assets.css',
            'public/css/application_assets.css',
        ], 'public/css/vendor.css', 'public')
        .scripts(scriptsJs, 'public/js/bower_assets.js', bowerDir)
        .scripts(applicationJs, 'public/js/application_assets.js', applicationDir)
        .styles([
            'public/js/bower_assets.js',
            'public/js/application_assets.js',
        ], 'public/js/vendor.js', 'public')
        .copy(bowerDir + 'font-awesome/fonts', 'public/fonts')
        .copy(applicationDir + 'creative-tim-light/fonts', 'public/fonts');
    // .copy(bowerDir + 'materialize/dist/fonts', 'public/fonts');
});