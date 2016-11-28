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
              <h4 class="title">User</h4>
            </div>
            <div class="content">
              <h1><b>Fullname: </b>{{$user->full_name}}</h1>
              <h1><b>ID Number: </b>{{$user->id_number}}</h1>
              <h1><b>Gender: </b>{{Str::ucFirst($user->gender)}}</h1>
              <h1><b>Date Of Birth: </b>{{$user->date_of_birth}}</h1>

              @if(count($user->comments) > 0)
                <h4>Comments</h4>
                <div class="content table-responsive table-full-width">
                  <table class="table table-hover table-striped">
                    <thead>
                    <tr>
                      <th>Title</th>
                      <th>Posted at</th>
                    </tr>
                    </thead>
                    @foreach($user->comments as $comment)
                      <tr>
                        <td>{{$comment->title}}</td>
                        <td>{{$comment->created_at->diffForHumans()}}</td>
                      </tr>
                    @endforeach
                  </table>
                </div>
              @endif

              @if(count($user->events) > 0)
                <h4>Events</h4>
                <div class="content table-responsive table-full-width">
                  <table class="table table-hover table-striped">
                    <thead>
                    <tr>
                      <th>Title</th>
                      <th>Posted at</th>
                    </tr>
                    </thead>
                    @foreach($user->events as $event)
                      <tr>
                        <td>{{$event->title}}</td>
                        <td>{{$event->created_at->diffForHumans()}}</td>
                      </tr>
                    @endforeach
                  </table>
                </div>
              @endif

              <a class="btn btn-danger" href="{{URL::previous()}}">Back</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
@endsection
