//var availableGames_url = buildUrlWithContextPath("openCompetitions");
var availableGames_url = "localhost:8080/openCompetitions";

$(function () {

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    getAvailableGames();
    

}

  function getAvailableGames() {
      $.ajax({
          url: availableGames_url,
          datatype: 'json',
          success: function (data) {
              if (data) {
                  availableGames = $.parseJSON(data);
                  var i = 1;
                  for (var url in availableGames) {
                      var game = document.createElement("P");
                      var t_game = document.createTextNode(i + ". " + availableGames[url]);
                      game.appendChild(t_game);

                      document.getElementById("availableCompetition").appendChild(game);
                      i++;
                  }
              }
          }
      });
  }