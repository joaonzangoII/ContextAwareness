@extends('layouts.master')
@section('content')
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          <div class="card">
            <div class="header">
              <h4 class="title">All Comments</h4>
              {{--<p class="category">Here is a subtitle for this table</p>--}}
            </div>
            <div class="content table-responsive table-full-width">
              @if(count($comments) > 0)
                <table class="table table-hover table-striped">
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>Latitude</th>
                      <th>Longitude</th>
                      <th>Created At</th>
                      <th>Actions</th>
                    </tr>
                  </thead>

                  <tbody>
                    @foreach($comments as $comment)
                      @if(not_null($comment->user) && not_null($comment->safe_zone))
                        <tr>
                          <td>{{$comment->title}}</td>
                          <td>{{$comment->user->full_name}}</td>
                          <td>{{$comment->safe_zone->name}}</td>
                          <td>{{$comment->created_at}}</td>
                          <td>
                            <a href="{{route('admin.comments.show', $comment->id)}}" class="btn btn-default">Show</a>
                            @if(Auth::user()->isAdmin())
                              {{--<a href="{{route('admin.comments.edit', $comment->id)}}" class="btn btn-info">Edit</a>--}}
                              {!!Form::open(['method'=> "POST","route" => ["admin.comments.delete" , $comment->id],"style"=>"display:inline"]) !!}
                              <a class="btn btn-s btn-danger"
                                 type="button"
                                 data-toggle="modal"
                                 data-target="#confirmDelete"
                                 data-title="Delete Safe Zone"
                                 data-message="Are you sure you want to delete this safe zone ?">
                                {{--<i class="fa fa-trash-o text-danger"></i>--}}
                                Delete
                              </a>
                            @endif
                            {!! Form::close() !!}
                          </td>
                        </tr>
                      @endif
                    @endforeach

                  </tbody>
                </table>

                <div class="text-center">
                  {!! $comments->links() !!}
                </div>
              @else
                <div class="text-center">
                  <h1>NO COMMENTS FOUND</h1>
                </div>
              @endif
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
