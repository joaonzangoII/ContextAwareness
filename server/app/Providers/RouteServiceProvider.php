<?php

namespace App\Providers;

use App\Comment;
use App\Timeline;
use App\User;
use App\SafeZone;
use Illuminate\Routing\Router;
use Illuminate\Support\Facades\Route;
use Illuminate\Foundation\Support\Providers\RouteServiceProvider as ServiceProvider;

class RouteServiceProvider extends ServiceProvider
{
  /**
   * This namespace is applied to your controller routes.
   *
   * In addition, it is set as the URL generator's root namespace.
   *
   * @var string
   */
  protected $namespace = 'App\Http\Controllers';

  /**
   * Define your route model bindings, pattern filters, etc.
   *
   * @return void
   */
  public function boot()
  {
    parent::boot();
    $router = $this->app->make(Router::class);
    $router->model('safe_zone', SafeZone::class);
    $router->model('user', User::class);
    $router->model('comment', Comment::class);
    $router->model('event', Timeline::class);

    $router->bind('safe_zone', function ($slug) {
      return SafeZone::findOrFail($slug);
    });

    $router->bind('user', function ($slug) {
      return User::with('comments')->findOrFail($slug);
    });

    $router->bind('comment', function ($slug) {
      return Comment::with('user')->findOrFail($slug);
    });

    $router->bind('event', function ($slug) {
      return Timeline::findOrFail($slug);
    });
  }

  /**
   * Define the routes for the application.
   *
   * @return void
   */
  public function map()
  {
    $this->mapAdminRoutes();

    $this->mapUsersRoutes();

    $this->mapApiRoutes();

    $this->mapWebRoutes();


    //
  }


  /**
   * Define the "admin" routes for the application.
   *
   * These routes are typically stateless.
   *
   * @return void
   */
  protected function mapAdminRoutes()
  {
    Route::group([
      'middleware' => ['web', 'auth'],
      'namespace' => $this->namespace,
      'as' => 'admin.',
      'prefix' => 'admin',
    ], function ($router) {
      require base_path('routes/admin.php');
    });
  }


  /**
   * Define the "admin" routes for the application.
   *
   * These routes are typically stateless.
   *
   * @return void
   */
  protected function mapUsersRoutes()
  {
    Route::group([
      'middleware' => ['web', 'auth'],
      'namespace' => $this->namespace,
      'as' => 'admin.',
      'prefix' => 'admin',
    ], function ($router) {
      require base_path('routes/users.php');
    });
  }

  /**
   * Define the "web" routes for the application.
   *
   * These routes all receive session state, CSRF protection, etc.
   *
   * @return void
   */
  protected function mapWebRoutes()
  {
    Route::group([
      'middleware' => 'web',
      'namespace' => $this->namespace,
    ], function ($router) {
      require base_path('routes/web.php');
    });
  }

  /**
   * Define the "api" routes for the application.
   *
   * These routes are typically stateless.
   *
   * @return void
   */
  protected function mapApiRoutes()
  {
    Route::group([
      'middleware' => 'web',
      'namespace' => $this->namespace,
      'prefix' => 'api',
    ], function ($router) {
      require base_path('routes/api.php');
    });
  }
}
