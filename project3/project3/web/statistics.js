/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function getStats(display){
    var req = new XMLHttpRequest();
    req.open('GET', 'http://'+location.host+'/project3/statistics?display='+display, false);
    req.send();
    
    if (req.status == 200) { 
        
        var x = document.getElementById('body');
        x.innerHTML=req.responseText;
        
        alert(req.responseText);
    }
}

