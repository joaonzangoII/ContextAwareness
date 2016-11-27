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
@section('content')
  <div class="content">
    <div class="container">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-8">
            <div class="card">
              <div class="header">
                <h4 class="title">Reset Password</h4>
              </div>
              @if (session('status'))
                <div class="alert alert-success">
                  {{ session('status') }}
                </div>
              @endif
              <form class="form-horizontal" role="form" method="POST" action="{{ url('/password/email') }}">
                {{ csrf_field() }}
                <div class="form-group{{ $errors->has('email') ? ' has-error' : '' }}">
                  <label for="email" class="col-md-4 control-label">E-Mail Address</label>

                  <div class="col-md-6">
                    <input id="email" type="email" class="form-control" name="email" value="{{ old('email') }}">

                    @if ($errors->has('email'))
                      <span class="help-block">
                                        <strong>{{ $errors->first('email') }}</strong>
                                    </span>
                    @endif
                  </div>
                </div>
                <div class="form-group">
                  <div class="col-md-6 col-md-offset-4">
                    <button type="submit" class="btn btn-primary">
                      Send Password Reset Link
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
@endsection
