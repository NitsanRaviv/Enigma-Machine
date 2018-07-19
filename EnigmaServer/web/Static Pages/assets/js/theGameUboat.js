var competitionStart_url = buildUrlWithContextPath("competitionStart");
var membersInfo_url = buildUrlWithContextPath("membersInfo");
var optionalStrings_url = buildUrlWithContextPath("optionalStrings");
var gameOver_url = buildUrlWithContextPath("gameOver");


$(function () {

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    competitionStart();
    getMembersInfo();
    getOptionalStrings();
    gameOver();
    

});

function competitionStart() {

    $.ajax({
        url: competitionStart_url,
        datatype: 'json',
        success: function (data) {
            if (data) {
                alert("We are ready to start!");
            }
        }
    });

// get json with crew name and num of agents
  function getMembersInfo() {
    $.ajax({
        url: membersInfo_url,
        datatype: 'json',
        success: function (data) {
            var membersInfoContainer = document.getElementById("membersInfo");
            if (data) {
                membersInfo = $.parseJSON(data);

                for (var url in membersInfo) {
                    var name = document.createElement("P");
                    var t_name = document.createTextNode("Name: " + membersInfo[url].name);  
                    name.appendChild(t_name);
                    var agents = document.createElement("P");
                    var t_agents = document.createTextNode("Number of agents: " + membersInfo[url].agent);  
                    agents.appendChild(t_agents);
                  
                    membersInfoContainer.appendChild(name);
                    membersInfoContainer.appendChild(agents);
                }
            }
        }
    });

// get json with crew name and optinal string

      function getOptionalStrings() {
    $.ajax({
        url: optionalStrings_url,
        datatype: 'json',
        success: function (data) {
            var optionalStringsContainer = document.getElementById("optionalStrings");
            if (data) {
                optionalStrings = $.parseJSON(data);

                for (var url in optionalStrings) {
                    var name = document.createElement("P");
                    var t_name = document.createTextNode("Name: " + optionalStrings[url].name);  
                    name.appendChild(t_name);
                    var optionalStrings = document.createElement("P");
                    var t_optionalString = document.createTextNode("Optional String: " + membersInfo[url].optionalString);  
                    optionalStrings.appendChild(t_optionalString);
                  
                    membersInfoContainer.appendChild(name);
                    membersInfoContainer.appendChild(optionalStrings);
                }
            }
        }
    });

// right now after one round, game is over
    function gameOver() {

    $.ajax({
        url: gameOver_url,
        datatype: 'json',
        success: function (data) {
            if (data) {
            	var btn = document.createElement("BUTTON");
            	var t_logout = document.createTextNode("Logout");
            	btn.appendChild(t_logout);
            	document.getElementById("logout").appendChild(btn);
            }
        }
    });