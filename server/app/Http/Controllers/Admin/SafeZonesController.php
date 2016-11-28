<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\AdminController;
use App\Http\Requests\SafeZonesRequest;
use App\SafeZone;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Session;

class SafeZonesController extends AdminController
{
  public function index()
  {
    $title = 'All Safe Zones';
    $safezones = SafeZone::latest()->paginate(10);
    return view('admin.safezones.index', compact('title', 'safezones'));
  }

  public function show($safezone)
  {
    $title = $safezone->name;
    return view('admin.safezones.show', compact('title', 'safezone'));
  }

  public function create()
  {
    $title = 'Create Safe Zones';
    return view('admin.safezones.create',compact('title'));
  }

  public function store(SafeZonesRequest $request)
  {
    SafeZone::create($request->all());
    Session::flash('message', 'You have successfully created a safe zone');
    Session::flash('alert-class', 'alert-success');
    return redirect(route("admin.safezones.all"));
  }

  public function edit($safezone)
  {
    $title = 'Edit ' . $safezone->name;
    return view('admin.safezones.edit', compact('title', 'safezone'));
  }

  public function update(SafeZonesRequest $request, $safezone)
  {
    $safezone->update($request->all());
    Session::flash('message', 'You have successfully Updated a safe zone');
    Session::flash('alert-class', 'alert-success');
    return redirect(route('admin.safezones.show', $safezone->id));
  }

  public function delete(Request $request, $safezone)
  {
    $safeZone->events()->delete();
    $safeZone->comments()->delete();
    $safezone->delete();
    Session::flash('message', 'You have successfully deleted a  safe zone');
    Session::flash('alert-class', 'alert-danger');
    return redirect(route('admin.safezones.all'));
  }

  public function map()
  {
    $title = 'Safe Zones Map';
    $safezones = SafeZone::all();
    return view('admin.safezones.map', compact('title', 'safezones'));
  }
}
