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
              <h4 class="title">All Safe Zones</h4>
              {{--<p class="category">Here is a subtitle for this table</p>--}}
            </div>
            <div class="content table-responsive table-full-width">
              <table class="table table-hover table-striped">
                <thead>
                <tr>
                  <th>Name</th>
                  <th>Latitude</th>
                  <th>Longitude</th>
                  <th>Radius</th>
                  <th>Created At</th>
                  <th>Actions</th>
                </tr>
                </thead>

                <tbody>
                @foreach($safezones as $safezone)
                  <tr>
                    <td>{{$safezone->name}}</td>
                    <td>{{$safezone->latitude}}</td>
                    <td>{{$safezone->longitude}}</td>
                    <td>{{$safezone->radius}}</td>
                    <td>{{$safezone->created_at}}</td>
                    <td>
                      <a href="{{route('admin.safezones.show', $safezone->id)}}" class="btn btn-default">Show</a>
                      @if($safezone->name !== 'Unsafe Zone')
                        @if(Auth::user()->isAdmin())
                          <a href="{{route('admin.safezones.edit', $safezone->id)}}" class="btn btn-info">Edit</a>
                          {!!Form::open(['method'=> "POST","route" => ["admin.safezones.delete" , $safezone->id],"style"=>"display:inline"]) !!}
                          <a class="btn btn-s btn-danger"
                             type="button"
                             data-toggle="modal"
                             data-target="#confirmDelete"
                             data-title="Delete Safe Zone {{$safezone->name}}"
                             data-message="Are you sure you want to delete this safe zone?">
                            {{--<i class="fa fa-trash-o text-danger"></i>--}}
                            Delete
                          </a>
                          {!! Form::close() !!}
                        @endif
                      @endif
                    </td>
                  </tr>
                @endforeach
                </tbody>
              </table>

              <div class="text-center">
                {!! $safezones->links() !!}
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
