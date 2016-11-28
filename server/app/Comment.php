<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;
use Cviebrock\EloquentSluggable\Sluggable;

class Comment extends Model
{
  use Notifiable;
  use Sluggable;
  public $fillable = ['title', 'body', 'user_id', 'safe_zone_id', 'slug'];

  public function sluggable()
  {
    return [
      'slug' => [
        'source' => 'title'
      ]
    ];
  }

  public function user()
  {
    return $this->belongsTo(User::class);
  }

  public function safeZone()
  {
    return $this->belongsTo(SafeZone::class);
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
}
