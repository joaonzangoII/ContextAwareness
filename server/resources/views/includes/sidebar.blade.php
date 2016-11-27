<div class="sidebar" data-color="purple" data-image="{{asset('admin/creative-tim-light/img/sidebar-5.jpg')}}">
  <!--
       Tip 1: you can change the color of the sidebar using: data-color="blue | azure | green | orange | red | purple"
       Tip 2: you can also add an image using data-image tag
   -->
  <div class="sidebar-wrapper">
    <div class="logo">
      <a href="{{ route( Auth::guard(null)->check() ? 'admin.home' : 'home')}}" class="simple-text">
        Context Awareness
      </a>
    </div>
    <ul class="nav">
      @if(Auth::guard(null)->check())
        <li class="active">
          <a href="{{route('admin.home')}}">
            <i class="pe-7s-graph"></i>
            <p>Dashboard</p>
          </a>
        </li>
        <li>
          <a href="{{route('admin.users.profile')}}">
            <i class="pe-7s-user"></i>
            <p>User Profile</p>
          </a>
        </li>
      @else
      @endif
    </ul>
  </div>
</div>
