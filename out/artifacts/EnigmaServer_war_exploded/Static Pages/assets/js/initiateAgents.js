var port_url =  myBuildUrlWithContextPath("EnigmaServer/getAllyPort");


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

                          for (var url in thePort) {
                              var portToUser = document.createElement("h2");
                              var t_port = document.createTextNode(thePort[url]);
                              portToUser.appendChild(t_port);
                              document.getElementById("port").appendChild(portToUser);
                          }
                      }
                  }
              });
      }