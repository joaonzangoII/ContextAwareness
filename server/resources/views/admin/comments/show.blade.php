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
              @if(not_null($comment->user) && not_null($comment->safe_zone))
                <h1>{{$comment->user->full_name}}</h1>
                <h1>{{$comment->safe_zone->name}}</h1>
              @endif
              <a class="btn btn-danger" href="{{URL::previous()}}">Back</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
@endsection