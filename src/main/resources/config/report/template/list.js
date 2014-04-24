var listCurrentlyHighlightedEl = null;

function highlight(messageEl) {
    if (messageEl) {
        messageEl.className = "selected";
        if (listCurrentlyHighlightedEl && messageEl!=listCurrentlyHighlightedEl) {
            listCurrentlyHighlightedEl.className = null;
        }
        listCurrentlyHighlightedEl = messageEl;
        parent.window.frames['details'].document.getElementById('message').innerHTML=messageEl.title;
    }

}
