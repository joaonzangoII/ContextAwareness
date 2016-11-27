<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| ADMIN Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/
Route::get('/dashboard', ['uses' => 'Admin\DashboardController@home', 'as' => 'home']);
Route::get('/safe-zones/all', 'Admin\SafeZonesController@index')->name('safezones.all');
Route::get('/safe-zones/map', 'Admin\SafeZonesController@map')->name('safezones.map');
Route::get('/safe-zones/create', 'Admin\SafeZonesController@create')->name('safezones.create');
Route::post('/safe-zones/create', 'Admin\SafeZonesController@store')->name('safezones.store');
Route::get('/safe-zones/show/{safe_zone}', 'Admin\SafeZonesController@show')->name('safezones.show');
Route::get('/safe-zones/edit/{safe_zone}', 'Admin\SafeZonesController@edit')->name('safezones.edit');
Route::post('/safe-zones/edit/{safe_zone}', 'Admin\SafeZonesController@update')->name('safezones.update');
Route::post('/safe-zones/delete/{safe_zone}', 'Admin\SafeZonesController@delete')->name('safezones.delete');

Route::get('/events/all', 'Admin\EventsController@index')->name('events.all');
Route::get('/events/show/{event}', 'Admin\EventsController@show')->name('events.show');
Route::post('/events/delete/{event}', 'Admin\EventsController@delete')->name('events.delete');


Route::get('/comments/all', 'Admin\CommentsController@index')->name('comments.all');
Route::get('/comments/create', 'Admin\CommentsController@create')->name('comments.create');
Route::post('/comments/create', 'Admin\CommentsController@store')->name('comments.store');
Route::get('/comments/show/{comment}', 'Admin\CommentsController@show')->name('comments.show');
Route::get('/comments/edit/{comment}', 'Admin\CommentsController@edit')->name('comments.edit');
Route::post('/comments/edit/{comment}', 'Admin\CommentsController@update')->name('comments.update');
Route::post('/comments/delete/{comment}', 'Admin\CommentsController@delete')->name('comments.delete');
