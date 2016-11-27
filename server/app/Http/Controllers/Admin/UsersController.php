<?php

namespace App\Http\Controllers\Admin;

use App\User;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Input;
use Illuminate\Support\Facades\Session;
use Intervention\Image\Facades\Image;

class UsersController extends Controller
{
  public function index()
  {
    $users = User::where('user_type', 'normal')->paginate(10);
    //$users = User::paginate(10);
    $title = 'All Users';
    return view('admin.users.index', compact('title', 'users'));
  }

  public function show($user)
  {
    $title = $user->full_name;
    return view('admin.users.show', compact('title', 'user'));
  }

  public function profile()
  {
    $user = User::findOrFail(Auth::user()->id);
    $title = "My Profile";
    return view('admin.users.profile', compact('title', 'user'));
  }

  public function updateProfile(Request $request)
  {
    $user = User::findOrFail(Auth::user()->id);
    $user->update($request->all());
    Session::flash('message', 'You have successfully Updated your profile');
    Session::flash('alert-class', 'alert-success');
    return redirect(route('admin.users.profile'));
  }

  public function edit($user)
  {
    $title = 'Edit ' . $user->full_name;
    return view('admin.users.edit', compact('title', 'user'));
  }

  public function update(Request $request, $user)
  {
    $user->update($request->all());
    Session::flash('message', 'You have successfully Updated a user');
    Session::flash('alert-class', 'alert-success');
    return redirect(route('admin.users.show', $user->id));
  }

  public function delete(Request $request, $user)
  {
    $user->delete();
    Session::flash('message', 'You have successfully deleted a  user');
    Session::flash('alert-class', 'alert-danger');
    return redirect(route('admin.users.all'));
  }


  public function updateProfileImage(Request $request,
                                     $user_id)
  {
    $user = Auth::user();
    if ($request->hasFile('profile-image')) {
      $image = Input::file('profile-image');
      $filename = time() . '.' . $image->getClientOriginalExtension();
      $path = public_path('profilepics/' . $filename);
      Image::make($image->getRealPath())->resize(200, 200)->save($path);
      $user->picture_url = "profilepics/" . $filename;
      $user->save();
      Session::flash('message', 'You have successfully Updated your profile picture');
      Session::flash('alert-class', 'alert-success');
    }

    return redirect(route("admin.users.profile", $user_id));
  }
}
