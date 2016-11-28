<nav class="navbar navbar-default navbar-fixed">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button"
              class="navbar-toggle"
              data-toggle="collapse"
              data-target="#navigation-example-2">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      {{-- <a class="navbar-brand" href="#">{{$title}}</a> --}}
    </div>
    <div class="collapse navbar-collapse">
      <ul class="nav navbar-nav navbar-left">
        @if(Auth::check())
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

                {!! Form::open(["id"=>"logout-form",
                                "method"=>"POST",
                                "url"=> route("logout"),
                                "style"=>"display: none;"]) !!}
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
