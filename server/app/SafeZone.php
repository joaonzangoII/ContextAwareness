<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;
use Cviebrock\EloquentSluggable\Sluggable;
use App\Comment;
use App\Timeline;

class SafeZone extends Model
{
  use Notifiable;
  use Sluggable;
  public $fillable = ['name', 'latitude', 'longitude', 'radius', 'description', 'slug'];

  public function sluggable()
  {
    return [
      'slug' => [
        'source' => 'name'
      ]
    ];
  }

  /**
   * Get the route key for the model.
   *
   * @return string
   */
  public function getRouteKeyName()
  {
    return 'slug';
  }

  public function events()
  {
    return $this->hasMany(Timeline::class);
  }

  public function comments()
  {
    return $this->hasMany(Comment::class);
  }
}
