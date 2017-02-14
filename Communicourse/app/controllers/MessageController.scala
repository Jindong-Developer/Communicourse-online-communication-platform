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
 


    def addMessage_text(content:String) = Action.async { 
   
        implicit request =>

        var message_content = content.split('|')(0)
        var group_id = content.split('|')(1).toLong
        var now = new Date()
        var dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var time = dateFormat.format( now )
       var userid=request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
        val newMessage = Message(0, "txt",message_content, group_id, userid,time)
        MessageService.addMessage(newMessage).map(res =>
        Ok("add_Message_success")
        ) 
    }
  
  
   def addMessage_file(chat_group:String ) = Action(parse.multipartFormData) { request =>
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
           
            Ok(views.html.upload())
        }.getOrElse {
            Ok("success")  
        }
    }
    
    
    def download_doc(doc_name:String ) = Action { 
        Ok.sendFile(new java.io.File(s"public/documents/$doc_name"))
    }
    
    def download_img(img_name:String ) = Action { 
        Ok.sendFile(new java.io.File(s"public/img/$img_name"))
    }

 
    
  


    def listAll(content:String) = Action.async { implicit request =>
       MessageService.listAllMessages map { messages => 
       val idc=content.split('|')(0).toInt
       val new_messages= messages.filter(_.chat_group ==  idc )   
       val url ="ws://localhost:9000/socket/"+idc
       var username=request.session.get("username").toString.slice(5,request.session.get("username").toString.length-1)
       val userid=request.session.get("userid").toString.slice(5,request.session.get("userid").toString.length-1)
   
       var new_content=content+"|"+url+"|"+username+"|"+userid

       Ok(views.html.chat_room(MessageForm.form, new_messages,new_content))
       }
    }
    
  
}

