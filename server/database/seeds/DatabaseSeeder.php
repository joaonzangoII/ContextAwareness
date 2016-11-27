<?php

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class DatabaseSeeder extends Seeder
{
  /**
   * Run the database seeds.
   *
   * @return void
   */
  public function run()
  {
    $this->command->info('Unguarding models');
    Model::unguard();
    DB::statement('SET FOREIGN_KEY_CHECKS = 0;');
     $this->call(UsersTableSeeder::class);
    $this->call(SafeZonesTableSeeder::class);
    DB::statement('SET FOREIGN_KEY_CHECKS = 1;');
  }
}
