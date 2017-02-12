function logout() {
	var domain=document.URL;
    var domains=domain.split("/");
    var domain2=domains[0]+"//"+domains[2];  
    window.location.href=domain2;
}

//生成标签页
var tab = new Ext.TabPanel({
    region:'center',
    deferredRender:false,
    activeTab:0,
    resizeTabs:true,
    minTabWidth: 115,
    tabWidth:135,
    enableTabScroll:true,
    html:'<iframe id="centerFrame" scrolling="auto" frameborder="0" width="100%" height="100%"></iframe>'
});


//载入目标页
function actionFn(node, url) {
    return tab.add({
        'id':node.id,
        'title':node.text,
        closable:true,  //通过html载入目标页
        html:'<iframe id="centerFrame" scrolling="auto" frameborder="0" width="100%" height="100%" src="' + url + '"></iframe>'
    });
}

Ext.onReady(function() {
    //layout
    var viewport = new Ext.Viewport({
        layout:'border',
        items:[
            new Ext.BoxComponent({
                region:'north',
                el: 'north',
                height:100
            }),{
        region: 'east',
        title: 'East Panel',
        collapsible: true,
	
        split: true,
        width: 300
    },new Ext.BoxComponent({
                region:'south',
                el: 'south',
                height:25
            }),{
                region:'west',
                id:'west-panel',
                split:true,
                width: 200,
                minSize: 175,//175
                maxSize: 400,
                margins:'0 0 0 0',
                layout:'accordion',
                title:'User Name',
                collapsible: true,
                layoutConfig:{
                    animate:true,
                },
                items: [
					{
						title:'Chat Groups',
						border:false,
						html:'<div id="tree1" style="overflow:auto;width:100%;height:100%" ></div>',
						//iconCls:'nav'
					},
					{
						title:'Groups Management',
						border:false,
						//iconCls:'settings',
						html:'<div id="tree2" style="overflow:auto;width:100%;height:100%"></div>',
						},
					{
						title:'Quizzes Management',
						border:false,  
						//iconCls:'settings',
						html:'<div id="tree3" style="overflow:auto;width:100%;height:100%"></div>'
					},
                    {
                        title:'Setting',
                        border:false,
                        //iconCls:'settings',
                        html:'<div id="tree4" style="overflow:auto;width:100%;height:100%"></div>'
                    }
                ]
            },
            tab//初始标签页
        ]
    });
	
	var chatGroup_root=chatGroups();
	
    var tree1 = new Ext.tree.TreePanel({
        renderTo:"tree1",
        root:chatGroup_root,  
        animate:true,
        enableDD:false,
        border:false,
        rootVisible:false,
        containerScroll: true,
    });
	
	


    var root2 = new Ext.tree.TreeNode({
        id:"root2",
		collapsible :false,
        text:"root2"
    });
	var MyGroups= new Ext.tree.TreeNode({
        id:'MyGroups',
        icon:'images/group.gif',
        text:'My Groups&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
        listeners:{
            'click':function(node, event) {
                event.stopEvent();
                var n = tab.getComponent(node.id);
                var url = 'list_owner';
                if (!n) { //if the pannel is already opened.
                    n = actionFn(node, url);
                } else {
                    tab.remove(n);
                    n = tab.getComponent(node.id);
                    n = actionFn(node, url);
                }
                tab.setActiveTab(n);
            }
        }
    });
	var ParticipantGroups= new Ext.tree.TreeNode({
        id:'ParticipantGroups',
        icon:'images/group.gif',
        text:'Participant Groups&nbsp;&nbsp;',
        listeners:{
            'click':function(node, event) {
                event.stopEvent();
                var n = tab.getComponent(node.id);
                var url = 'list_members';
                if (!n) { //if the pannel is already opened.
                    n = actionFn(node, url);
                } else {
                    tab.remove(n);
                    n = tab.getComponent(node.id);
                    n = actionFn(node, url);
                }
                tab.setActiveTab(n);
            }
        }
    });
	var NewGroup= new Ext.tree.TreeNode({
        id:'NewGroup',
        icon:'images/group.gif',
        text:'Create a new Group',
        listeners:{
            'click':function(node, event) {
                event.stopEvent();
                var n = tab.getComponent(node.id);
                var url = 'goto_createchatgroup';
                if (!n) { //if the pannel is already opened.
                    n = actionFn(node, url);
                } else {
                    tab.remove(n);
                    n = tab.getComponent(node.id);
                    n = actionFn(node, url);
                }
                tab.setActiveTab(n);
            }
        }
    });
	root2.appendChild(MyGroups);
	root2.appendChild(ParticipantGroups);
	root2.appendChild(NewGroup);
    var tree2 = new Ext.tree.TreePanel({
		renderTo:"tree2",
		collapsible :false,
		root:root2,
		animate:true,
		enableDD:false,
		border:false,
		rootVisible:false,
		containerScroll: true
	});
	
	var root4 = new Ext.tree.TreeNode({
        id:"root4",
		collapsible :false,
        text:"root4"	
    });
	var Profile= new Ext.tree.TreeNode({
        id:'Profile',
        icon:'images/user.gif',
        text:'Profile&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
        listeners:{
            'click':function(node, event) {
                event.stopEvent();
                var n = tab.getComponent(node.id);
                var url = 'getUser';
                if (!n) { //if the pannel is already opened.
                    n = actionFn(node, url);
                } else {
                    tab.remove(n);
                    n = tab.getComponent(node.id);
                    n = actionFn(node, url);
                }
                tab.setActiveTab(n);
            }
        }
    });
	var PasswordReset= new Ext.tree.TreeNode({
        id:'PasswordReset',
        icon:'images/key.gif',
        text:'Password',
        listeners:{
            'click':function(node, event) {
                event.stopEvent();
                var n = tab.getComponent(node.id);
                var url = 'inpassword';
                if (!n) { //if the pannel is already opened.
                    n = actionFn(node, url);
                } else {
                    tab.remove(n);
                    n = tab.getComponent(node.id);
                    n = actionFn(node, url);
                }
                tab.setActiveTab(n);
            }
        }
    });
	var Logout= new Ext.tree.TreeNode({
        id:'Logout',
        icon:'images/logout.png',
        text:'Log out&nbsp;&nbsp;&nbsp;&nbsp;',
        listeners:{
            'click':function(node, event) {
                event.stopEvent();
				logout();		           
            }
        }
    });
	root4.appendChild(Profile);
	root4.appendChild(PasswordReset);
	root4.appendChild(Logout);
    var tree4 = new Ext.tree.TreePanel({
		renderTo:"tree4",
		collapsible :false,
		root:root4,
		animate:true,
		enableDD:false,
		border:false,
		rootVisible:false,
		containerScroll: true
	});

    

});
