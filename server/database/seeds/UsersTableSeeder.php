<?php

use App\User;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class UsersTableSeeder extends Seeder
{
  /**
   * Run the database seeds.
   *
   * @return void
   */
  public function run()
  {
    DB::table("users")->truncate();

    User::create([
      'firstname' => 'admin',
      'lastname' => 'admin',
      'id_number' => '8512305392082',
      'email' => 'admin@admin.com',
      'password' => 'admin',
      'gender' => '',
      'date_of_birth' => '',
      'user_type' => 'admin',
      'picture_url' => '/profilepics/smile-profile-picture.jpg',
    ]);

    User::create([
      'firstname' => 'Carlos',
      'lastname' => 'Matlala',
      'id_number' => '9209155443086',
      'email' => 'khutsomatlala3@gmail.com',
      'password' => 'normal',
      'gender' => '',
      'date_of_birth' => '',
      'user_type' => 'normal',
      'picture_url' => '/profilepics/anymouse.jpg',
    ]);
  }
}
