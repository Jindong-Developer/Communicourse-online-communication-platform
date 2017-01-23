package controllers

import model.{User, UserForm}
import model.{Chatgroup, ChatgroupForm}
import java.io.File
import play.api.mvc._
import scala.concurrent.Future
import service.UserService
import service.ChatgroupService
import scala.concurrent.ExecutionContext.Implicits.global



import java.net.URL
import javax.inject._

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.Materializer
import akka.stream.scaladsl.{BroadcastHub, Flow, Keep, MergeHub, Source}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}



class UserController extends Controller {


  
  def in_password = Action{ 
      Ok(views.html.change_password(UserForm.form, Seq.empty[User]))
  }
  
  def register = Action{ 
      Ok(views.html.register(UserForm.form, Seq.empty[User]))
    
  }
  
  def login = Action.async { 

      implicit request =>
      UserService.checkUser(UserForm.form.bindFromRequest.get.userName,UserForm.form.bindFromRequest.get.password) map { users => 
      if (users.isEmpty)
        Ok("Password or Username Wrong!")
      else
        Redirect(routes.ChatgroupController.list_members_owner(users.head.userName))
          .withSession("userid"->users.head.id.toString)
          
      }
    
  }
    
    
  
  def update_password = Action.async { 

      implicit request =>
      val userid  =request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
      val password=UserForm.form.bindFromRequest.get.password
      val new_password=UserForm.form.bindFromRequest.get.identify
      UserService.checkPassword(userid.toLong,password) map { users => 
      if (users.isEmpty)
        Ok("Wrong password")
      else
      {
        UserService.update_password(userid,new_password)
        Ok("Success")
      }
    }
    
  }
  
  def getUser = Action.async { 
      
      implicit request =>
      var userid  =request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
      UserService.getUser(userid.toLong) map { users => 
      Ok(views.html.change_user_information(UserForm.form, users))
      }
  }

    def update_personalinfor = Action.async { 

      implicit request =>
      val username=UserForm.form.bindFromRequest.get.userName
      val email   =UserForm.form.bindFromRequest.get.email
      var userid  =request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
   
      UserService.update_personalinfor(userid,username,email).map { res => 
      Ok("Successful")
      }
    }
    
  def addUser() = Action.async { implicit request =>
    UserForm.form.bindFromRequest.fold(
        errorForm => Future.successful(Ok(("Fail"))),
      data => {
        val newUser = User(0, data.identify, data.userName, data.password, data.email , "" , "")
        UserService.addUser(newUser).map(res =>
          Ok("Successful")
        )
      })
  }

  def deleteUser(id: Long) = Action.async { implicit request =>
    UserService.deleteUser(id) map { res =>
      Redirect("routes.UserController.index()")
    }
  }
  
  def getUsers_nameandidentifi(content: String) = Action.async { implicit request =>
    UserService.listAllUsers map { users =>
    val members=content.split('|')(1)
    var n_a_d = ""
    for(user <- users){
        if(members.contains(user.id.toString))
        n_a_d=n_a_d+user.userName+":"+user.identify+";"
    }
    val new_content=content.split('|')(0)+"|"+n_a_d
    Redirect(routes.MessageController.listAll(new_content))
    }
  }
}

