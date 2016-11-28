<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>@yield('title') Context Awareness</title>
  <link href="{{asset('css/vendor.css')}}" rel="stylesheet" media="screen">
  <script src="{{asset('js/vendor.js')}}" type="text/javascript"></script>
  {{--<link href='http://fonts.googleapis.com/css?family=Roboto:400,300' rel='stylesheet' type='text/css'>--}}
  <link href='https://fonts.googleapis.com/css?family=Roboto:400,700,300' rel='stylesheet' type='text/css'>
  {{--<link href="https://fonts.googleapis.com/css?family=Raleway:400,00" rel="stylesheet" type="text/css">--}}
  @yield('assets')
</head>
<body>
<div class="wrapper">
  @include('includes.sidebar')
  <div class="main-panel">
    @include("includes.navbar")
    @if(Session::has('message'))
      <div
          id="notification"
          data-notify="container"
          class="col-xs-11 col-sm-4 alert {{Session::get('alert-class', 'alert-info')}} alert-with-icon animated fadeInDown"
          role="alert" data-notify-position="top-right"
          style="display: inline-block; margin: 0px auto; position: fixed; transition: all 0.5s ease-in-out; z-index: 1031; top: 20px; right: 20px;">
        <button
            id="btnCloseNotification"
            type="button" aria-hidden="true" class="close" data-notify="dismis"
            style="position: absolute; right: 10px; top: 50%; margin-top: -13px; z-index: 1033;">×
        </button>
        <i data-notify="icon" class="material-icons">notifications</i><span data-notify="title"></span>
        <span data-notify="message">{{Session::get('message')}}</span><a href="#" target="_blank" data-notify="url"></a>
      </div>
    @endif

    {{--@if(Session::has('status'))--}}
      {{--<div--}}
          {{--id="notification"--}}
          {{--data-notify="container"--}}
          {{--class="col-xs-11 col-sm-4 alert {{Session::get('alert-class', 'alert-info')}} alert-with-icon animated fadeInDown"--}}
          {{--role="alert" data-notify-position="top-right"--}}
          {{--style="display: inline-block; margin: 0px auto; position: fixed; transition: all 0.5s ease-in-out; z-index: 1031; top: 20px; right: 20px;">--}}
        {{--<button--}}
            {{--id="btnCloseNotification"--}}
            {{--type="button" aria-hidden="true" class="close" data-notify="dismis"--}}
            {{--style="position: absolute; right: 10px; top: 50%; margin-top: -13px; z-index: 1033;">×--}}
        {{--</button>--}}
        {{--<i data-notify="icon" class="material-icons">notifications</i><span data-notify="title"></span>--}}
        {{--<span data-notify="message">{{Session::get('status')}}</span><a href="#" target="_blank" data-notify="url"></a>--}}
      {{--</div>--}}
    {{--@endif--}}
    @yield('content')
  </div>
</div>
@include("includes.scripts")
</body>
</html>
