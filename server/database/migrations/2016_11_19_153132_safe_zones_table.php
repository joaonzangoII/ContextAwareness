<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class SafeZonesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('safe_zones', function (Blueprint $table) {
          $table->increments('id');
          $table->string('name');
          $table->string('latitude');
          $table->string('longitude');
          $table->string('radius')->default("500");
          $table->text('description')->nullable();
          $table->string('slug');
          $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::drop('safe_zones');
    }
}
