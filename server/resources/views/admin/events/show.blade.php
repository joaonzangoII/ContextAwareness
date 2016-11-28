@extends('layouts.master')
@section('title')
  {{$title}} |
@endsection
@section('content')
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-8">
          <div class="card">
            <div class="header">
              <h4 class="title">Event</h4>
            </div>
            <div class="content">
              <h1><b>Title: </b>{{$event->title}}</h1>
              <h1><b>Created At: </b>{{$event->created_at}}</h1>
              @if(not_null($event->user) && not_null($event->safeZone))
                <h1><b>User: </b>{{$event->user->full_name}}</h1>
                <h1><b>Place: </b>{{$event->safeZone->name}}</h1>
              @endif

              <a class="btn btn-danger" href="{{URL::previous()}}">Back</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
@endsection
