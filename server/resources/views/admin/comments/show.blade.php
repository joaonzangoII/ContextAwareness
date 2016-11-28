@extends('layouts.master')
@section('content')
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-8">
          <div class="card">
            <div class="header">
              <h4 class="title">Comment</h4>
            </div>
            <div class="content">
              <h1>{{$comment->title}}</h1>
              <h1><b>Created At: </b>{{$comment->created_at}}</h1>
              @if(not_null($comment->user) && not_null($comment->safeZone))
                <h1><b>User: </b>{{$comment->user->full_name}}</h1>
                <h1><b>Place:</b> {{$comment->safeZone->name}}</h1>
              @endif
              <a class="btn btn-danger" href="{{URL::previous()}}">Back</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
@endsection
