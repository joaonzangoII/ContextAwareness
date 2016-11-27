<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class AppController extends Controller
{
  public function home()
  {
    $title = "Context Awareness";
    return view("welcome", compact('title'));
  }
}
