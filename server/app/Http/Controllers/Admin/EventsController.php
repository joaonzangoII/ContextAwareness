<?php

namespace App\Http\Controllers\Admin;

use App\Timeline;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Session;

class EventsController extends Controller
{
  public function index()
  {
    $title = 'All Events';
    if (Auth::user()->isAdmin()) {
      $events = Timeline::whereHas('safe_zone')
      ->whereHas('user')
      ->latest()
      ->paginate(10);
    } else {
      $events = Timeline::with('user', 'safe_zone')
      ->where('user_id', Auth::user()->id)
      ->latest()
      ->paginate(10);
    }

    return view('admin.events.index', compact('title', 'events'));
  }

  public function show($event)
  {
    $title = $event->name;
    return view('admin.events.show', compact('title', 'event'));
  }

  public function delete(Request $request, $event)
  {
    $event->delete();
    Session::flash('message', 'You have successfully deleted an event');
    Session::flash('alert-class', 'alert-danger');
    return redirect(route('admin.events.all'));
  }
}
