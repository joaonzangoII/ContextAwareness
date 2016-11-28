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
              <h4 class="title">Add a New Safe Zone</h4>
              {{--<p class="category">Here is a subtitle for this table</p>--}}
            </div>
            {!! Form::open(['METHOD'=>'POST','url' => route('admin.safezones.store')]) !!}
            <div class="form-group {{ $errors->has('name') ? ' has-error' : '' }}">
              <label class="col-md-44 control-label">Name</label>
              <div class="col-md-66">
                <input type="text" class="form-control" name="name" value="{{old('name')}}">
                @if ($errors->has('name'))
                  <span class="help-block">
                    <strong>{{ $errors->first('name') }}</strong>
                  </span>
                @endif
              </div>
            </div>

            <div class="form-group {{ $errors->has('latitude') ? ' has-error' : '' }}">
              <label class="col-md-44 control-label">Latitude</label>
              <div class="col-md-66">
                <input type="text" class="form-control" name="latitude" value="{{old('latitude')}}">
                @if ($errors->has('latitude'))
                  <span class="help-block">
                    <strong>{{ $errors->first('latitude') }}</strong>
                  </span>
                @endif
              </div>
            </div>

            <div class="form-group {{ $errors->has('longitude') ? ' has-error' : '' }}">
              <label class="col-md-44 control-label">Longitude</label>
              <div class="col-md-66">
                <input type="text" class="form-control" name="longitude" value="{{old('longitude')}}">
                @if ($errors->has('longitude'))
                  <span class="help-block">
                    <strong>{{ $errors->first('longitude') }}</strong>
                  </span>
                @endif
              </div>
            </div>

            <div class="form-group {{ $errors->has('description') ? ' has-error' : '' }}">
              <label class="col-md-44 control-label" for="description">Description</label>
              <div class="col-md-66">
                <textarea type="text" class="form-control" name="description" value="{{old('description')}}"></textarea>
                @if ($errors->has('description'))
                  <span class="help-block">
                    <strong>{{ $errors->first('description') }}</strong>
                  </span>
                @endif
              </div>
            </div>
            <div class="form-group">
              <div class="col-md-66">
                {!! Form::submit('Submit', ['class'=>'btn btn default']) !!}
                <a class="btn btn-danger" href="{{URL::previous()}}">Back</a>
              </div>
            </div>
            {!! Form::close() !!}
          </div>
        </div>
      </div>
    </div>
  </div>
@endsection
