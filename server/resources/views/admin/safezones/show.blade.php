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
              <h4 class="title">Safe Zone</h4>
            </div>
            <div class="content">
              <h1><b>Name: </b>{{$safezone->name}}</h1>
              <h1><b>Latitude: </b>{{$safezone->latitude}}</h1>
              <h1><b>Longitude: </b>{{$safezone->longitude}}</h1>
              <h1><b>Radius: </b>{{$safezone->radius}}</h1>

              <a class="btn btn-danger" href="{{URL::previous()}}">Back</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
@endsection
