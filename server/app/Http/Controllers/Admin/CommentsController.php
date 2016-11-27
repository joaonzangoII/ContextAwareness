<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\AdminController;
//use App\Http\Requests\CommentsRequest;
use App\Comment;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Session;

class CommentsController extends AdminController
{
  public function index()
  {
    $title = 'All Comments';
    if (Auth::user()->isAdmin()==true) {
      $comments = Comment::with('user', 'safe_zone')
      ->whereHas('safe_zone')
      ->whereHas('user')
      ->latest()
      ->paginate(10);
    } else {
      $comments = Comment::with('user', 'safe_zone')
      ->where('user_id', Auth::user()->id)
      ->latest()
      ->paginate(10);
    }

    return view('admin.comments.index', compact('title', 'comments'));
  }

  public function show($comment)
  {
    $title = $comment->name;
    return view('admin.comments.show', compact('title', 'comment'));
  }

  public function create()
  {
    $title = 'Create Safe Zones';
    return view('admin.comments.create', compact('title'));
  }

  public function store(Request $request)
  {
    Comment::create($request->all());
    Session::flash('message', 'You have successfully created a safe zone');
    Session::flash('alert-class', 'alert-success');
    return redirect(route("admin.comments.all"));
  }

  public function edit($comment)
  {
    $title = 'Edit ' . $comment->name;
    return view('admin.comments.edit', compact('title', 'comment'));
  }

  public function update(Request $request, $comment)
  {
    $comment->update($request->all());
    Session::flash('message', 'You have successfully Updated a comment');
    Session::flash('alert-class', 'alert-success');
    return redirect(route('admin.comments.show', $comment->id));
  }

  public function delete(Request $request, $comment)
  {
    $comment->delete();
    Session::flash('message', 'You have successfully deleted a  comment');
    Session::flash('alert-class', 'alert-danger');
    return redirect(route('admin.comments.all'));
  }
}
