<?php

namespace App\Providers;

use Illuminate\Support\ServiceProvider;
use App\Services\CustomValidator;
use Illuminate\Support\Facades\Validator;

class AppServiceProvider extends ServiceProvider
{
  /**
   * Bootstrap any application services
   * @return void
   */
  public function boot()
  {
    Validator::resolver(function ($translator, $data, $rules, $messages) {
      return new CustomValidator($translator, $data, $rules, $messages);
    });
  }

  /**
   * Register any application services.
   *
   * @return void
   */
  public function register()
  {
    //
  }
}
