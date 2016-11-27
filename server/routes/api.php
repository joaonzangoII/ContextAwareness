<?php

use App\Notifications\RegisterUserNotification;
use App\SafeZone;
use App\User;
use App\Timeline;
use App\Comment;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Crypt;
use Illuminate\Support\Facades\Password;
use Illuminate\Support\Facades\Validator;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::get('/user', function (Request $request) {
  return $request->user();
})->middleware('auth:api');

Route::get('/users/all', function (Request $request) {
  return response()->json(User::all());
});

Route::get('/users/comments/{user_id}', function (Request $request, $user_id) {
  $comments = Comment::where("user_id", $user_id)->get();
  return response()->json($comments);
});

Route::post('/reset/password', function (Request $request) {
  $validator = Validator::make($request->all(), [
    'email' => 'required|email'
  ]);

  if ($validator->fails()) {
    return response()->json([
      'error' => true,
      'code' => '500',
      'messages' => $validator->messages()
    ]);
  }

// We will send the password reset link to this user. Once we have attempted
// to send the link, we will examine the response then see the message we
// need to show to the user. Finally, we'll send out a proper response.
  $broker = Password::broker();
  $response = $broker->sendResetLink(
    $request->only('email')
  );

  if ($response === Password::RESET_LINK_SENT) {
    $user = User::where('email', $request->input('email'))->first();
    return response()->json([
      'error' => false,
      'code' => '200',
      'status' => trans($response),
      'user' => $user
    ]);
  }

// If an error was returned by the password broker, we will get this message
// translated so we can notify a user of the problem. We'll redirect back
// to where the users came from so they can attempt this process again.

  return response()->json([
    'error' => true,
    'code' => '500',
    'messages' => [
      'email' => [
        trans($response)
      ]
    ]
  ]);
});

Route::post('/login', function (Request $request) {
  $validator = Validator::make($request->all(), [
    'password' => 'required',
    'id_number' => 'required',
  ]);

  if ($validator->fails()) {
    return response()->json([
      'error' => true,
      'code' => '500',
      'messages' => $validator->messages()
    ]);
  }

  if (Auth::attempt($request->only('id_number', 'password')) == false) {
    return response()->json([
      'error' => true,
      'code' => '500',
      'messages' => [
        'id_number' => [
          'Login credentials are wrong. Please try again!'
        ]
      ]
    ]);
  }

  return response()->json([
    'error' => false,
    'code' => '200',
    'user' => User::where('id_number', $request->input('id_number'))->first()
  ]);
});


Route::post('/register', function (Request $request) {
  $validator = Validator::make($request->all(), [
    'firstname' => 'required|max:255',
    'lastname' => 'required|max:255',
    'email' => 'required|email|max:255|unique:users',
    'password' => 'required|min:6|strength|confirmed',
    'id_number' => 'required|size:13|unique:users|correct',
  ]);

  if ($validator->fails()) {
    return response()->json([
      'error' => true,
      'code' => '500',
      'messages' => $validator->messages()
    ]);
  }

  $data = $request->all();
  $data['password'] = $request->input('password');
  $data['gender'] = '';
  $data['date_of_birth'] = '';
  $user = User::create($data);
  $user = User::find($user->id);
  $user->notify(new RegisterUserNotification());
  return response()->json([
    'error' => false,
    'code' => '200',
    'user' => $user
  ]);
});


Route::post('/edit/profile', function (Request $request) {
  $validator = Validator::make($request->all(), [
    'user_id' => 'required',
    'firstname' => 'required|max:255',
    'lastname' => 'required|max:255',
    'email' => 'required|email|max:255',
    'password' => 'required|min:6|strength|confirmed',
  ]);

  if ($validator->fails()) {
    return response()->json([
      'error' => true,
      'code' => '500',
      'messages' => $validator->messages()
    ]);
  }


  $user = User::find($request->input('user_id'));
  if($user==null){
    return response()->json([
      'error' => true,
      'code' => '500',
      'messages' => [
        'safe_zone_id' => [
          'This user does not exist'
        ]
      ]
    ]);
  }
  $user->update($request->all());
  return response()->json([
    'error' => false,
    'code' => '200',
    'user' => $user
  ]);
});

