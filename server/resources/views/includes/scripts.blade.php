<!-- Scripts -->
{{--<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=false&key=AIzaSyDk_BZkzSg0UMXtT_R-ijdp8sX8aiu22NY"></script>--}}
<footer>

</footer>
<script type="text/javascript">
  $(document).ready(function () {
    window.Laravel = <?php echo json_encode([
     'csrfToken' => csrf_token(),
    ]); ?>

    $('div.alert').delay(6000).slideUp(300);
    $('#btnCloseNotification').on('click', function ($ev) {
      $ev.preventDefault();
      $('div.alert').delay(0).slideUp(1);
    });

    //    $('#date_of_birth').datepicker({
    //      dateFormat: 'yy-mm-dd'
    //        });

//
//    demo.initChartist();
//
//    $.notify({
//      icon: 'pe-7s-gift',
//      message: "Welcome to <b>Light Bootstrap Dashboard</b> - a beautiful freebie for every web developer."
//
//    }, {
//      type: 'info',
//      timer: 4000
//    });
  });
</script>
@yield('scripts')