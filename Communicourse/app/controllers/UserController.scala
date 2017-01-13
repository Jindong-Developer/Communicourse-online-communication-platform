package controllers

import model.{User, UserForm}
import model.{Chatgroup, ChatgroupForm}
import play.api.mvc._
import scala.concurrent.Future
import service.UserService
import scala.concurrent.ExecutionContext.Implicits.global

class UserController extends Controller {

  def index = Action{ 
      Ok(views.html.login(UserForm.form, Seq.empty[User]))
    
  }
  
  def register = Action{ 
      Ok(views.html.register(UserForm.form, Seq.empty[User]))
    
  }
  
  def login = Action.async { 
   

      
       
      implicit request =>
    UserService.checkUser(UserForm.form.bindFromRequest.get.userName,UserForm.form.bindFromRequest.get.password) map { users => 
    if (users.isEmpty)
      Ok(views.html.login(UserForm.form, users))

    else
     // Ok(views.html.createchatgroup(ChatgroupForm.form,Seq.empty[Chatgroup],users.head.id.toString)).withSession("userid" ->users.head.id.toString).withSession("chatgroup" ->users.head.chat_grupe.toString)
    
      Redirect("/listAllChatgroup").withSession("userid" ->users.head.id.toString)//.withSession("chatgroup" ->users.head.chat_grupe.toString)
    }
    
    
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
      })*/
  }

  def addUser() = Action.async { implicit request =>
    UserForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => Future.successful(Ok(views.html.login(errorForm, Seq.empty[User]))),
      data => {
        val newUser = User(0, data.identify, data.userName, data.password, data.email,data.chat_grupe,data.quizzes,data.favourites)
        UserService.addUser(newUser).map(res =>
          Redirect(routes.UserController.index())
        )
      })
  }

  def deleteUser(id: Long) = Action.async { implicit request =>
    UserService.deleteUser(id) map { res =>
      Redirect(routes.UserController.index())
    }
  }

}

