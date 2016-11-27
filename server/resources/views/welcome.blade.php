@extends('layouts.master')
@section('content')
  <!-- Styles -->
  <style>
    html, body {
      background-color: #fff;
      color: #636b6f;
      font-family: 'Raleway', sans-serif;
      font-weight: 100;
      height: 100vh;
      margin: 0;
    }

    .full-height {
      height: 100vh;
    }

    .flex-center {
      align-items: center;
      display: flex;
      justify-content: center;
    }

    .position-ref {
      position: relative;
    }

    .top-right {
      position: absolute;
      right: 10px;
      top: 18px;
    }

    .content {
      text-align: center;
    }

    .title {
      font-size: 60px;
    }

    .links > a {
      color: #636b6f;
      padding: 0 25px;
      font-size: 12px;
      font-weight: 600;
      letter-spacing: .1rem;
      text-decoration: none;
      text-transform: uppercase;
    }

    .m-b-md {
      margin-bottom: 30px;
    }
  </style>
  </head>
  <body>
  <div class="flex-center position-ref full-height">
    <div class="content">
      <div class="row">
        <div class="title m-b-md">
          <img src="{{asset('logo.jpeg')}}" width="400px" height="400px" alt="logo"/>
        </div>
      </div>
      <div class="title m-b-md">
        Context Awareness
      </div>
      <div class="links">
        <a href="{{asset('contextawareness.apk')}}"> <i class="fa fa-android fa-2x"></i> Download our android
          application</a>
        {{--<a href="https://laracasts.com">Laracasts</a>--}}
        {{--<a href="https://laravel-news.com">News</a>--}}
        {{--<a href="https://forge.laravel.com">Forge</a>--}}
        {{--<a href="https://github.com/laravel/laravel">GitHub</a>--}}
      </div>
    </div>
  </div>
@endsection