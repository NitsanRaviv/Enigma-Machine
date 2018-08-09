var uboat_url = myBuildUrlWithContextPath("/EnigmaServer/canStartCompetition");

$(function () {
    $.ajaxSetup({cache: false});

    waitingMsg();
    var readyInterval = window.setInterval(function(){
        $.ajax({
            url: uboat_url,
            datatype: 'json',
            success: function (data) {
                if(data){
                    uboat = $.parseJSON(data);

                    if(uboat.started.localeCompare("yes") === 0){
                        clearBox("wait");
                        window.clearInterval(readyInterval);
                    }
                }
            }
        });
    }, 3000);


    var gameOverInterval = window.setInterval(function(){
        $.ajax({
            url: uboat_url,
            datatype: 'json',
            success: function (data) {
                if(data){
                    uboat = $.parseJSON(data);

                    if(uboat.started.localeCompare("yes") === 0){
                        clearBox("membersInfo");
                        clearBox("optionalStrings");
                        getMembersInfo();
                        getOptionalStrings();
                    }

                    if(uboat.finished.localeCompare("yes") === 0){
                        window.clearInterval(gameOverInterval);
                        //window.location = "/theGameUboat.html"
                    }
                }
            }
        });
    }, 3000);

    logout();
});

function waitingMsg() {
    var msg = document.createElement("p");
    var t_msg = document.createTextNode("waiting to competitors..");
    msg.appendChild(t_msg);
    document.getElementById("wait").appendChild(msg);
}

function clearBox(elementID) {
    document.getElementById(elementID).innerHTML = "";
}

function getMembersInfo() {
  $.ajax({
      url: uboat_url,
      datatype: 'json',
      success: function (data) {
          if (data) {
              uboat = $.parseJSON(data);
              var theMembers = document.createElement("P");
              var t_Members= document.createTextNode(uboat.alliesAndAgents);
              theMembers.appendChild(t_Members);
              document.getElementById("membersInfo").appendChild(theMembers);
          }
      }
  });
}

function getOptionalStrings() {
  $.ajax({
      url: uboat_url,
      datatype: 'json',
      success: function (data) {
          if (data) {
              uboat = $.parseJSON(data);
              var theStrings = document.createElement("P");
              var t_Strings= document.createTextNode(uboat.allAlliesPotentials);
              theStrings.appendChild(t_Strings);
              document.getElementById("optionalStrings").appendChild(theStrings);
          }
      }
  });
}

function logout() {
    var btn = document.createElement("BUTTON");
    var t_logout = document.createTextNode("Logout");
    btn.appendChild(t_logout);
    document.getElementById("logout").appendChild(btn);
    document.getElementById("logout").onclick = function() {window.location = "/uboutLogout";};
}