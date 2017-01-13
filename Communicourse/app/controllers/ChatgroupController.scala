package controllers

import model.{Chatgroup, ChatgroupForm}
import model.{User, UserForm}
import play.api.mvc._
import scala.concurrent.Future
import service.ChatgroupService
import service.UserService
import scala.concurrent.ExecutionContext.Implicits.global

class ChatgroupController extends Controller {

  /*def index = Action.async { implicit request =>
    ChatgroupService.listAllUsers map { chatgroups => 
      Ok(views.html.login(ChatgroupForm.form, chatgroups))
    }
  }*/
  
 /* def login = Action.async { 
   

      
       
      implicit request =>
    UserService.checkUser(UserForm.form.bindFromRequest.get.userName,UserForm.form.bindFromRequest.get.password) map { users => 
    if (users.isEmpty)
      Ok(views.html.login(UserForm.form, users))

    else
      Ok(views.html.index(users.head.id.toString)).withSession("userid" ->users.head.id.toString)
    }*/
    
    
    /*implicit request =>
    UserForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => Future.successful(Ok(views.html.register(errorForm, Seq.empty[User]))),
      data => {
  
        UserService.checkUser(data.userName,data.password).map(users =>
          if (users.isEmpty)
        Ok(views.html.b("fail"))
          else
        Ok(views.html.b("success"))
        )
      })
  }*/

  def addChatgroup() = Action.async { 
     /* var now = new Date()
    var  dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var hehe = dateFormat.format( now )*/
      
      
      implicit request =>
    ChatgroupForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => Future.successful(Ok(views.html.b("errorForm, Seq.empty[User]"))),
      data => {
        val newChatgroup = Chatgroup(0, data.group_name, "," , data.owner,"sdfgfdg")
        //UserService.update_chatgroup(data.owner, UserService.getUser(data.owner).chat_grupe +data.owner+"," )
        ChatgroupService.addChatgroup(newChatgroup).map(res =>
          Ok(views.html.b("success"))//.withSession("chatgroup" ->request.session.get("chatgroup").toString.slice(5,request.session.get("chatgroup").toString.length-1)+data.owner+",")
        )
      })
  }

  def deleteChatgroup(id: Long) = Action.async { implicit request =>
    var userid=request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
   // UserService.update_chatgroup(userid,
     //                         request.session.get("chatgroup").toString.slice(5,request.session.get("chatgroup").toString.length-1).replace(","+id+"," , ",") )
    ChatgroupService.deleteChatgroup(id) map { res =>
      Redirect(routes.ChatgroupController.listAll())
    }
  }


    def listAll = Action.async { implicit request =>
    ChatgroupService.listAllChatgroups map { chatgroups => 
       Ok(views.html.exitchatgroup(ChatgroupForm.form, chatgroups,request.session.get("userid").toString))
    
       // Ok(views.html.createchatgroup(ChatgroupForm.form,Seq.empty[Chatgroup],"users.head.id.toString"))
    }
    }
    
    def exitchatgroup(content:String) = Action.async { implicit request =>
    var members=content.split(':')(1)
    var userid=content.split(':')(2)
    ChatgroupService.update_chatgroup(content.split(':')(0), members.replace(","+userid+","  , ",")) map { chatgroups => 
       Redirect(routes.ChatgroupController.listAll())
    
       // Ok(views.html.createchatgroup(ChatgroupForm.form,Seq.empty[Chatgroup],"users.head.id.toString"))
    }
    }
    
    
  
}

