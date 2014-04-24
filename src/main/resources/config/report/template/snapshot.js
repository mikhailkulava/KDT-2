var frameSet = "";
function showSnapshot(file) { 
  
     var ischecked = parent.window.frames['settings'].document.forms[0].snapshots.checked; //disable/enable snaphots;
    if (!ischecked || file == "" ) {
        frameSet  = parent.document.getElementById('detailsFrameSet').rows;
        parent.document.getElementById('detailsFrameSet').rows = '5%,50%';
    return;
    }
    parent.window.document.getElementById('snapshot').src = "";
    
   
    if (ischecked && file.length>0) {
        if (frameSet == ""){
              frameSet  = parent.document.getElementById('detailsFrameSet').rows;
        }
   
        tempFrame = frameSet.split(',');
        parent.document.getElementById('detailsFrameSet').rows = tempFrame[0]+','+tempFrame[1]+',50%';
        
        parent.window.document.getElementById('snapshot').src = ischecked && file && file.length>0 ? "pages/"+file : "";
    } 
}

