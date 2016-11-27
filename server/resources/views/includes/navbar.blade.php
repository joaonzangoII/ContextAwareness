{{--<nav class="navbar navbar-default">--}}
{{--<div class="container-fluid">--}}
{{--<!-- Brand and toggle get grouped for better mobile display -->--}}
{{--<div class="navbar-header">--}}
{{--<button type="button"--}}
{{--class="navbar-toggle collapsed"--}}
{{--data-toggle="collapse"--}}
{{--data-target="#bs-example-navbar-collapse-1"--}}
{{--aria-expanded="false">--}}
{{--<span class="sr-only">Toggle navigation</span>--}}
{{--<span class="icon-bar"></span>--}}
{{--<span class="icon-bar"></span>--}}
{{--<span class="icon-bar"></span>--}}
{{--</button>--}}
{{--<a class="navbar-brand" href="#">Context Awareness</a>--}}
{{--</div>--}}

{{--<!-- Collect the nav links, forms, and other content for toggling -->--}}
{{--<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">--}}
{{--<ul class="nav navbar-nav">--}}
{{--@if(Auth::check())--}}
{{--<li class="dropdown">--}}
{{--<a href="#"--}}
{{--class="dropdown-toggle"--}}
{{--data-toggle="dropdown"--}}
{{--role="button"--}}
{{--aria-haspopup="true"--}}
{{--aria-expanded="false">Users <span class="caret"></span></a>--}}
{{--<ul class="dropdown-menu">--}}
{{--<li>--}}
{{--<a href="{{route("admin.users.all")}}">--}}
{{--View All--}}
{{--</a>--}}
{{--</li>--}}
{{--</ul>--}}
{{--</li>--}}

{{--<li class="dropdown">--}}
{{--<a href="#"--}}
{{--class="dropdown-toggle"--}}
{{--data-toggle="dropdown"--}}
{{--role="button"--}}
{{--aria-haspopup="true"--}}
{{--aria-expanded="false">Safe Zones <span class="caret"></span></a>--}}
{{--<ul class="dropdown-menu">--}}
{{--<li><a href="{{route("admin.safezones.create")}}">Add New</a></li>--}}
{{--<li>--}}
{{--<a href="{{route("admin.safezones.all")}}">View All </a>--}}
{{--</li>--}}
{{--<li><a href="{{route("admin.safezones.map")}}">View on Map</a></li>--}}
{{--</ul>--}}
{{--</li>--}}

{{--<li class="dropdown">--}}
{{--<a href="#"--}}
{{--class="dropdown-toggle"--}}
{{--data-toggle="dropdown"--}}
{{--role="button"--}}
{{--aria-haspopup="true"--}}
{{--aria-expanded="false">Events <span class="caret"></span></a>--}}
{{--<ul class="dropdown-menu">--}}
{{--<li>--}}
{{--<a href="{{route("admin.events.all")}}">View All </a>--}}
{{--</li>--}}
{{--</ul>--}}
{{--</li>--}}
{{--@endif--}}
{{--</ul>--}}
{{--<form class="navbar-form navbar-left" role="search">--}}
{{--<div class="form-group">--}}
{{--<input type="text" class="form-control" placeholder="Search">--}}
{{--</div>--}}
{{--<button type="submit" class="btn btn-default">Submit</button>--}}
{{--</form>--}}
{{--<ul class="nav navbar-nav navbar-right">--}}
{{--@if(Auth::guest())--}}
{{--<li class="active">--}}
{{--<a href="{{route('login')}}">Login <span class="sr-only">(current)</span></a>--}}
{{--</li>--}}
{{--<li><a href="{{route('register')}}">Register</a></li>--}}
{{--@else--}}
{{--<li class="dropdown">--}}
{{--<a href="#"--}}
{{--class="dropdown-toggle"--}}
{{--data-toggle="dropdown"--}}
{{--role="button"--}}
{{--aria-haspopup="true"--}}
{{--aria-expanded="false">My Account <span class="caret"></span></a>--}}

{{--</li>--}}
{{--@endif--}}
{{--</ul>--}}
{{--</div><!-- /.navbar-collapse -->--}}
{{--</div><!-- /.container-fluid -->--}}
{{--</nav>--}}

<nav class="navbar navbar-default navbar-fixed">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navigation-example-2">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">{{$title}}</a>
    </div>
    <div class="collapse navbar-collapse">
      <ul class="nav navbar-nav navbar-left">
        @if(Auth::check())
          {{--<li class="dropdown">--}}
            {{--<a href="#" class="dropdown-toggle" data-toggle="dropdown">--}}
              {{--<i class="fa fa-globe"></i>--}}
              {{--<b class="caret"></b>--}}
              {{--<span class="notification">{{count(Auth::user()->unreadNotifications)}}</span>--}}
            {{--</a>--}}
            {{--@if(count(Auth::user()->unreadNotifications)>0)--}}
              {{--<ul class="dropdown-menu">--}}
                {{--@foreach (Auth::user()->unreadNotifications as $notification)--}}
                  {{--<li><a href="#">Notification 1</a></li>--}}
                {{--@endforeach--}}
              {{--</ul>--}}
            {{--@endif--}}
          {{--</li>--}}
          <li class="dropdown">
            <a href="#"
               class="dropdown-toggle"
               data-toggle="dropdown"
               role="button"
               aria-haspopup="true"
               aria-expanded="false">Users <span class="caret"></span></a>
            <ul class="dropdown-menu">
              <li>
                <a href="{{route("admin.users.all")}}">
                  View All
                </a>
              </li>
            </ul>
          </li>

          <li class="dropdown">
            <a href="#"
               class="dropdown-toggle"
               data-toggle="dropdown"
               role="button"
               aria-haspopup="true"
               aria-expanded="false">Safe Zones <span class="caret"></span></a>
            <ul class="dropdown-menu">
              @if(Auth::user()->isAdmin())
                <li><a href="{{route("admin.safezones.create")}}">Add New</a></li>
              @endif
              <li>
                <a href="{{route("admin.safezones.all")}}">View All </a>
              </li>
              <li><a href="{{route("admin.safezones.map")}}">View on Map</a></li>
            </ul>
          </li>

          <li class="dropdown">
            <a href="#"
               class="dropdown-toggle"
               data-toggle="dropdown"
               role="button"
               aria-haspopup="true"
               aria-expanded="false">Events <span class="caret"></span></a>
            <ul class="dropdown-menu">
              <li>
                <a href="{{route("admin.events.all")}}">View All </a>
              </li>
            </ul>
          </li>

          <li class="dropdown">
            <a href="#"
               class="dropdown-toggle"
               data-toggle="dropdown"
               role="button"
               aria-haspopup="true"
               aria-expanded="false">Comments <span class="caret"></span></a>
            <ul class="dropdown-menu">
              <li>
                <a href="{{route("admin.comments.all")}}">View All </a>
              </li>
            </ul>
          </li>
        @endif
      </ul>

      <ul class="nav navbar-nav navbar-right">
        <li>
          <a href="{{route("home")}}">
            <p><i class="pe-7s-home"></i> Home</p>
          </a>
        </li>
        @if(Auth::guest())
          <li>
            <a href="{{route('login')}}">
              <p><i class="pe-7s-user"></i> Login</p>
            </a>
          </li>

          <li>
            <a href="{{route('register')}}">
              <p><i class="pe-7s-note"></i> Register</p>
            </a>
          </li>
        @else
          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="pe-7s-user"></i> Account
              <b class="caret"></b>
            </a>
            <ul class="dropdown-menu">
              <li>
                <a href="{{route('admin.users.profile')}}">
                  Profile
                </a>
              </li>
              <li>
                <a href="{{route("logout")}}"
                   onclick="event.preventDefault();
                            document.getElementById('logout-form').submit();">
                  Logout
                </a>

                {!! Form::open(["id"=>"logout-form", "method"=>"POST", "url"=> route("logout"), "style"=>"display: none;"]) !!}
                {!! Form::hidden("type","lecturer") !!}
                {!! Form::close() !!}
              </li>
            </ul>
          </li>
          <li>
            <a href="{{route("logout")}}"
               onclick="event.preventDefault();
                            document.getElementById('logout-form').submit();">
              <i class="pe-7s-power"></i> Logout
            </a>
          </li>
        @endif
      </ul>
    </div>
  </div>
</nav>
