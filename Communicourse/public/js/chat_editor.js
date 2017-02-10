var inputForm;
var K;
var htmlEditor;


Ext.onReady(function () {

	Ext.QuickTips.init();
	
	var chat_content = new Ext.Panel({
		id: 'chat_content',
		height: 250,
	    width: 600,
		border: true,
		bodyStyle:'background:#f8fAfA;',
		html: '',
	});
	
	var group_member = groupmembers();

	var editor = new Ext.Panel({
    		width: '600',
            height: '150',
            name: 'content',
            id: 'content',
    		bodyStyle:'background:#f8fAfA',
    		items:
    			[{
    				xtype: 'displayfield',
    				html :'<form action="form_action.asp" method="get"><input type="file"/> </form><br><textarea id="message_send" rows="10" cols="200"></textarea>',
    				bodyStyle:'background:#ff0000',
                    //bodyStyle:'background-image: url("./images/doc.png")'
    			}]

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
				    var chatContent=Ext.getCmp('chat_content');
					chatContent.body.update(document.getElementById('message_send').value);
					document.getElementById('message_send').value='';
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
			        bodyStyle:'padding:5px 3px 0px 10px',
                    items:[chat_content]  
                },{  
                    columnWidth: 1,
                    bodyStyle:'padding:5px 0px 0px 0px',
                    items:[group_member]  
                },{  
                    columnWidth: 1,
                    baseCls:'x-plain',  
                    bodyStyle:'padding:0px 0 0px 10px',
                    items:[editor]
                },{  
                    columnWidth: 1,  
                    baseCls:'x-plain',  
                    bodyStyle:'padding:3px',
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