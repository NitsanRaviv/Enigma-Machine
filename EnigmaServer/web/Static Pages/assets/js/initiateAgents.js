var port_url = "http://localhost:8080/???";


      $(function () {

          //prevent IE from caching ajax calls
          $.ajaxSetup({cache: false});

          getPort();

      });

      function getPort() {
          $.ajax({
              url: port_url,
              datatype: 'json',
              success: function (data) {
                  if (data) {
                      thePort = $.parseJSON(data);

                      var portToUser = document.createElement("h2");
                      var t_port = document.createTextNode(thePort);
                      portToUser.appendChild(t_port);
                      document.getElementById("port").appendChild(portToUser);
                      }
          });
      }