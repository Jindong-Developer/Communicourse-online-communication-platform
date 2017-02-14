package controllers

import model.{Chatgroup, ChatgroupForm}
import model.{User, UserForm}
import model.{Message, MessageForm}
import play.api.mvc._
import scala.concurrent.Future
import service.ChatgroupService
import service.UserService
import scala.concurrent.ExecutionContext.Implicits.global
import java.text.SimpleDateFormat  
import java.util.Date   

class ChatgroupController extends Controller {
 
   


    
   def createchatgroup_processdata = Action.async { 

       implicit request =>
       UserService.listAllUsers map { users => 
           val groupname=ChatgroupForm.form.bindFromRequest.get.group_name
           val userid=request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
           val description=  ChatgroupForm.form.bindFromRequest.get.description
           var members = ChatgroupForm.form.bindFromRequest.get.members
           var test= "no"
           var members_id=","
    
           for(user <- users)
           {
              if(members.contains(user.email))
                members_id=members_id+user.id.toString+","      
            }
            var content=groupname+":"+members_id+":"+userid+":"+description+":"+test
            Redirect(routes.ChatgroupController.createChatgroup(content))
        }
    }
  
  
  
    def createChatgroup(content:String) = Action.async { implicit request =>
      val groupname= content.split(':')(0)
      val members = content.split(':')(1)
      val userid = content.split(':')(2)
      val description = content.split(':')(3)
      var now = new Date()
      var  dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      var time = dateFormat.format( now )
      val newChatgroup = Chatgroup(0, groupname, members, userid,time,description)
      ChatgroupService.addChatgroup(newChatgroup).map(res =>
         Ok("Successful cc123456789"+content.split(':')(4)+content.split(':')(1))//.withSession("chatgroup" ->request.session.get("chatgroup").toString.slice(5,request.session.get("chatgroup").toString.length-1)+data.owner+",")
         )
    }
    
    
    
   def updatechatgroup_members_processdata(content:String) = Action.async { 

      implicit request =>
     UserService.listAllUsers map { users => 
    var members =content.split(':')(1)
    var chatgroup_id= content.split(':')(0)
    var members_id=""
    
    for(user <- users)
    {  
        if(members.contains(user.email))
          members_id=members_id+user.id.toString+","      
    }
    var new_content=chatgroup_id+":"+members_id
      Redirect(routes.ChatgroupController.getchatgroup_members(new_content))
     }
    
  }
  
  def getchatgroup_members(content:String) = Action.async { 

      implicit request =>
      var idc=content.split(':')(0).toLong
      ChatgroupService.getChatgroup(idc.toLong) map { chatgroups =>
      var members = chatgroups.head.members.toString+content.split(':')(1)
      var new_content=content.split(':')(0)+":"+members
      Redirect(routes.ChatgroupController.updatechatgroup_members(new_content))
     }
  }
  
   def updatechatgroup_members(content:String) = Action.async { 

      implicit request =>
      var idc=content.split(':')(0)
      var members =content.split(':')(1)
      ChatgroupService.update_chatgroup_members(idc,members)  map { chatgroups =>
      Ok("update_members_sucess")
      }
    }
  
  

    def goto_createchatgroup = Action{ 
      Ok(views.html.create_chatgroup(ChatgroupForm.form, Seq.empty[Chatgroup]))
    }

    def deleteChatgroup(id: Long) = Action.async { implicit request =>
      var userid=request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
      ChatgroupService.deleteChatgroup(id) map { res =>
        Redirect(routes.ChatgroupController.list_owner())
      }
    }


    def listAll = Action.async { implicit request =>
      ChatgroupService.listAllChatgroups map { chatgroups => 
        Ok("List_All_Group")
      }
    }
    
    def getChatgroup(idc:String) = Action.async { implicit request =>
      ChatgroupService.getChatgroup(idc.toLong) map { chatgroups => 
      val content=idc+"|"+chatgroups.head.members
       Redirect(routes.UserController.getUsers_nameandidentifi(content))
      }
    }
    
    def list_owner = Action.async { implicit request =>
      ChatgroupService.listAllChatgroups map { chatgroups => 
      val userid  =request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
      val new_chatgroups= chatgroups.filter(_.owner ==  userid)
      Ok(views.html.list_owner(ChatgroupForm.form, new_chatgroups,userid))
      }        
    }
    
    def list_members = Action.async { implicit request =>
      ChatgroupService.listAllChatgroups map { chatgroups => 
      val userid  =request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
      val new_chatgroups= chatgroups.filter(_.members.contains(","+userid+","))
      Ok(views.html.list_members(ChatgroupForm.form, new_chatgroups,userid))
      }
    }
    
    
    def list_members_owner(username:String) = Action.async { implicit request =>
      ChatgroupService.listAllChatgroups map { chatgroups => 
      val userid  =request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
      val member_chatgroups= chatgroups.filter(_.members.contains(","+userid+",") )
      val owner_chatgroups= chatgroups.filter(_.owner ==  userid)
      Ok(views.html.main(ChatgroupForm.form, owner_chatgroups,member_chatgroups)).withSession( request.session+ ("username" ->username))
      }
    } 
    
    def aftercreatechatgroup_update() = Action.async { implicit request =>
      ChatgroupService.listAllChatgroups map { chatgroups => 
      
      val userid  =request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
      val member_chatgroups= chatgroups.filter(_.members.contains(","+userid+",") )
      val owner_chatgroups= chatgroups.filter(_.owner ==  userid)
      Ok(views.html.main(ChatgroupForm.form, owner_chatgroups,member_chatgroups))
      }
    }  
   
   
    def exitChatgroup(content:String) = Action.async { implicit request =>
      var members=content.split(':')(1)
      var userid=content.split(':')(2)
      ChatgroupService.update_chatgroup_members(content.split(':')(0), members.replace(","+userid+","  , ",")) map { chatgroups => 
        Redirect(routes.ChatgroupController.list_members())
      }
    }
 
}

