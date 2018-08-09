var allies_url = myBuildUrlWithContextPath("/EnigmaServer/getInfoCompAlly");

$(function () {
    $.ajaxSetup({cache: false});

    getMissionSize();
    getDecodeString();
    waitingMsg();

    var readyInterval = window.setInterval(function(){
        $.ajax({
            url: allies_url,
            datatype: 'json',
            success: function (data) {
                if(data){
                    allies = $.parseJSON(data);
                    if(allies.started.localeCompare("yes") === 0){
                        clearBox("ready");
                        window.clearInterval(readyInterval);
                    }
                }
            }
        });
    }, 3000);

    var gameOverInterval = window.setInterval(function(){
        $.ajax({
            url: allies_url,
            datatype: 'json',
            success: function (data) {
                if(data){
                    allies = $.parseJSON(data);
                    clearBox("competitorsInfo");
                    clearBox("agentDetails");
                    getCompetitorsInfo();
                    getAgentDetails();
                    if(allies.winner.localeCompare("noWinner") != 0){
                        printWinner();
                        window.clearInterval(gameOverInterval);
                    }
                }
            }
        });
    }, 3000);
});


function waitingMsg() {
    var msg = document.createElement("p");
    var t_msg = document.createTextNode("waiting to others allies..");
    msg.appendChild(t_msg);
    document.getElementById("ready").appendChild(msg);
}

function getMissionSize() {
    $.ajax({
        url: allies_url,
        datatype: 'json',
        success: function (data) {
            if(data){
                allies = $.parseJSON(data);
                var theSize = document.createElement("P");
                var t_size = document.createTextNode(allies.sizeOfMission);
                theSize.appendChild(t_size);
                document.getElementById("missionSize").appendChild(theSize);
            }
        }
    });
}

function clearBox(elementID) {
    document.getElementById(elementID).innerHTML = "";
}

function getDecodeString() {
    $.ajax({
        url: allies_url,
        datatype: 'json',
        success: function (data) {
            if(data){
                allies = $.parseJSON(data);
                var theString = document.createElement("P");
                var t_String= document.createTextNode(allies.encryptedString);
                theString.appendChild(t_String);
                document.getElementById("decodeString").appendChild(theString);
            }
        }
    });
}

function getCompetitorsInfo() {
    $.ajax({
        url: allies_url,
        datatype: 'json',
        success: function (data) {
            if(data){
                allies = $.parseJSON(data);
                var competitor = document.createElement("P");
                var t_Competitor= document.createTextNode(allies.alliesAndAgents);
                competitor.appendChild(t_Competitor);
                document.getElementById("competitorsInfo").appendChild(competitor);
            }
        }
    });
}

function getAgentDetails() {
    $.ajax({
        url: allies_url,
        datatype: 'json',
        success: function (data) {
            if(data){
                allies = $.parseJSON(data);
                var theAgentDetails = document.createElement("P");
                var t_agent = document.createTextNode(allies.allyAndPotentials);
                theAgentDetails.appendChild(t_agent);
                document.getElementById("agentDetails").appendChild(theAgentDetails);
            }
        }
    });
}

function printWinner() {
    $.ajax({
        url: allies_url,
        datatype: 'json',
        success: function (data) {
            if(data){
                allies = $.parseJSON(data);
               // clearBox("missionSize");
                //clearBox("decodeString");
                //clearBox("competitorsInfo");
              //clearBox("agentDetails");
                var Winner = document.createElement("P");
                var t_Winner = document.createTextNode(allies.winner);
                Winner.appendChild(t_Winner);
                document.getElementById("gameOver").appendChild(Winner);

                var end = document.createElement("P");
                var t_End = document.createTextNode("30 seconds until leaving this page...");
                end.appendChild(t_End);
                document.getElementById("byebye").appendChild(end);
                window.setTimeout(function () {
                       window.location = "initiateAgents.html";
                }, 30000);
            }
        }
    });
}
