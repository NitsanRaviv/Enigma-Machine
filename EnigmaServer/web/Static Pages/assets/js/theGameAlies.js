var allies_url = "http://localhost:8080/???";

$(function () {

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    showReadyBtn();
    while(readyToStart() == false){
        waitingMsg();
    }
    clearBox("ready");

    getMissionSize();
    getDecodeString();

    while(gameOver() == false){
        getCompetitorsInfo();
        getAgentDetails();
    }

    printWinner();

});

function showReadyBtn() {
    var btn = document.createElement("BUTTON");
    var t_ready = document.createTextNode("Ready");
    btn.appendChild(t_ready);
    document.getElementById("ready").appendChild(btn);
    document.getElementById("ready").onclick = function() {clearBox("ready")};
}

function waitingMsg() {
    var msg = document.createElement("p");
    var t_msg = document.createTextNode("waiting to others allies..");
    msg.appendChild(t_msg);
    document.getElementById("ready").appendChild(msg);
}

function readyToStart() {
    $.ajax({
        url: allies_url,
        datatype: 'json',
        success: function (data) {
            allies = $.parseJSON(data);
            var res = false;

            if(allies[0].localeCompare("yes") === 0)
                res = true;
        }
    });

    return res;
}

function getMissionSize() {
    $.ajax({
        url: allies_url,
        datatype: 'json',
        success: function (data) {
            allies = $.parseJSON(data);
            var theSize = document.createElement("P");
            var t_size = document.createTextNode(allies[1]);
            theSize.appendChild(t_size);
            document.getElementById("missionSize").appendChild(theSize);
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
            allies = $.parseJSON(data);
            var theString = document.createElement("P");
            var t_String= document.createTextNode(allies[2]);
            theString.appendChild(t_String);
            document.getElementById("decodeString").appendChild(theString);
        }
    });
}


function gameOver() {
    $.ajax({
        url: allies_url,
        datatype: 'json',
        success: function (data) {
            allies = $.parseJSON(data);
            var res = true;

            if(allies[0].localeCompare("no") === 0)
                res = false;
        }
    });

    return res;
}

function getCompetitorsInfo() {
    $.ajax({
        url: allies_url,
        datatype: 'json',
        success: function (data) {
            allies = $.parseJSON(data);
            var competitor = document.createElement("P");
            var t_Competitor= document.createTextNode(allies[1]);
            competitor.appendChild(t_Competitor);
            document.getElementById("competitorsInfo").appendChild(competitor);
        }
    });
}

function getAgentDetails() {
    $.ajax({
        url: allies_url,
        datatype: 'json',
        success: function (data) {
        allies = $.parseJSON(data);
        var theAgentDetails = document.createElement("P");
        var t_agent = document.createTextNode(allies[2]);
        theAgentDetails.appendChild(t_agent);
        document.getElementById("agentDetails").appendChild(theAgentDetails);
        }
    });
}

function printWinner() {
    $.ajax({
        url: allies_url,
        datatype: 'json',
        success: function (data) {
            allies = $.parseJSON(data);
            clearBox("missionSize");
            clearBox("decodeString");
            clearBox("competitorsInfo");
            clearBox("agentDetails");
            var Winner = document.createElement("h2");
            var t_Winner = document.createTextNode("The Winner" + allies[0]);
            Winner.appendChild(t_Winner);
            document.getElementById("gameOver").appendChild(Winner);
            window.setTimeout(function () {
                window.location = "initiateAgents.html";
            }, 4000);
        }
    });
}