var availableGames_url = myBuildUrlWithContextPath("/EnigmaServer/openCompetitions");
var allGames = [];
var i = 1;

      $(function () {

          //prevent IE from caching ajax calls
          $.ajaxSetup({cache: false});
          getAvailableGames();
          window.setInterval(function(){
              getAvailableGames();
          }, 3000);

      });

      function getAvailableGames() {
          $.ajax({
              url: availableGames_url,
              datatype: 'json',
              success: function (data) {
                  if (data) {
                      availableGames = $.parseJSON(data);
                      var addNew = 1;
                      for (var url in availableGames) {
                          for(var game in allGames){
                              if(allGames[game].localeCompare(availableGames[url]) === 0)
                                  addNew = 0;
                          }
                          if(addNew) {
                              var game = document.createElement("P");
                              var t_game = document.createTextNode(i + ". " + availableGames[url]);
                              game.appendChild(t_game);

                              document.getElementById("availableCompetition").appendChild(game);
                              allGames.push(availableGames[url]);
                              i++;
                          }

                          addNew = 1;
                      }
                  }
              }
          });
      }