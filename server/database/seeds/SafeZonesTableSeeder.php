<?php

use App\SafeZone;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class SafeZonesTableSeeder extends Seeder
{
  /**
   * Run the database seeds.
   *
   * @return void
   */
  public function run()
  {
    DB::table("safe_zones")->truncate();
    $items = [
      ['name' => 'Unsafe Zone', 'latitude' => '0', 'longitude' => '0'],
      ['name' => 'TUT Soshanguve campus', 'latitude' => '-25.5383104', 'longitude' => '28.103489'],
      ['name' => 'TUT Arcadia Campus', 'latitude' => '-25.744000', 'longitude' => '28.202161'],
      ['name' => 'TUT Main Campus', 'latitude' => '-25.737083', 'longitude' => '28.146270'],
      ['name' => 'UP Hatfield Campus', 'latitude' => '-25.7561672', 'longitude' => '28.231111'],
      ['name' => 'Alberton Civic Centre', 'latitude' => '-26.2203755', 'longitude' => '28.252941'],
      ['name' => 'Union Buildings, South Africa', 'latitude' => '-25.740207', 'longitude' => '28.211989', 'radius' => '200000'],
      ['name' => 'sosha L', 'latitude' => '-25.539481', 'longitude' => '28.095852']
    ];

    foreach ($items as $item) {
      SafeZone::create($item);
    }
    //    DB::table("safe_zones")->insert($data);
  }
}