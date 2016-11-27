<?php
namespace App\Http\Controllers\Admin;


use Illuminate\Support\Facades\Auth;

class DashboardController
{
  public function home()
  {
    $title = 'Dashboard';
    $user = Auth::user();
    return view('admin.dashboard', compact('title', 'user'));
  }
}