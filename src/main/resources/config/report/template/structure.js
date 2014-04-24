var Issue = function() {
        this.id = undefined;
        this.url = undefined;
        this.status = undefined;
        this.name = undefined;
        this.description = undefined;
    };
    
var isssueHeaderTr = '<tr class=\'section_header\'><td> name</td><td>status</td><td>description</td><td></td></tr>';

/* Create a tree of issues for scenario and nodes
 *          scenario_name(root)
 *          /        |      \
 *        step1    step2   step3---------\
 *       /     \             |   \        \
 *  substep1   substep2 substep3  \        \
 *                               substep4 substep5(body:[issue1,issue2,...])
 */
var Tree = function() {
    this.root = undefined;
    this.node2JSON     = function (node) { 
        var result = '{"id":"'+this.escape(node.id)+'","name":"'+this.escape(node.name)+'","body":[';
        for (var iterable in node.body) {  
            var item = node.body[iterable];
            result += '{"id":"'+this.escape(item.id)+'","name":"'+this.escape(item.name)+'","status":"'+this.escape(item.status)+'","description":"'+this.escape(item.description)+'","url":"'+this.escape(item.url)+'"}'+',';
        }
        result += ']';
        if (node.parent) {
            if (typeof(node.parent.id) == 'number') {
                result += ',"parent":{"id":"'+node.parent.id+'"}'
            } else {
                result += ',"parent":'+this.node2JSON(node.parent);
            }
        }
        return result += '}';
    };
    this.issue2JSON     = function (issue) { 
        return '{"id":"'+this.escape(issue.id)+'","name":"'+this.escape(issue.name)+'","status":"'+this.escape(issue.status)+'","url":"'+this.escape(issue.url)+'","description":"'+this.escape(issue.description)+'"}';
    };
    this.escape = function(str) {
        if(typeof(str)!='string') return "";
        return str.replaceAll('\\','\\\\').replaceAll('"','\\"');
    };
};
Tree.TreeNode = function() {
    this.id         = "";
    this.name       = "";
    this.parent     = undefined;
    this.childrens  = new Array();
    this.body       = new Array();
    this.div        = undefined;
};
var tree = new Tree();


String.prototype.replaceAll = function(search, replace){
    return this.split(search).join(replace);
};

var statusColorMap = new Object();
statusColorMap['fail']='red';
statusColorMap['pass']='green';
statusColorMap['']='';

var getRootOfBranch = function(div) {
    var getParentTrigger = function(div) {
        return $(div).parent('div.branch').prev('div.trigger');
    }
    var result = $(div);
    var parent = getParentTrigger(div);
    while(parent.length>0) {
        result = parent;
        parent = getParentTrigger(parent);
    }
    return result;
}

var markBranch = function(rootOfBranch) {

    parent.knownBugs = 0;
    var markTreeRecursive = function(node) {
        if(!node) return '';
        var status = '';
        if(node.childrens && node.childrens.length>0) {
            $(node.childrens).each(function() {
                var tmp = markTreeRecursive(this);
                if(tmp=='fail' || (tmp=='pass' && status=='')) {
                    status = tmp;
                }
            });
        }
        if(status!='fail' && node.body && node.body.length>0) {
            var tmp = '';
            $(node.body).each(function(){
                if (this.status=='pass' && tmp=='') {
                    tmp = this.status;
                } else if (this.status=='fail') {
                    tmp=this.status;
                    parent.knownBugs++;
                    return;
                }
            })
            status = tmp;
        }
        if (node && node.body && node.body.length>0) {
            if (status == 'pass') {
                $(node.div).children('img[name=issue_img]').attr('src','template/level_info.gif');
            } else {
                $(node.div).children('img[name=issue_img]').attr('src','template/id.png');
            
            }
        } else {
            $(node.div).children('img[name=issue_img]').attr('src','template/fold_clear.gif');
        }
        $(node.div).children('span[class*=title]').css('color',statusColorMap[status]);
        return status;
    }
    markTreeRecursive(rootOfBranch);
    //alert(parent.knownBugs);
}