Route::get('/safe-zones/all', function (Request $request) {
  return response()->json(SafeZone::all());
});

Route::post('/safe-zones/create', function (Request $request) {
  $validator = Validator::make($request->all(), [
    'name' => 'required|max:255',
    'latitude' => 'required|numeric',
    'longitude' => 'required|numeric',
    'radius' => 'required|numeric|positive',
  ]);

  if ($validator->fails()) {
    return response()->json([
      'error' => true,
      'code' => '500',
      'messages' => $validator->messages()
    ]);
  }
  $data = $request->all();
  $safezone = SafeZone::create($data);
  return response()->json([
    'error' => false,
    'code' => '200',
    'safezone' => $safezone
  ]);
});

Route::post('/safe-zones/update', function (Request $request) {
  $validator = Validator::make($request->all(), [
    'safe_zone_id' => 'required',
    'name' => 'required|max:255',
    'latitude' => 'required|numeric',
    'longitude' => 'required|numeric',
    'radius' => 'required|numeric|positive',
  ]);

  if ($validator->fails()) {
    return response()->json([
      'error' => true,
      'code' => '500',
      'messages' => $validator->messages()
    ]);
  }

  $safezone = SafeZone::find($request->input('safe_zone_id'));
  if($safezone==null){
    return response()->json([
      'error' => true,
      'code' => '500',
      'messages' => [
        'safe_zone_id' => [
          'This safe zone does not exist'
        ]
      ]
    ]);
  }
  $safezone->update($request->all());
  return response()->json([
    'error' => false,
    'code' => '200',
    'safezone' => $safezone
  ]);
});

Route::post('/safe-zones/delete', function (Request $request) {
  $validator = Validator::make($request->all(), [
    'safe_zone_id' => 'required',
  ]);

  if ($validator->fails()) {
    return response()->json([
      'error' => true,
      'code' => '500',
      'messages' => $validator->messages()
    ]);
  }

  $safezone = SafeZone::find($request->input('safe_zone_id'));
  if($safezone==null){
    return response()->json([
      'error' => true,
      'code' => '500',
      'messages' => [
        'safe_zone_id' => [
          'This safe zone does not exist'
        ]
      ]
    ]);
  }
  $safezone->delete();
  return response()->json([
    'error' => false,
    'code' => '200',
    'safe_zone_id' => $request->input('safe_zone_id')
  ]);
});

Route::post('/comment', function (Request $request) {
  $validator = Validator::make($request->all(), [
    'title' => 'required|max:255',
    'body' => 'required|max:255',
    'user_id' => 'required|numeric',
    'safe_zone_id' => 'required|numeric',
  ]);

  if ($validator->fails()) {
    return response()->json([
      'error' => true,
      'code' => '500',
      'messages' => $validator->messages()
    ]);
  }

  $data = $request->all();
  $comment = Comment::create($data);
  return response()->json([
    'error' => false,
    'code' => '200',
    'comment' => $comment
  ]);
});


Route::post('/event', function (Request $request) {
  $validator = Validator::make($request->all(), [
    'title' => 'required|max:255',
    'body' => 'required|max:255',
    'user_id' => 'required|numeric',
    'safe_zone_id' => 'required|numeric',
  ]);

  if ($validator->fails()) {
    return response()->json([
      'error' => true,
      'code' => '500',
      'messages' => $validator->messages()
    ]);
  }

  $data = $request->all();
  $event = Timeline::create($data);
  return response()->json([
    'error' => false,
    'code' => '200',
    'event' => $event
  ]);
});

// Route::get('*', function (Request $request) {
//   return response()->json([
//     'error' => true,
//     'code' => '500',
//     'messages' => ["not found"]
//   ]);
// });
