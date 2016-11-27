<?php

namespace App;

use Carbon\Carbon;
use Illuminate\Notifications\Notifiable;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Cviebrock\EloquentSluggable\Sluggable;

class User extends Authenticatable
{
  use Notifiable;
  use Sluggable;

  /**
   * The attributes that are mass assignable.
   *
   * @var array
   */
  protected $fillable = [
    'firstname', 'middlename', 'lastname', 'id_number',
    'user_type', 'last_logged_in_at', 'email',
    'password', 'gender', 'date_of_birth', 'slug'
  ];

  /**
   * Return the sluggable configuration array for this model.
   *
   * @return array
   */
  public function sluggable()
  {
    return [
      'slug' => [
        'source' => 'full_name'
      ]
    ];
  }

  /**
   * The attributes that should be hidden for arrays.
   *
   * @var array
   */
  protected $hidden = [
    'password', 'remember_token',
  ];

  public function events()
  {
    return $this->hasMany(Timeline::class);
  }

  public function comments()
  {
    return $this->hasMany(Comment::class);
  }


  public function getFullNameAttribute()
  {
    return $this->firstname . " " . $this->lastname;
  }

  public function isAdmin()
  {
    return $this->user_type === 'admin';
  }

  public function setPasswordAttribute($password)
  {
    $this->attributes['password'] = bcrypt($password);
  }

  public function setDateOfBirthAttribute()
  {
    $baseYear = 1950;
    $shortYear = substr($this->attributes['id_number'], 0, 2);
    $month = substr($this->attributes['id_number'], 2, 2);
    $day = substr($this->attributes['id_number'], 4, 2);
    $year = 100 + $baseYear + ($shortYear - $baseYear) % 100;
    $this->attributes['date_of_birth'] = Carbon::create($year, $month, $day)->format("Y-m-d");
  }

  public function setGenderAttribute()
  {
    $this->attributes['gender'] = substr($this->attributes['id_number'], 6, 4) >= 5000 ? "male" : "female";
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

  public function getUpdateProfilePictureLinkAttribute()
  {
    return route("admin.users.update.profile.image", $this->id);
  }
}
