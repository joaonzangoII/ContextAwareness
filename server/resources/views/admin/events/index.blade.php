@extends('layouts.master')
@section('title')
  {{$title}} |
@endsection
@section('content')
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          <div class="card">
            <div class="header">
              <h4 class="title">All Events</h4>
              {{--<p class="category">Here is a subtitle for this table</p>--}}
            </div>
            <div class="content table-responsive table-full-width">
              @if(count($events) > 0)
                <table class="table table-hover table-striped">
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>User</th>
                      <th>Safezone</th>
                      <th>Time</th>
                      <th>Actions</th>
                    </tr>
                  </thead>

                  <tbody>
                    @foreach($events as $event)
                      @if(not_null($event->user) && not_null($event->safeZone))
                        <tr>
                          <td>{{$event->title}}</td>
                          <td>{{$event->user->full_name}}</td>
                          <td>{{$event->safeZone->name}}</td>
                          <td>{{$event->created_at}}</td>
                          <td>
                            <a href="{{route('admin.events.show', $event->id)}}" class="btn btn-default">Show</a>
                            {{--<a href="{{route('admin.events.edit', $event->id)}}" class="btn btn-info">Edit</a>--}}
                            {!!Form::open(['method'=> "POST","route" => ["admin.events.delete" , $event->id],"style"=>"display:inline"]) !!}
                            <a class="btn btn-s btn-danger"
                               type="button"
                               data-toggle="modal"
                               data-target="#confirmDelete"
                               data-title="Delete Safe Zone"
                               data-message="Are you sure you want to delete this safe zone ?">
                              {{--<i class="fa fa-trash-o text-danger"></i>--}}
                              Delete
                            </a>
                            {!! Form::close() !!}
                          </td>
                        </tr>
                      @endif
                    @endforeach
                  </tbody>
                </table>

                <div class="text-center">
                  {!! $events->links() !!}
                </div>
              @else
                <div class="text-center">
                  <h1>NO EVENTS FOUND</h1>
                </div>
              @endif
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
@endsection
