var inputForm;
var K;
var htmlEditor;

Ext.onReady(function () {

	Ext.QuickTips.init();
	
	var chat_content = new Ext.Panel({
		id: 'chat_content',
		height: 350,
		width: 700,
		border: true,
		bodyStyle:'background:#f8fAfA;',
	});
	
	var group_member = new Ext.Panel({
		id: 'group_member',
		title: 'Group Memebers:',
		height: 350,
		width: 200,
		border: true,
		bodyStyle:'background:#f8fAfA',
		items:
			[{
				xtype: 'displayfield',
				html :'<br><p>Wentao: student</p><br><p>Robert: tutor</p><br><p>Jindong: student</p>',
				autoScroll:1,
				id: 'group_members',
				width:200,
				height:200,
			},{
				xtype: 'button',
				text: 'Invite',
				id: 'invite',
                width:80,
				height:30,
				bodyStyle:'margin-left:10px',
				handler: function () {
					//  alert("invite");
			          inviteMember();	   
				}
			}]

	});

	var myeditor = new Ext.form.TextArea({
		width: '800',
		height: '150',
		name: 'content',
		id: 'content',
		emptyText: 'empty',
		anchor: '80%',
		listeners: {
			"render": function (f) {
				K = KindEditor;
				htmlEditor = K.create('#content', {
					uploadJson: './kindeditor/upload_json.jsp',
					fileManagerJson: './kindeditor/file_manager_json.jsp',
					height: 200,
					width: 700,
					resizeType: 1,
					allowPreviewEmoticons: true,
					allowImageUpload: true
				});
			}
		}
	});


	var buttonPanel = new Ext.Panel({
		id: 'buttonPanel',
		height: 50,
		border: false,
		items:
			{
				xtype: 'button',
				text: 'Send',
				id: 'finsh',
				width:80,
				height:30,
				name: 'finsh',
				handler: function () {
					Ext.Msg.alert('', htmlEditor.html());	   
				}
			}
	});
	
	inputForm = new Ext.form.FormPanel({
		collapsible: false,
		border: true,
		
		id: 'inputForm',
		name: 'inputForm',
		encType: "multipart/form-data",
		labelWidth: 0,
		labelAlign: 'right',
		buttonAlign: 'center',
		autoScroll: true,
		fileUpload: true, 
        layout:'table',  
		layoutConfig:{columns:2},
		items: [{  
                    columnWidth: 1,
			        baseCls:'x-plain',
					height: 355,
		            width: 720,
			        bodyStyle:'padding:5px 0 0px 20px',
                    items:[chat_content]  
                },{  
                    columnWidth: 1,
					height: 350,
		            width: 200,
                    bodyStyle:'padding:1px',  
                    items:[group_member]  
                },{  
                    columnWidth: 1,  
                    baseCls:'x-plain',  
                    bodyStyle:'padding:0px 0 0px 20px',  
                    items:[myeditor]  
                },{  
                    columnWidth: 1,  
                    baseCls:'x-plain',  
                    bodyStyle:'padding:1px',  
                    items:[buttonPanel]  
                }]
	});

	var viewport = new Ext.Viewport({
		layout: 'fit',
		items: [inputForm]
	});

	function resetForm() {
		inputForm.form.reset();

	}


});