var issueTablePrefix = '<table class=\'issues\' >'+isssueHeaderTr;
var issueTableSuffix = '</table>';
var issueActionSuffix = '<td><img class="dialog_edit" id="edit" src="edit-icon.png" rel="#dialog_edit" title="Edit issue"><img class="dialog_remove" id="remove" src="remove-icon.png" rel="#dialog_remove" title="Remove issue"></td>';
var drawIssueTable = function(div) {
    var time = new Date().getTime();
    var isIssueExists = false;
    var errorDescription = "";
    if (div.treeNode && div.treeNode.body) {
        var table = issueTablePrefix;
        $(div.treeNode.body).each(function() {
            isIssueExists = true;
            errorDescription = parent.window.frames['details'].document.getElementById('message').innerHTML;
            //alert(errorDescription);
            //alert(parent.window.frames['details'].document.getElementById('issueDetails').innerHTML);
            //alert(parent.window.frames['details'].document.getElementById('message').getElementsByClassName('diff')[0].innerHTML);
            try{
                var diff = parent.window.frames['details'].document.getElementById('message');
                if ( typeof diff!='undefined'){
                    // && typeof diff.getElementsByClassName('diff')!='undefined'
                    alert(iff.getElementsByClassName('diff').length())
                }
            } catch(e){
                //alert(typeof(e));
                //alert(e.description);
                
            }    
            
            table += '<tr id="'+this['id']+'" ><td><a href='+this['url']+' target="_blank">'+this['name']+'</a></td><td id="status_'+this['status']+'">'+this['status']+'</td><td>'+this['description']+'</td>'+issueActionSuffix+'</tr>';
            errorDescription = "";
        })
        table += issueTableSuffix;
    }
    $('#issues',parent.window.frames['details'].document).html(isIssueExists==true?table:"");
    $(function() {
        $(".dialog_remove",parent.window.frames['details'].document).click(function() {
            parent.window.frames['details'].openRemoveDialog(this);
            this.log("drawIssueTable Time: "+(new Date().getTime()-time));
            return false;
        });
    })   
    $(function() {
        $(".dialog_edit",parent.window.frames['details'].document).click(function() {
            parent.window.frames['details'].openEditDialog(this);
            this.log("drawIssueTable Time: "+(new Date().getTime()-time));
            return false;
        });
    });
    
}

var getParentTrigger = function(div) {
    var parent = $(div).parent('div.branch').prev('div.trigger');
    if (parent.length==0) {
        return $(div).attr('class')=='trigger'?$('div#log_name'):parent;
    } else {
        return parent;
    }
    
}

var loadStoredIssue = function(root) {
    
    var time = new Date().getTime();
    var bindJS2DOM = function(par,node,isTop) {
        parent.window.frames['details'].isSWT=true;
        for (var it in node.childrens) {
            var child = node.childrens[it];
            var domChild = isTop==true?$(par).children('div.trigger'):$(par).next('div.branch').children('div.trigger');
            domChild.each(function(){
                if($(this).children('span[class*=title]').text()==child.name) {
                    this.treeNode = child;
                    this.treeNode.div = this;
                    bindJS2DOM(this,child,false);
                }
            })
        }
    }
    if (typeof(parent.window.execute)=='function') {
    
        tree.root = eval('('+parent.window.execute('load',root)+')');
        var par = $("html").children("body");
        bindJS2DOM(par,tree.root,true);
        
    }
    this.log("loadStoredIssue Time: "+(new Date().getTime()-time));
}

var change = function(action, issue, node) {
    if (typeof(parent.window.execute)=='function') {
        return parent.window.execute(action,tree.issue2JSON(issue),tree.node2JSON(node));
    } else {
        alert('Report doesn`t open in editor.');
    }
    return issue;
}

var log = function(arg) {
    if (typeof(parent.window.log)=='function') {
        return parent.window.log(arg);
    }
}

var execute = function(action, arg0, arg1) {
    if (typeof(parent.window.execute)=='function') {
        return parent.window.execute(action,arg0,arg1);
    }
    return arg0;
}

var add = function(issue, node) {
    return execute('add',tree.issue2JSON(issue),tree.node2JSON(node));
}

var remove = function(id) {
    return execute('remove',id);
}

var edit = function(issue) {
    return execute('edit',tree.issue2JSON(issue));
}

var getNode = function(div) {
    $(div).each(function(){
        if(!this.treeNode) {
            this.treeNode = new Tree.TreeNode();
            this.treeNode.div = this;
            if ($(this).attr('class')=='trigger') {
                this.treeNode.name = $(this).children('span[class*=title]').text();
            } else {
                this.treeNode.name = $(this).text();
            }
            var parent = getParentTrigger(this);
            if (parent.length>0) {
                var parentNode = getNode(parent);
                this.treeNode.parent = parentNode;
                parentNode.childrens.push(this.treeNode);
                this.treeNode.parent = parentNode;
            } else if(typeof(tree.root)=='undefined' || typeof(tree.root.name)=='undefined') {
                tree.root = this.treeNode;
            }
        }
    })
    var res;
    if(typeof($(div)[0].treeNode)=='undefined') {
        res = tree.root;
    } else {
        res = $(div)[0].treeNode;
    }
    return res;
}

$(document).ready(function() {
    try {
        loadStoredIssue($('div#log_name').text());
    }catch(e){
        alert(document.readyState+'    '+e.message);
    }
    

    $('div.trigger').click(function(){
        parent.window.frames['details'].div = this;
        drawIssueTable(this);
    });
    
    markBranch(tree.root);
});






var elm = parent.window.frames['details'].document.getElementById('issues').children
var test ;
for(i=0;i<elm.length;i++){
    if ("failIssueDetails" == elm[i].className){
        if (/Expected text|Expected presence/.test(elm[i].innerHTML)) {
            test = elm[i].innerHTML.split("Expected");

        }
    }
}




function compare(test,control){
    //// test.sort() ?????
    if (control.length != test.length) {
        return false;
    }
    var pass = false;
    for (var testIndx in test ) {
        pass = false
        for(var controlIndx in control) {
            if (control[controlIndx] == test[testIndx]){
                pass = true;
                break;
            } 
        }
        if (!pass) {
            false
        }

    }
    return true;
}