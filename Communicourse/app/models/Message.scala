package model

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global



case class Message(idm: Long, typee: String, content: String, chat_group: Long, user: String, created_time : String)

case class MessageFormData(content: String,  chat_group: Int ,user: String )

object MessageForm {

  val form = Form(
    mapping(
      
      "content" -> nonEmptyText,
      "chat_group" -> number,
      "user" -> nonEmptyText
      
    )(MessageFormData.apply)(MessageFormData.unapply)
  )
}

class MessageTableDef(tag: Tag) extends Table[Message](tag, "message") {

  def idm = column[Long]("idm", O.PrimaryKey,O.AutoInc)
  def typee = column[String]("typee")
  def content = column[String]("content")
  def chat_group = column[Long]("chat_group")
  def user = column[String]("user")
  def created_time = column[String]("created_time")


 override def * =
    (idm, typee, content, chat_group, user,created_time) <>(Message.tupled, Message.unapply)
}


object Messages {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val messages = TableQuery[MessageTableDef]

  def add(message: Message): Future[String] = {
    dbConfig.db.run(messages += message).map(res => "User successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  
  def get(idm: Long): Future[Option[Message]] = {
    dbConfig.db.run(messages.filter(_.idm === idm).result.headOption)
  }
  
 

  def listAll: Future[Seq[Message]] = {
    dbConfig.db.run(messages.result)
  }
  
  def listSome(idm:Long): Future[Seq[Message]] = {
    dbConfig.db.run(messages.filter(_.idm === idm).result)
  }
  
 
  
 

}
