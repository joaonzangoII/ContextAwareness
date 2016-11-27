@extends('layouts.master')
@section('content')
  <style>
  /* html, body {
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
    }*/
  </style>
  <div class="content">
    <div class="container-fluid">
      <h1><b>Safe Zones:</b> {{count($safeZones)}}</h1>
      <h1><b>Events:</b> {{count($events)}}</h1>
      <h1><b>Comments:</b> {{count($comments)}}</h1>
      <div class="row">
        <div class="col-md-4">
           <div class="card">
               <div class="header">
                   <h4 class="title">Email Statistics</h4>
                   <p class="category">Last Campaign Performance</p>
               </div>
               <div class="content">
                   <div id="chartEvents" class="ct-chart ct-perfect-fourth"></div>
                   <div class="footer">
                       <div class="legend">
                           <i class="fa fa-circle text-info"></i> Open
                           <i class="fa fa-circle text-danger"></i> Bounce
                           <i class="fa fa-circle text-warning"></i> Unsubscribe
                       </div>
                       <hr>
                       <div class="stats">
                           <i class="fa fa-clock-o"></i> Campaign sent 2 days ago
                       </div>
                   </div>
               </div>
           </div>
       </div>
       <div class="col-md-8">

       </div>
     </div>
     <div class="row">
       <div class="col-md-4">
         <h3><b>Latest added Safe Zones</b></h3>
         <div class="content table-responsive table-full-width">
         <table class="table table-hover table-striped">
           <thead>
             <tr>
               <td>Name</td>
               <td>Events</td>
               <td>Comments</td>
             </tr>
           </thead>
           <tbody>
           @for ($i = 0; $i < count($safeZones); $i++)
            <tr>
              <td>{{$safeZones[$i]->name}}</td>
              <td>{{count($safeZones[$i]->events)}}</td>
              <td>{{count($safeZonesByEvent[$i]->comments)}}</td>
            </tr>
           @endfor
          </tbody>
         </table>
         </div>
       </div>

       <div class="col-md-4">
         <h3><b>Safe Zones By Event</b></h3>
         <div class="content table-responsive table-full-width">
         <table class="table table-hover table-striped">
           <thead>
             <tr>
               <td>Name</td>
               <td>Events</td>
             </tr>
           </thead>
           <tbody>
             @for ($i = 0; $i < count($safeZonesByEvent); $i++)
              <tr>
                <td>{{$safeZonesByEvent[$i]->name}}</td>
                <td>{{count($safeZonesByEvent[$i]->events)}}</td>
              </tr>
             @endfor
           </tbody>
         </table>
         </div>
       </div>

       <div class="col-md-4">
         <h3><b>Safe Zones By Comment</b></h3>
         <div class="content table-responsive table-full-width">
         <table class="table table-hover table-striped">
           <thead>
             <tr>
               <td>Name</td>
               <td>Events</td>
             </tr>
           </thead>
           <tbody>
             @for ($i = 0; $i < count($safeZonesByComment); $i++)
              <tr>
                <td>{{$safeZonesByComment[$i]->name}}</td>
                <td>{{count($safeZonesByComment[$i]->comments)}}</td>
              </tr>
             @endfor
          </tbody>
         </table>
       </div>
       </div>
    </div>
  </div>
</div>
@endsection

@section('scripts')
<script type="text/javascript">
  var dataEvents = {
    // A labels array that can contain any sort of values
    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri'],
    // Our series array that contains series objects or in this case series data arrays
    series: [
      [5, 2, 4, 2, 0]
    ]
  };

  var optionsEvents = {
     lineSmooth: false,
     low: 0,
     high: 800,
     showArea: true,
     height: "245px",
     axisX: {
       showGrid: false,
     },
     lineSmooth: Chartist.Interpolation.simple({
       divisor: 3
     }),
     showLine: false,
     showPoint: false,
   };

   var responsiveEvents = [
     ['screen and (max-width: 640px)', {
       axisX: {
         labelInterpolationFnc: function (value) {
           return value[0];
         }
       }
     }]
   ];
  // Create a new line chart object where as first parameter we pass in a selector
  // that is resolving to our chart container element. The Second parameter
  // is the actual data object.
   // new Chartist.Line('.ct-chart', data);
   Chartist.Line('#chartEvents', dataEvents, optionsEvents, responsiveEvents);
   //Chartist.Bar('#chartActivity', data, options, responsiveOptions);
</script>
@endsection
