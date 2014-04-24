var currentlyHighlightedEl = null;

function toggleBranch(branch) {
    var objBranch = $('#B' + branch);
    var objImg = $('#I' + branch);
    if (objBranch.is(":visible")) {
        objBranch.hide();
        objImg.each(function() {
            this.src = 'template/fold_closed.gif';
        })
    } else {
        objBranch.show();
        objImg.each(function() {
            this.src = 'template/fold_opened.gif';
        })
    }
}

function expandProblem() {
    $('div.trigger>img[src*=error],div.trigger>img[src*=warn]').next(
            'span[class*=title]').each(function() {
        var id = this.id.replace(/^\D+/g, '');
        var objBranch = $('#B' + id);
        var objImg = $('#I' + id);
        if (!objBranch.is(":visible")) {
            objBranch.show();
            objImg.each(function() {
                this.src = 'template/fold_opened.gif';
            })
        }
    })
}

function collapsAll() {
    $('div.trigger>img').next('span[class*=title]').each(function() {
        var id = this.id.replace(/^\D+/g, '');
        var objBranch = $('#B' + id);
        var objImg = $('#I' + id);
        if (objBranch.is(":visible")) {
            objBranch.hide();
            objImg.each(function() {
                this.src = 'template/fold_closed.gif';
            })
        }
    })
}

function highlightMessage(messageEl) {
    if (messageEl) {
        messageEl.className = 'title_selected';
        for ( var childItem in messageEl.childNodes) {
            if (messageEl.childNodes[childItem].nodeType == 1) {
                messageEl.childNodes[childItem].className = 'title_selected';
            }
        }
        if (currentlyHighlightedEl && messageEl != currentlyHighlightedEl) {
            currentlyHighlightedEl.className = 'title';
            for ( var childItem in currentlyHighlightedEl.childNodes) {
                if (currentlyHighlightedEl.childNodes[childItem].nodeType == 1) {
                    currentlyHighlightedEl.childNodes[childItem].className = 'title';
                }
            }
        }
        currentlyHighlightedEl = messageEl;
    }
}

function showDetails(msgId) {
    if (parent.window.frames['details'].isSWT != undefined && parent.window.frames['details'].isSWT) {
        parent.window.frames['details'].document.getElementById('issue_block').style.display="block";
    } 
    
	$('div.details', parent.window.frames['details'].document).attr('class','details status_'+$('#ST'+msgId).text());
    setElementValueInFrame('details', 'title', $('#T' + msgId).text());

    el = document.getElementById("M"+msgId);
    if (el) {
        setElementValueInFrame("details","message",getTextFromElement(el));
    }
    setElementValueInFrame('details', 'time', $('#D' + msgId).text());
    setElementValueInFrame('details', 'start', $('#TM' + msgId).text());
    setElementValueInFrame('details', 'summary', '');
    parent.window.frames['details'].document.getElementById("executionTime").style.display="block";
    
    var timeElement = parent.window.frames['details'].document.getElementById("time");
    if (timeElement != undefined) {
        if (  /\d/.test(timeElement.innerHTML)) {
            parent.window.frames['details'].document.getElementById("duration").style.display="block"; 
            timeElement.style.display="block"; 
        } else {
            parent.window.frames['details'].document.getElementById("duration").style.display="none"; 
            timeElement.style.display="none"; 
        }
    }
    parent.window.frames['details'].document.getElementById("start").style.display="block";      
        
    var page = $('#S' + msgId).text();
    $('#filename',parent.window.frames['settings'].document).attr('value',page)
    parent.showSnapshot(page);

    
}

var ie = document.all;
function getTextFromElement(el) {    
    //disable-output-escaping="yes" is not working in XSL for Firefox
    return ie ? el.innerHTML : el.textContent; 
}

function showLogDetails() {
    var el = $('#log_name');
    var knownBugs = parent.knownBugs==undefined?0:parent.knownBugs;
    var fail = el.attr('fail_count')==undefined?0:el.attr('fail_count');
    var newBugs = fail>knownBugs?fail-knownBugs:0;
    var display = parent.window.frames['details'].isSWT==undefined||false?'none':'block';
    setElementValueInFrame('details', 'title', el.text());
    setElementValueInFrame('details', 'time', 'Date: ' + el.attr('d')
            + '<br/>Time: ' + el.attr('t') + '<br/>Duration: '
            + el.attr('et') + ' second(s)');
    parent.window.frames['details'].document.getElementById("executionTime").style.display="none";      
    parent.window.frames['details'].document.getElementById("duration").style.display="none";   
     parent.window.frames['details'].document.getElementById("start").style.display="none";  

       setElementValueInFrame('details', 'summary', '<table style="display:box" id="summaryTable"><tr><td id="fail_ico" width="16px"></td><td>Fail</td><td>' + fail + '<td/></tr>'+
            '<tr><td id="pass_ico" width="16px"></td><td>Pass</td><td>' + el.attr('passed_count') + '</td></tr>'+
            '<tr class="showOnlyInSWT" id="knownBugsSWT" style="display:'+display+'" ><td id="knownBug_ico" width="16px"></td><td>Known Bugs</td><td>'+knownBugs+'</td></tr>'+
            '<tr class="showOnlyInSWT" id="newBugsSWT" style="display:'+display+'"><td id="newBug_ico" width="16px"></td><td><b>New Bugs</b></td><td>'+newBugs+'</td></tr>');    
            
    if (currentlyHighlightedEl) {
        currentlyHighlightedEl.className = 'title';
        currentlyHighlightedEl = null;
    }


}
function expandProblemIfNeeded() {
    if ($('input[name=expand_problem]',
            parent.window.frames['settings'].document).is(':checked')) {
        collapsAll();
        expandProblem();
    }
    ;
}
function setElementValueInFrame(frame, elId, text) {
    $('#' + elId, parent.window.frames[frame].document).html(text);
}

function showSnapshot(file) { 
    var snap = $('#snapshot',parent.window.frames['details'].document);
    if ($('input[name=snapshots]',parent.window.frames['settings'].document).is(':checked') && file && file.length>0) {
        snap.show();
		snap.children("iframe").attr("src","../pages/"+file);
    } else {
		snap.hide();
		snap.children("iframe").attr("src","");
	}
}
