<?php
namespace App\Http\Controllers\Admin;
use App\User;
use App\SafeZone;
use App\Timeline;
use App\Comment;

use Illuminate\Support\Facades\Auth;

class DashboardController
{
  public function home()
  {

    $user = Auth::user();
    if(!$user->isAdmin()){
      $title = 'User {{$user->full_name}}';
      $events = Timeline::where('user_id', $user->id)->latest()->get();
      $comments = Comment::where('user_id', $user->id)->latest()->get();
      return view('admin.user',
             compact('title',
                     'user',
                     'events',
                     'comments'));
    }

    $title = 'Dashboard';
    $users = User::latest();
    $safeZones = SafeZone::latest()->get();
    $events = Timeline::latest()->get();
    $comments = Comment::latest()->get();
    $safeZonesByEvent = SafeZone::withCount('events')->orderBy('events_count', 'desc')->take(8)->get();
    $safeZonesByComment = SafeZone::withCount('comments')->orderBy('comments_count', 'desc')->take(8)->get();
    return view('admin.dashboard',
           compact('title',
                   'user',
                   'users',
                   'safeZones',
                   'events',
                   'comments',
                   'safeZonesByEvent',
                   'safeZonesByComment'));

  }
}
