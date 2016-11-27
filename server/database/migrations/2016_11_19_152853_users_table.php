<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class UsersTable extends Migration
{
  public function up()
  {
    Schema::create('users', function (Blueprint $table) {
      $table->increments('id');
      $table->string('firstname');
      $table->string('middlename')->nullable();
      $table->string('lastname');
      $table->string('id_number');
      $table->string('email')->unique();
      $table->string('picture_url')->default("/profilepics/user.jpg");
      $table->string('password');
      $table->enum('gender', ['male', 'female', 'other'])->default('male');
      $table->string('date_of_birth');
      $table->enum('user_type', ["admin", "normal"])->default("normal");
      $table->rememberToken();
      $table->timestamp("last_logged_in_at")->nullable();
      $table->string('slug');
      $table->timestamps();

    });
  }

  public function down()
  {
    Schema::drop('users');
  }
}
