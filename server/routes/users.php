<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| USERS Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::get('/users/all', 'Admin\UsersController@index')->name('users.all');
Route::get('/users/create', 'Admin\UsersController@create')->name('users.create');
Route::post('/users/create', 'Admin\UsersController@store')->name('users.store');
Route::get('/users/profile', 'Admin\UsersController@profile')->name('users.profile');
Route::post('/users/profile', 'Admin\UsersController@updateProfile')->name('users.profile.update');
Route::get('/users/show/{user}', 'Admin\UsersController@show')->name('users.show');
Route::get('/users/edit/{user}', 'Admin\UsersController@edit')->name('users.edit');
Route::post('/users/edit/{user}', 'Admin\UsersController@update')->name('users.update');
Route::post('/users/delete/{user}', 'Admin\UsersController@delete')->name('users.delete');
Route::post('/users/profile/image/{user}', 'Admin\UsersController@updateProfileImage')->name('users.update.profile.image');