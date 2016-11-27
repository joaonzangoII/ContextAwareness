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
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-4">
          <div class="card card-user">
            <div class="image">
              <img src="https://ununsplash.imgix.net/photo-1431578500526-4d9613015464?fit=crop&fm=jpg&h=300&q=75&w=400"
                   alt="..."/>
            </div>
            <div class="content">
              <div class="author">
                <a href="#">
                  <img id="profile-image" class="avatar border-gray"
                       src="{{asset($user->picture_url)}}"
                       alt="..."/>

                  <div id="profile-image-caption" style="display:none" class="desc">
                    <p class="desc_content">Click on image to Update profile picture</p>
                  </div>

                  <h4 class="title">{{ucwords($user->full_name)}}<br/>
                    <small>{{$user->slug}}</small>
                  </h4>
                </a>
              </div>
              <p class="description text-center">{{$user->about_me}}</p>
              <p class="description text-center">{{$user->date_of_birth}}</p>
              <p class="description text-center">{{Str::ucFirst($user->gender)}}</p>
            </div>
            <hr>
            <div class="text-center">
              <button href="#" class="btn btn-simple"><i class="fa fa-facebook-square"></i></button>
              <button href="#" class="btn btn-simple"><i class="fa fa-twitter"></i></button>
              <button href="#" class="btn btn-simple"><i class="fa fa-google-plus-square"></i></button>

            </div>
          </div>
        </div>

        <div class="col-md-8">
          <div class="card">
            <div class="header">
              <h4 class="title">Edit User {{ucwords($user->full_name)}}</h4>
            </div>
            <div class="content">
              {!! Form::model($user, ['url'=> route('admin.users.profile.update'),'method'=>'POST']) !!}
              <div class="col-md-666">
                <div class="form-group {{ $errors->has('firstname') ? ' has-error' : '' }}">
                  <label class="col-md-44 control-label">First name</label>
                  <div class="col-md-66">
                    <input type="text" class="form-control" name="firstname" value="{{$user->firstname}}">
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
                    <input type="text" class="form-control" name="middlename" value="{{$user->middlename}}">
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
                    <input type="text" class="form-control" name="lastname" value="{{$user->lastname}}">
                    @if ($errors->has('lastname'))
                      <span class="help-block">
                        <strong>{{ $errors->first('lastname') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>
                <div class="form-group {{ $errors->has('email') ? ' has-error' : '' }}">
                  <label class="col-md-44 control-label">E-Mail Address</label>
                  <div class="col-md-66">
                    <input type="email" class="form-control" name="email" value="{{$user->email}}">
                    @if ($errors->has('email'))
                      <span class="help-block">
                        <strong>{{ $errors->first('email') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>
                <div class="form-group {{ $errors->has('id_number') ? ' has-error' : '' }}">
                  <label class="col-md-44 control-label">ID Number</label>
                  <div class="col-md-66">
                    <input type="text" class="form-control" name="id_number" value="{{$user->id_number}}">
                    @if ($errors->has('id_number'))
                      <span class="help-block">
                        <strong>{{ $errors->first('id_number') }}</strong>
                      </span>
                    @endif
                  </div>
                </div>
                {{--<div class="form-group {{ $errors->has('password') ? ' has-error' : '' }}">--}}
                {{--<label class="col-md-44 control-label">Password</label>--}}
                {{--<div class="col-md-66">--}}
                {{--<input type="password" class="form-control" name="password">--}}
                {{--@if ($errors->has('password'))--}}
                {{--<span class="help-block">--}}
                {{--<strong>{{ $errors->first('password') }}</strong>--}}
                {{--</span>--}}
                {{--@endif--}}
                {{--</div>--}}
                {{--</div>--}}
              </div>
              {!! Form::submit('Submit', ['class'=>'btn btn default']) !!}
              <a class="btn btn-danger" href="{{URL::previous()}}">Back</a>
              {!! Form::close() !!}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div id="myModal" class="modal fade" data-keyboard="false" data-backdrop="false">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">Update Profile Picture</h4>
        </div>
        <div class="modal-body">
          {!! Form::open(['id'=>'image_form', 'url' => $user->update_profile_picture_link, 'files' => true, 'method' => 'POST']) !!}
          <div class="form-group">
            <label for="profile-image-upload"><strong>Choose a file</strong></label>
            {!! Form::file('profile-image',
            ['id'=>'profile-image-upload' , 'class'=>'filestyle','accept'=>'image/*', 'caption'=>"{count} files selected"]) !!}
          </div>
          {!! Form::close() !!}
        </div>
        <div class="modal-footer">
          <button id="close_modal" class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
          <button id="modal_review_btn" class="btn btn-primary">Update</button>
        </div>
      </div>
    </div>
  </div>
@endsection

@section("scripts")
  <script type="text/javascript">
    $(document).ready(function () {
      $('#profile-image').on('click', function ($ev) {
        $ev.preventDefault();
        $('#myModal').modal('show');
        document.location.href = "#content";
      });

      $("input[name='profile-image']").change(function () {
        console.log($(this).val());
      });

      $('#profile-image').hover(function () {
        //$('#profile-image-caption').show();
        $('#profile-image-caption').slideToggle("slow");
      });
      $('#profile-image').mouseleave(function () {
        $('#profile-image-caption').hide();
        // $('#profile-image-caption').slideToggle( "slow" );
      });

      $('#close_modal').on('click', function (ev) {
        $('#myModal').modal('hide');
      })

      $('#modal_review_btn').on('click', function ($ev) {
        $ev.preventDefault();
        $('#image_form').submit();
      });

      var inputs = document.querySelectorAll('.inputfile');
      Array.prototype.forEach.call(inputs, function (input) {
        var label = input.nextElementSibling,
            labelVal = label.innerHTML;

        input.addEventListener('change', function (e) {
          var fileName = '';
          if (this.files && this.files.length > 1)
            fileName = ( this.getAttribute('caption') || '' ).replace('{count}', this.files.length);
          else
            fileName = e.target.value.split('\\').pop();

          if (fileName)
            label.querySelector('span').innerHTML = fileName;
          else
            label.innerHTML = labelVal;
        });
      });
    });
  </script>
@append
