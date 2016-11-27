@extends('layouts.master')
@section('content')
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          <div class="card">
            <div class="header">
              <h4 class="title">All Users</h4>
              {{--<p class="category">Here is a subtitle for this table</p>--}}
            </div>
            <div class="content table-responsive table-full-width">
              <table class="table table-hover table-striped">
                <thead>
                <tr>
                  <th>FullName</th>
                  <th>Id Number</th>
                  <th>Email</th>
                  <th>Date of Birth</th>
                  <th>Gender</th>
                </tr>
                </thead>
                <tbody>
                @foreach($users as $user)
                  <tr>
                    <td>{{$user->full_name}}</td>
                    <td>{{$user->id_number}}</td>
                    <td>{{$user->email}}</td>
                    <td>{{$user->date_of_birth}}</td>
                    <td>{{ ucwords($user->gender)}}</td>
                    <td>
                      <a href="{{route('admin.users.show', $user->id)}}" class="btn btn-default">Show</a>
                      @if(Auth::user()->isAdmin())
                        {{--<a href="{{route('admin.users.edit', $user->id)}}" class="btn btn-info">Edit</a>--}}
                        {!!Form::open(['method'=> "POST","route" => ["admin.users.delete" , $user->id],"style"=>"display:inline"]) !!}
                        <a class="btn btn-s btn-danger"
                           type="button"
                           data-toggle="modal"
                           data-target="#confirmDelete"
                           data-title="Delete User {{$user->full_name}}"
                           data-message="Are you sure you want to delete this user?">
                          {{--<i class="fa fa-trash-o text-danger"></i>--}}
                          Delete
                        </a>
                        {!! Form::close() !!}
                      @endif
                    </td>
                  </tr>
                @endforeach
                </tbody>
              </table>

              <div class="text-center">
                {!! $users->links() !!}
              </div>
            </div>
          </div>

          <!-- Modal Dialog -->
          <div class="modal fade"
               id="confirmDelete"
               role="dialog"
               aria-labelledby="confirmDeleteLabel"
               data-keyboard="false"
               data-backdrop="false"
               aria-hidden="true">
            <div class="modal-dialog">
              <div class="modal-content">
                <div class="modal-header">
                  <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                  <h4 class="modal-title">Delete Parmanently</h4>
                </div>
                <div class="modal-body">
                  <p>Are you sure about this ?</p>
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                  <button type="button" class="btn btn-danger" id="confirm">Delete</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
@endsection

@section("scripts")
  <script type="text/javascript">
    //
    <!-- Dialog show event handler -->
    $('#confirmDelete').on('show.bs.modal', function (e) {
      $message = $(e.relatedTarget).attr('data-message');
      $(this).find('.modal-body p').text($message);
      $title = $(e.relatedTarget).attr('data-title');
      $(this).find('.modal-title').text($title);

      // Pass form reference to modal for submission on yes/ok
      var form = $(e.relatedTarget).closest('form');
      $(this).find('.modal-footer #confirm').data('form', form);
    });

    //
    <!-- Form confirm (yes/ok) handler, submits form -->
    $('#confirmDelete').find('.modal-footer #confirm').on('click', function () {
      $(this).data('form').submit();
      // console.log("clicked");

    });
  </script>
@endsection
