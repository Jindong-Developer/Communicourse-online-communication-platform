package controllers

import model.{Message, MessageForm}
import model.{User, UserForm}
import java.io.File
import play.api.mvc._
import scala.concurrent.Future
import service.MessageService
import service.UserService
import scala.concurrent.ExecutionContext.Implicits.global
import java.text.SimpleDateFormat  
import java.util.Date 


import play.api.mvc.BodyParsers.parse.multipartFormData
import play.api.mvc.MultipartFormData.FilePart
import play.api.libs.iteratee.Iteratee
import java.io.ByteArrayOutputStream
import play.api.mvc.BodyParser
import play.api.mvc.MultipartFormData

class MessageController extends Controller {
 


  def addMessage_text() = Action.async { 
   
    implicit request =>
    val content=MessageForm.form.bindFromRequest.get.content
      MessageForm.form.bindFromRequest.fold(
      errorForm => Future.successful(Ok(content)),
       data => { 
             var now = new Date()
             var dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
             var time = dateFormat.format( now )
             var userid=request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
           
             val newMessage = Message(0, "text",data.content, 27, userid,time)
       
             MessageService.addMessage(newMessage).map(res =>
             Ok("success")
             )
      })
  }
  
  
  def addMessage_file(chat_group:String ) = Action(parse.multipartFormData) { request =>
        // val chat_group=MessageForm.form.bindFromRequest.get.chat_group
        request.body.file("document").map { doc =>
            
            val filename = doc.filename
            var file_type="doc"
            var username=request.session.get("username").toString.slice(5,request.session.get("username").toString.length-1)
           
            if(filename.contains(".jpg") || filename.contains(".png") ||filename.contains(".gif")){
                file_type="img"
                doc.ref.moveTo(new File(s"public/img/$filename"))
            } 
            else
                doc.ref.moveTo(new File(s"public/documents/$filename"))
            
            var now = new Date()
            var dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var time = dateFormat.format( now )
            var userid=request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
            val newMessage = Message(0, file_type,username+":"+filename, chat_group.toInt, userid,time)
            MessageService.addMessage(newMessage)
           
            Ok("file")
            //Ok("File uploaded"+username)
        }.getOrElse {
            Ok("success")  
        }
    }
    
    /*
    def download_file(file_name:String ) = Action { 
        Ok.sendFile(new java.io.File(s"public/documents/$file_name"))
    }*/

 
    
  


    def listAll(content:String) = Action.async { implicit request =>
    MessageService.listAllMessages map { messages => 
       val idc=content.split('|')(0)
       val new_messages= messages.filter(_.chat_group ==  idc )   
       val url ="ws://localhost:9000/socket/"+idc
       var username=request.session.get("username").toString.slice(5,request.session.get("username").toString.length-1)
    
       var new_content=content+"|"+url+"|"+username

       Ok(views.html.chat_room(MessageForm.form, new_messages,new_content))
       //Ok(request.session.get("username").toString)
       // Ok(views.html.createchatgroup(ChatgroupForm.form,Seq.empty[Chatgroup],"users.head.id.toString"))
       }
    }
    
    
  
}

