@extends('layouts.master')
@section('assets')
  <style type="text/css">
    .sns-signin {
      margin-top: 20px;
      /*margin-top: 94px;*/
    }

    .sns-signin a {
      margin-bottom: 22px;
      display: block;
      color: #fff;
    }

    .sns-signin a span {
      margin-left: 30px;
    }

    .sns-signin .normal {
      background-color: #3e4764;
      -webkit-transition: background-color 300ms linear;
      -moz-transition: background-color 300ms linear;
      -o-transition: background-color 300ms linear;
      -ms-transition: background-color 300ms linear;
      transition: background-color 300ms linear;
    }

    .sns-signin .normal:hover {
      background-color: #235B97;
    }

    .sns-signin .facebook .fa-user {
      color: #505050;
      font-weight: 300;
      font-size: 20px;
      background-color: #eaeaea;
      padding: 15px 19px 15px 18px;
    }

    .sns-signin .facebook {
      background-color: #3e4764;
      -webkit-transition: background-color 300ms linear;
      -moz-transition: background-color 300ms linear;
      -o-transition: background-color 300ms linear;
      -ms-transition: background-color 300ms linear;
      transition: background-color 300ms linear;
    }

    .sns-signin .facebook:hover {
      background-color: #235B97;
    }

    .sns-signin .facebook .fa-facebook {
      color: #505050;
      font-weight: 300;
      font-size: 20px;
      background-color: #eaeaea;
      padding: 15px 19px 15px 18px;
    }

    .sns-signin .google-plus {
      background-color: #e66060;
      -webkit-transition: background-color 300ms linear;
      -moz-transition: background-color 300ms linear;
      -o-transition: background-color 300ms linear;
      -ms-transition: background-color 300ms linear;
      transition: background-color 300ms linear;
    }

    .sns-signin .google-plus:hover {
      background-color: #EC574B;
    }

    .sns-signin .google-plus .fa-google-plus {
      color: #505050;
      font-weight: 300;
      font-size: 20px;
      background-color: #eaeaea;
      padding: 15px;
    }

    .sns-signin .github {
      background-color: #2b2b2b;
      -webkit-transition: background-color 300ms linear;
      -moz-transition: background-color 300ms linear;
      -o-transition: background-color 300ms linear;
      -ms-transition: background-color 300ms linear;
      transition: background-color 300ms linear;
    }

    .sns-signin .github:hover {
      background-color: #444444;
    }

    .sns-signin .github .fa-github {
      color: #505050;
      font-weight: 300;
      font-size: 20px;
      background-color: #eaeaea;
      padding: 15px;
    }


  </style>
@endsection
@section("content")
  <div class="content">
    <div class="container">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-8">
            <div class="card">
              <div class="header">
                <h4 class="title">Register</h4>
              </div>
              {!! Form::open(['url'=> route('register'),'method'=>'POST']) !!}
              <div class="col-md-66">
                <div class="form-group {{ $errors->has('firstname') ? ' has-error' : '' }}">
                  <label class="col-md-44 control-label">First name</label>
                  <div class="col-md-66">
                    <input type="text" class="form-control" name="firstname" value="{{old('firstname')}}">
                    @if ($errors->has('firstname'))
                      <span class="help-block">
                        <strong>{{ $errors->first('firstname') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>
                <div class="form-group {{ $errors->has('middlename') ? ' has-error' : '' }}">
                  <label class="col-md-44 control-label">Middle name (optional) </label>
                  <div class="col-md-66">
                    <input type="text" class="form-control" name="middlename" value="{{old('middlename')}}">
                    @if ($errors->has('middlename'))
                      <span class="help-block">
                        <strong>{{ $errors->first('middlename') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>
                <div class="form-group {{ $errors->has('lastname') ? ' has-error' : '' }}">
                  <label class="col-md-44 control-label">Last name</label>
                  <div class="col-md-66">
                    <input type="text" class="form-control" name="lastname" value="{{old('lastname')}}">
                    @if ($errors->has('lastname'))
                      <span class="help-block">
                        <strong>{{ $errors->first('lastname') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>
                <div class="form-group {{ $errors->has('id_number') ? ' has-error' : '' }}">
                  <label class="col-md-44 control-label">ID Number</label>
                  <div class="col-md-66">
                    <input type="text" class="form-control" name="id_number" value="{{old('id_number')}}">
                    @if ($errors->has('id_number'))
                      <span class="help-block">
                        <strong>{{ $errors->first('id_number') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>

                <div class="form-group {{ $errors->has('email') ? ' has-error' : '' }}">
                  <label class="col-md-44 control-label">E-Mail Address</label>
                  <div class="col-md-66">
                    <input type="email" class="form-control" name="email" value="{{old('email')}}">
                    @if ($errors->has('email'))
                      <span class="help-block">
                        <strong>{{ $errors->first('email') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>

                <div class="form-group {{ $errors->has('password') ? ' has-error' : '' }}">
                  <label class="col-md-44 control-label">Password</label>
                  <div class="col-md-66">
                    <input type="password" class="form-control" name="password">
                    @if ($errors->has('password'))
                      <span class="help-block">
                        <strong>{{ $errors->first('password') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>

                <div class="form-group {{ $errors->has('password_confirmation') ? ' has-error' : '' }}">
                  <label class="col-md-44 control-label">Confirm Password</label>
                  <div class="col-md-66">
                    <input type="password" class="form-control" name="password_confirmation">
                    @if ($errors->has('password_confirmation'))
                      <span class="help-block">
                        <strong>{{ $errors->first('password_confirmation') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>

                <div class="form-group">
                  <div class="col-md-66 col-md-offset-44">
                    <section title=".squaredTwo">
                      <!-- .squaredTwo -->
                      <div class="squaredTwo">
                        <input type="checkbox" value="None" id="squaredTwo" name="remember" checked/>
                        Remember Me
                        <label for="squaredTwo"></label>
                      </div>
                      <!-- end .squaredTwo -->
                    </section>
                  </div>
                </div>
                <div class="form-group">
                  <div class="col-md-66 col-md-offset-44">
                    <button type="submit" class="btn btn-primary">Login</button>
                    <a class="btn btn-link" href="">Forgot Your Password?</a>
                  </div>
                </div>
              </div>
              {!! Form::close() !!}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
@endsection
